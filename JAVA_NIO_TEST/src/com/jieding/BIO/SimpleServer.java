package com.jieding.BIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * @ClassName: SimpleServer.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:最大的问题在于线程资源问题
 * 
 * BIO Blocked IO 模型 同步阻塞模型
 * 
 * 服务端线程数与客户端数目为1:1的关系，这在大量客户端访问的并发模型中明显不可取；
 * 其次，它的输入、输出流阻塞问题也并没有解决，如果因为网络延时，服务端输出数据需要60S
 * 那么每个客户端都需要等待60S。这在大规模客户访问情况下简直就是灾难。
 */
public class SimpleServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(ServerSocket server = new ServerSocket(8191);) {
			System.out.println("server is running");
			while(true){
				//在有socket连接进来之前，阻塞在accept()方法上
				Socket incoming = server.accept();
				System.out.println("client address: "+incoming.getRemoteSocketAddress());
				new Thread(new SimpleServerHandler(incoming)).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class SimpleServerHandler implements Runnable{
	private Socket incoming;
	
	public SimpleServerHandler(Socket incoming){
		this.incoming = incoming;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try(PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);) {
			System.out.println("wait 10 seconds to send message");
			Thread.sleep(10000);
			out.println("hello this is the message from SimpleServer");
			System.out.println("message sent!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}