package com.jieding.SNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Thread(new SimpleClientHandle()).start();
	}

}

class SimpleClientHandle implements Runnable{
	
	private Selector selector;
	
	private SocketChannel socketChannel;
	
	private volatile boolean stop;
	
	public SimpleClientHandle(){
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(socketChannel.connect(new InetSocketAddress("127.0.0.1",8191))){
				socketChannel.register(selector, SelectionKey.OP_READ);
			}else{
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
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
					}catch(Exception e){
						if(key !=null){
							key.cancel();
							if(key.channel() !=null){
								key.channel().close();
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void handleInput(SelectionKey key) throws IOException{
		if(key.isValid()){
			SocketChannel sc = (SocketChannel) key.channel();
			if(key.isConnectable()){
				if(sc.finishConnect()){
					sc.register(selector, SelectionKey.OP_READ);
				}else{
					System.exit(1);
				}
			}
			if(key.isReadable()){

				ByteBuffer readBuffer = ByteBuffer.allocate(100);
				int readBytes = sc.read(readBuffer);
				if(readBytes>0){
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String content = new String(bytes, "UTF-16BE");
					System.out.println(content);
					//this.stop = true;
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
