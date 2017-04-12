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
 * �������ǵĵ�����SimpleServer���������SimpleServer_2���������ǵķ���˳��������ܹ�
 * �����ش������ͻ��˵�������ô�ǲ��ǵ��������ǵķ���˾�д�ñȽ��������أ�
 * ��ʵ�ϣ��������������Ȼ��һ�����⣬�Ǿ���ÿ��һ���ͻ��˳����������ǵķ���˳���ʱ�����ǵ�
 * ����˶��Ὠ��һ���߳���������Ӧ��socket��IO������
 * �������߳��ڲ���ϵͳ����һ���������Դ��������1:1�ķ�����߳���ͻ���socket��IOģ�ͣ�������
 * �ڸ߲����ĳ�������Ϊ�̴߳�������������ڴ������
 * ������Ǳ������һ�ַ����ܹ��ڱ��ַ���˲�������ͻ���IO�����ͬʱ�����ʹ��������̵߳���ģ���ʡ
 * ����ϵͳ����Դ��
 * ����һ������SimpleServer_4�У����ǻὫ���������иĽ��������������
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
