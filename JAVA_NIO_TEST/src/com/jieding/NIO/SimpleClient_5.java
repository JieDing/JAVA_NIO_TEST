package com.jieding.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleClient_5 {
	static volatile boolean stop = false;
	static Selector sel = null;
	
	public static void main(String[] args) {
		
		SocketChannel sc = null;
		
		
		try {
			//��ʼ����·������selector
			sel = Selector.open();
			//��ȡһ��δ�󶨵�socket��Channel
			sc = SocketChannel.open();
			//��Channel����Ϊ������ģʽ
			sc.configureBlocking(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��SocketChannel��������
		try {
			//�������ip�˿ڳɹ�
			if(sc.connect(new InetSocketAddress("127.0.0.1",8191))){
				//��������ע���ע�������ĸ�Channel
				sc.register(sel, SelectionKey.OP_READ);
			}else{
				//���û����ֱ�����ӳɹ�����˵�������û�з���TCP����Ӧ����Ϣ
				//��˽���Channelע�ᵽ�������ϲ��ҹ�עconnect�¼�
				sc.register(sel, SelectionKey.OP_CONNECT);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//������Ӳ��ɹ�ֱ����ֹ����
			System.exit(1);
			e.printStackTrace();
		}
		
		while(!stop){
			
			try {
				//ÿ��1s����һ��selector����ȡ��ʱselector��ȫ������SelectionKey
				sel.select(1000);
				//��ȡ��ǰselector��ȫ��SelectionKey
				Set<SelectionKey> selectionKeys = sel.selectedKeys();
				//��ȡSelectionKeys�ĵ�����
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key = it.next();
					it.remove();
					handleKey(key);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(sel != null){
			try {
				sel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void stop(){
		stop = true;
	}
	
	private static void handleKey(SelectionKey key) throws ClosedChannelException, IOException {
		// TODO Auto-generated method stub
		if(key.isValid()){
			SocketChannel sc = (SocketChannel) key.channel();
			
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(sel, SelectionKey.OP_READ);
				}else{
					System.exit(1);
				}
			}
			
			//���key��Ӧ��ChannelĿǰ������
			if(key.isReadable()){
				ByteBuffer readBuffer = ByteBuffer.allocate(500);
				int readBytes = sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String content = new String(bytes, "UTF-16BE");
					System.out.println(content);
					//stop = true;
				}else if(readBytes<0){
					key.cancel();
					sc.close();
				}else{
					;
				}
			}
			
		}
	}

}
