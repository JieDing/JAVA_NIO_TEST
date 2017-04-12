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
		// TODO Auto-generated method stub
		ServerSocketChannel ssc = null;
		try {
			ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(8191),1024);
			ssc.configureBlocking(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ssc = null;
		}
	}

}

class ReactorThread implements Runnable{
	private Selector sel;
	private ServerSocketChannel serverChannel;
	private boolean stop;
	
	public ReactorThread(ServerSocketChannel serverChannel){
		this.serverChannel = serverChannel;
		try {
			sel = Selector.open();
			this.serverChannel.register(sel, SelectionKey.OP_ACCEPT);
			System.out.println("server is running");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				//������������ÿ��1s�������Ƿ�������������Channel��selector������ѵһ��
				sel.select(1000);
				Set<SelectionKey> selectionKeys = sel.selectedKeys();
				Iterator<SelectionKey> it = selectionKeys.iterator();
				SelectionKey key = null;
				while(it.hasNext()){
					key  = it.next();
					//SelectionKey�����Զ��ӵ��������Ƴ��������������Ҫ�ֶ��Ƴ�
					it.remove();
					handleValidKey(key);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(sel !=null){
			try {
				sel.close();
				sel = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(serverChannel !=null){
			try {
				serverChannel.close();
				serverChannel = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void handleValidKey(SelectionKey key) {
		// TODO Auto-generated method stub
		if(key.isValid()){
			if(key.isAcceptable()){
				//���ܵ��µ�����
				try {
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					System.out.println("����һ������");
					SocketChannel sc = ssc.accept();
					sc.configureBlocking(false);
					sc.register(sel, SelectionKey.OP_WRITE);
					key.cancel();
					sc.close();
					ssc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(key.isWritable()){
				try {
					SocketChannel sc = (SocketChannel) key.channel();
					sc.configureBlocking(false);
					ByteBuffer buffer = ByteBuffer.allocate(500);
					String contents = "this is the message from the server";
					System.out.println("contents size: "+contents.length()*2);
					byte[] bytes = contents.getBytes("UTF-16BE");
					System.out.println("bytes length: "+bytes.length);
					buffer.flip();
					while(buffer.hasRemaining())
						sc.write(buffer);
					
					key.cancel();
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
