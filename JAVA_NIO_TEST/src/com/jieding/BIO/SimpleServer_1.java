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
 * ��򵥵�Server����
 * 1. ����ServerSocket������8191�˿�
 * 2. ͨ��server.accept()������ȡ������TCP���ӵ�Socket���˷���������ֱ�����ӽ���
 * 3. ������������һ�仰
 * 4. �ر��������Socket��ServerSocket
 * 
 * ��Ҫע�������������Server�˽�����һ��TCP����֮��͹ر��ˣ���Ȼ����ʵ�ʵķ���˳������ǲ���ʵ�ġ�
 * ������ǿ�ʼ�Դ˳�����иĶ���������Ϊһ���ܽ��ܶ���ͻ������ӵķ���˳��򡪡�SimpleServer_2.java
 */
public class SimpleServer_1 {

	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		Socket incoming = null;
		PrintWriter out = null;
		try {
			//���������׽���,���󶨶˿�
			server = new ServerSocket(8191);
	
			System.out.println("server is running");
	
			//��ȡ��ͻ����׽��ֽ������ӵ��׽���
			incoming = server.accept();
			
			System.out.println(incoming.getRemoteSocketAddress());
			
			//����һ��PrintWriter,�������ǽ�PrintWriter������ַ���ת��Ϊ�ֽ���
			out = new PrintWriter(incoming.getOutputStream(),true);
			//��OutputStreamWriter������ı�����
			out.println("this is the message from the server");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//�ͷ������
			if(out!=null){
				out.close();
				out = null;
			}
			//�ͷ�socket
			if(incoming!=null){
				incoming.close();
				incoming = null;
			}
			//�ͷ�ServerSocket
			if(server!=null){
				server.close();
				server = null;
			}
		}
	}

}
