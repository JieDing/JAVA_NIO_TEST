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
 * @Description:�������������߳���Դ����
 * 
 * BIO Blocked IO ģ�� ͬ������ģ��
 * 
 * ������߳�����ͻ�����ĿΪ1:1�Ĺ�ϵ�����ڴ����ͻ��˷��ʵĲ���ģ�������Բ���ȡ��
 * ��Σ��������롢�������������Ҳ��û�н���������Ϊ������ʱ����������������Ҫ60S
 * ��ôÿ���ͻ��˶���Ҫ�ȴ�60S�����ڴ��ģ�ͻ���������¼�ֱ�������ѡ�
 */
public class SimpleServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(ServerSocket server = new ServerSocket(8191);) {
			System.out.println("server is running");
			while(true){
				//����socket���ӽ���֮ǰ��������accept()������
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