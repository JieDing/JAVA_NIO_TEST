package com.jieding.BIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @ClassName: SimpleServer_1.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:
 * 最简单的Server程序
 * 1. 建立ServerSocket并监听8191端口
 * 2. 通过server.accept()方法获取建立了TCP连接的Socket，此方法会阻塞直到连接建立
 * 3. 向输出流中输出一句话
 * 4. 关闭输出流、Socket和ServerSocket
 * 
 * 需要注意的是这个程序的Server端接受了一次TCP连接之后就关闭了，显然这在实际的服务端程序中是不现实的。
 * 因此我们开始对此程序进行改动，将它变为一个能接受多个客户端连接的服务端程序——SimpleServer_2.java
 */
public class SimpleServer_1 {

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		Socket incoming = null;
		PrintWriter out = null;
		try {
			//建立监听套接字,并绑定端口
			server = new ServerSocket(8191);
	
			System.out.println("server is running");
	
			//获取与客户端套接字建立连接的套接字
			incoming = server.accept();
			
			System.out.println(incoming.getRemoteSocketAddress());
			
			//建立一个PrintWriter,允许我们将PrintWriter输出的字符流转换为字节流
			out = new PrintWriter(incoming.getOutputStream(),true);
			//向OutputStreamWriter中输出文本数据
			out.println("this is the message from the server");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//释放输出流
			if(out!=null){
				out.close();
				out = null;
			}
			//释放socket
			if(incoming!=null){
				incoming.close();
				incoming = null;
			}
			//释放ServerSocket
			if(server!=null){
				server.close();
				server = null;
			}
		}
	}

}
