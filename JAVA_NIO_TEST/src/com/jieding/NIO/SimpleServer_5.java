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
			//通过ServerSocketChannel.open()来生成一个没有绑定的服务套结字通道
			ssc = ServerSocketChannel.open();  
			//ssc.socket()获取与通道绑定的ServerSocket，然后在将该ServerSocket绑定指定的端口
			ssc.socket().bind(new InetSocketAddress(8191));
			//设置通道为非阻塞，才能够将该通道注册到多路复用器上去
			ssc.configureBlocking(false);
			
			new Thread(new ReactorThread(ssc)).start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//如果ServerSocketChannel未初始化成功，或者ServerSocket绑定的端口被占用
			//则程序退出
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
			//初始化一个新的多路复用器
			sel = Selector.open();
			//将ServerSocketChannel注册到selector上去，因此Selector在监听该通道时，对该通道处于
			//接受就绪事件感兴趣,一旦将Channel注册到复用器上，selector会持有一个该Channel的SelectionKey
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			System.out.println("Server is running");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//如果初始化Selector失败则程序退出
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
				//每隔1s唤醒selector1次，返回在selector中感兴趣事件就绪的通道
				sel.select(1000);
				//获取当前所有准备就绪的SelectionKey
				Set<SelectionKey> selectionKeys = sel.selectedKeys();
				//获得它的迭代器
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
			//关闭多路复用器，则它上面注册的Channel都会被自动关闭，因此无需重复关闭
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
			//如果该key对应的Channel准备好接受新的socket连接
			if(key.isAcceptable()){
				//取出key所对应的Channel，此时该Channel应该为ServerSocketChannel
				ServerSocketChannel ssh = (ServerSocketChannel) key.channel();
				//获得连接到该ServerSocket上的socket的Channel
				SocketChannel sc = ssh.accept();
							
				//将该SocketChannel设置为非阻塞
				sc.configureBlocking(false);
				//我们关心incomingSocket是否能够向客户端socket输出数据
				sc.register(sel, SelectionKey.OP_WRITE);
				
			}
		}
		//如果该key对应的Channel写就绪
		if(key.isWritable()){
			//此时sc为incoming socket 所对应的Channel
			SocketChannel sc = (SocketChannel) key.channel();
			String content = "this is the message from the server\n";
			String content2 = "hahaha\n";
			byte[] bytes = content.getBytes("UTF-16BE");
			byte[] bytes2 = content2.getBytes("UTF-16BE");
			//创建缓冲区，并分配大小为输出字符串字节长度
			ByteBuffer buff = ByteBuffer.allocate(bytes.length+bytes2.length);
			System.out.println("输出长度："+bytes.length+bytes2.length);
			buff.put(bytes);
			buff.put(bytes2);
			//将缓冲区切换为读模式，等待Channel将缓冲区中的字节写入到通道中去
			buff.flip();
			
			sc.write(buff);
			//将Key从集合中删除掉
			key.cancel();
			sc.close();
		}
	}

}
