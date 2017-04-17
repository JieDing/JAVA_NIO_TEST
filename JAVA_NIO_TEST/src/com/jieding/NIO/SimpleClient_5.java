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
			//初始化多路复用器selector
			sel = Selector.open();
			//获取一个未绑定的socket的Channel
			sc = SocketChannel.open();
			//将Channel设置为非阻塞模式
			sc.configureBlocking(false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//对SocketChannel进行连接
		try {
			//如果连接ip端口成功
			if(sc.connect(new InetSocketAddress("127.0.0.1",8191))){
				//则向复用器注册关注读就绪的该Channel
				sc.register(sel, SelectionKey.OP_READ);
			}else{
				//如果没有最直接连接成功，则说明服务端没有返回TCP握手应答消息
				//因此将该Channel注册到复用器上并且关注connect事件
				sc.register(sel, SelectionKey.OP_CONNECT);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//如果连接不成功直接终止程序
			System.exit(1);
			e.printStackTrace();
		}
		
		while(!stop){
			
			try {
				//每隔1s唤醒一次selector，获取当时selector的全部就绪SelectionKey
				sel.select(1000);
				//获取当前selector的全部SelectionKey
				Set<SelectionKey> selectionKeys = sel.selectedKeys();
				//获取SelectionKeys的迭代器
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
			
			//如果key对应的Channel目前读就绪
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
