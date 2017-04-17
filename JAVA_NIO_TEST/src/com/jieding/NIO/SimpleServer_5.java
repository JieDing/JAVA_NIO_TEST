package com.jieding.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleServer_5 {

	public static void main(String[] args) {
		ServerSocketChannel ssc = null;
		try {
			//ͨ��ServerSocketChannel.open()������һ��û�а󶨵ķ����׽���ͨ��
			ssc = ServerSocketChannel.open();  
			//ssc.socket()��ȡ��ͨ���󶨵�ServerSocket��Ȼ���ڽ���ServerSocket��ָ���Ķ˿�
			ssc.socket().bind(new InetSocketAddress(8191));
			//����ͨ��Ϊ�����������ܹ�����ͨ��ע�ᵽ��·��������ȥ
			ssc.configureBlocking(false);
			
			new Thread(new ReactorThread(ssc)).start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//���ServerSocketChannelδ��ʼ���ɹ�������ServerSocket�󶨵Ķ˿ڱ�ռ��
			//������˳�
			System.exit(1);
			e.printStackTrace();
		}
	}

}

class ReactorThread implements Runnable{
	Selector sel = null;
	volatile boolean stop = false;
	public ReactorThread(ServerSocketChannel ssc){
		try {
			//��ʼ��һ���µĶ�·������
			sel = Selector.open();
			//��ServerSocketChannelע�ᵽselector��ȥ�����Selector�ڼ�����ͨ��ʱ���Ը�ͨ������
			//���ܾ����¼�����Ȥ,һ����Channelע�ᵽ�������ϣ�selector�����һ����Channel��SelectionKey
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			System.out.println("Server is running");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//�����ʼ��Selectorʧ��������˳�
			System.exit(1);
			e.printStackTrace();
		}
	}
	
	public void stop(){
		this.stop = true;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop){
			try {
				//ÿ��1s����selector1�Σ�������selector�и���Ȥ�¼�������ͨ��
				sel.select(1000);
				//��ȡ��ǰ����׼��������SelectionKey
				Set<SelectionKey> selectionKeys = sel.selectedKeys();
				//������ĵ�����
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try{
						handleKey(key);
					}catch(IOException e){
						if(key!=null){
							key.cancel();
							if(key.channel()!=null)
								key.channel().close();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sel != null){
			//�رն�·����������������ע���Channel���ᱻ�Զ��رգ���������ظ��ر�
			try {
				sel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void handleKey(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
		if(key.isValid()){
			//�����key��Ӧ��Channel׼���ý����µ�socket����
			if(key.isAcceptable()){
				//ȡ��key����Ӧ��Channel����ʱ��ChannelӦ��ΪServerSocketChannel
				ServerSocketChannel ssh = (ServerSocketChannel) key.channel();
				//������ӵ���ServerSocket�ϵ�socket��Channel
				SocketChannel sc = ssh.accept();
							
				//����SocketChannel����Ϊ������
				sc.configureBlocking(false);
				//���ǹ���incomingSocket�Ƿ��ܹ���ͻ���socket�������
				sc.register(sel, SelectionKey.OP_WRITE);
				
			}
		}
		//�����key��Ӧ��Channelд����
		if(key.isWritable()){
			//��ʱscΪincoming socket ����Ӧ��Channel
			SocketChannel sc = (SocketChannel) key.channel();
			String content = "this is the message from the server\n";
			String content2 = "hahaha\n";
			byte[] bytes = content.getBytes("UTF-16BE");
			byte[] bytes2 = content2.getBytes("UTF-16BE");
			//�������������������СΪ����ַ����ֽڳ���
			ByteBuffer buff = ByteBuffer.allocate(bytes.length+bytes2.length);
			System.out.println("������ȣ�"+bytes.length+bytes2.length);
			buff.put(bytes);
			buff.put(bytes2);
			//���������л�Ϊ��ģʽ���ȴ�Channel���������е��ֽ�д�뵽ͨ����ȥ
			buff.flip();
			
			sc.write(buff);
			//��Key�Ӽ�����ɾ����
			key.cancel();
			sc.close();
		}
	}

}
