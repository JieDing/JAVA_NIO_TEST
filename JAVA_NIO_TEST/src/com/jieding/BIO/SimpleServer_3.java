package com.jieding.BIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @ClassName: SimpleServer_3.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:
 * 这是我们的第三个SimpleServer程序，相比于SimpleServer_2来看，我们的服务端程序现在能够
 * 并发地处理多个客户端的请求。那么是不是到这里我们的服务端就写得比较完美了呢？
 * 事实上，现在这个程序仍然有一个问题，那就是每当一个客户端尝试连接我们的服务端程序时，我们的
 * 服务端都会建立一个线程来处理响应地socket的IO操作。
 * 问题是线程在操作系统中是一个宝贵的资源，像这种1:1的服务端线程与客户端socket的IO模型，很容易
 * 在高并发的程序中因为线程创建过多而导致内存溢出。
 * 因此我们必须想出一种方法能够在保持服务端并发处理客户端IO请求的同时，降低大量创建线程的损耗，节省
 * 操作系统的资源。
 * 在下一个程序SimpleServer_4中，我们会将这个程序进行改进，满足这个需求。
 */
public class SimpleServer_3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket incoming = null;
		
		try {
			server = new ServerSocket(8191);
			System.out.println("server is running");
			while(true){
				incoming = server.accept();
				new Thread(new SimpleServer_3_Handler(incoming)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(incoming!=null){
				incoming.close();
				incoming = null;
			}
		}
		
	}

}

class SimpleServer_3_Handler implements Runnable{
	
	private Socket incoming;
	
	
	public SimpleServer_3_Handler(Socket incoming) {
		
		this.incoming = incoming;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try {
			out = new PrintWriter(incoming.getOutputStream(),true);
			System.out.println("wait 10 seconds to send the msg");
			Thread.sleep(10000);
			out.println("this is the message from the server");
			System.out.println("msg sent successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out !=null){
				out.close();
				out = null;
			}
		}
	}
	
}
