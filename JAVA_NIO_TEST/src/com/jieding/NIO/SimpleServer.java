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

public class SimpleServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread(new ReactorThread(),"NIO-Server").start();
	}

}

class ReactorThread implements Runnable{
	
	//serverSocket���ܵ�
	private  ServerSocketChannel serverChannel;
	
	//��·������
	private Selector selector;
	
	private volatile boolean stop;
	
	public ReactorThread(){
		try {
			serverChannel =  ServerSocketChannel.open();
			selector = Selector.open();
			
			serverChannel.configureBlocking(false);
			serverChannel.bind(new InetSocketAddress(8191));
			/**
			 * ע��register()�����ĵڶ�������������һ����interest���ϡ���
			 * ��˼����ͨ��Selector����Channelʱ��ʲô�¼�����Ȥ�����Լ������ֲ�ͬ���͵��¼���
			 * 1.Connect
			 * 2.Accept
			 * 3.Read
			 * 4.Write
			 * ͨ��������һ���¼���˼�Ǹ��¼��Ѿ�������
			 * ���ԣ�ĳ��channel�ɹ����ӵ���һ����������Ϊ�����Ӿ�������
			 * һ��server socket channel׼���ý����½�������ӳ�Ϊ�����վ�������
			 * һ�������ݿɶ���ͨ������˵�ǡ�����������
			 * �ȴ�д���ݵ�ͨ������˵�ǡ�д��������
			 */
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			System.out.println("the server is running");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
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
				selector.select(1000);
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				
				while(it.hasNext()){
					key = it.next();
					it.remove();
					try{
						handleInput(key);
					}catch (Exception e){
						if(key!=null){
							key.cancel();
							if(key.channel() !=null)
								key.channel().close();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(selector!=null){
			try{
				selector.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			if(key.isAcceptable()){
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				
				sc.register(selector, SelectionKey.OP_WRITE);
				
			}
			if(key.isWritable()){
				SocketChannel sc = (SocketChannel) key.channel();
				String content ="hello this is the message from SimpleServer";
				byte[] bytes = content.getBytes("UTF-16BE");
				ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
				buffer.put(bytes);
				buffer.flip();
				
				sc.write(buffer);
				
				key.cancel();
				sc.close();
				
			}
		}
	}
	
}
