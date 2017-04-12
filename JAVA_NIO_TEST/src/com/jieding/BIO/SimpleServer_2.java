package com.jieding.BIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @ClassName: SimpleServer_2.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:
 * 这是我们的第二个Server程序
 * 相比较于SimpleServer_1来看，这个程序最大的不同在于我们在主线程中添加了while(true)循环
 * 因此，我们的ServerSocket能够不断地接受建立了连接的Socket
 * 但是由于我们这里是服务端单线程串行，因此我们必须处理完一个socket的IO行为之后，才能再接受新的incoming socket
 * 然后再处理新的IO行为。
 * 在这里我写的writeData方法，就是为了模拟IO行为有时候需要花费一定的时间，有可能因为网络延迟等等。
 * 那么假如我们有100个客户端尝试连接我们的服务端呢？
 * 第一个幸运的用户只需要等待10S完成IO操作，就可以获取到数据
 * 第二个用户需要先等第一个用户10S完成它的IO操作，然后再等10S完成自己的IO操作，就可以获取到数据
 * 第三个用户需要先等前两个用户20S完成他们的操作，然后再等10S完成自己的IO操作，才可以获取到数据
 * ...
 * 以此类推
 * 第一百个用户需要等1000S才可以获取到数据。。。
 * 这样的服务端程序哪有客户端敢使用呢？
 * 因此我们需要想办法让多个客户端能够并行地完成IO操作，获取自己的数据，而不能串行地持续阻塞等待。
 * 
 * 在下一个程序SimpleServer_3中，我们会实现服务端并行地接受多个客户端的请求。
 */
public class SimpleServer_2 {
	
	static PrintWriter out = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket incoming = null;
		
		
		try {
			server = new ServerSocket(8191);
			System.out.println("server is running");
			
			while(true){
				incoming = server.accept();
				writeData(incoming);	
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(incoming!=null){
				incoming.close();
				incoming = null;
			}
			if(server !=null){
				server.close();
				server = null;
			}
		}
	}
	
	private static void writeData(Socket incoming){
		try {
			out = new PrintWriter(incoming.getOutputStream(),true);
			System.out.println("wait 10 seconds to send the msg");
			Thread.sleep(50000);
			out.println("this is the message from the server");
			System.out.println("msg sent successfully");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
				out =null;
			}
		}
	}

}
