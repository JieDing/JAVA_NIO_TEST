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
	
	//serverSocket父管道
	private  ServerSocketChannel serverChannel;
	
	//多路复用器
	private Selector selector;
	
	private volatile boolean stop;
	
	public ReactorThread(){
		try {
			serverChannel =  ServerSocketChannel.open();
			selector = Selector.open();
			
			serverChannel.configureBlocking(false);
			serverChannel.bind(new InetSocketAddress(8191));
			/**
			 * 注意register()方法的第二个参数。这是一个“interest集合”，
			 * 意思是在通过Selector监听Channel时对什么事件感兴趣。可以监听四种不同类型的事件：
			 * 1.Connect
			 * 2.Accept
			 * 3.Read
			 * 4.Write
			 * 通道触发了一个事件意思是该事件已经就绪。
			 * 所以，某个channel成功连接到另一个服务器称为“连接就绪”。
			 * 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
			 * 一个有数据可读的通道可以说是“读就绪”。
			 * 等待写数据的通道可以说是“写就绪”。
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
