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
 * �������ǵĵڶ���Server����
 * ��Ƚ���SimpleServer_1����������������Ĳ�ͬ�������������߳��������while(true)ѭ��
 * ��ˣ����ǵ�ServerSocket�ܹ����ϵؽ��ܽ��������ӵ�Socket
 * �����������������Ƿ���˵��̴߳��У�������Ǳ��봦����һ��socket��IO��Ϊ֮�󣬲����ٽ����µ�incoming socket
 * Ȼ���ٴ����µ�IO��Ϊ��
 * ��������д��writeData����������Ϊ��ģ��IO��Ϊ��ʱ����Ҫ����һ����ʱ�䣬�п�����Ϊ�����ӳٵȵȡ�
 * ��ô����������100���ͻ��˳����������ǵķ�����أ�
 * ��һ�����˵��û�ֻ��Ҫ�ȴ�10S���IO�������Ϳ��Ի�ȡ������
 * �ڶ����û���Ҫ�ȵȵ�һ���û�10S�������IO������Ȼ���ٵ�10S����Լ���IO�������Ϳ��Ի�ȡ������
 * �������û���Ҫ�ȵ�ǰ�����û�20S������ǵĲ�����Ȼ���ٵ�10S����Լ���IO�������ſ��Ի�ȡ������
 * ...
 * �Դ�����
 * ��һ�ٸ��û���Ҫ��1000S�ſ��Ի�ȡ�����ݡ�����
 * �����ķ���˳������пͻ��˸�ʹ���أ�
 * ���������Ҫ��취�ö���ͻ����ܹ����е����IO��������ȡ�Լ������ݣ������ܴ��еس��������ȴ���
 * 
 * ����һ������SimpleServer_3�У����ǻ�ʵ�ַ���˲��еؽ��ܶ���ͻ��˵�����
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
