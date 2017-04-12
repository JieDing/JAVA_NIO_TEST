package com.jieding.BIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * @ClassName: SimpleServer_4.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:
 * �������ǵ�4��SimpleServer������ǰһ��������ȣ���������ڱ����˲��д������ͻ��˵�������ͬʱ��
 * ʹ���̳߳ؼ������ı��˷�����߳���ͻ�������1:1��ģ�ͣ�ת�����m:n��ģ��
 * ����mΪ������̵߳����������nΪ�ͻ��˵�������n����Զ����m��
 * �������Ǵ�SimpleServer_1~4����˳����ڲ�ͣ�ؽ�����Խ��Խ�ӽ������Ĳ�������µķ���ˣ�������������
 * �����ҳ�һЩ�ط��ó����ø������㡣
 * 
 * ����SimpleServer_4���ԣ���������������IOģ���������ġ�
 * ��ôʲô���������͵�IO(blocking IO)�أ�����Ҫ��Unix����ϵͳ������IOģ��˵��
 * 
 * ����������Ҫ��֪����IO������Ϊ�����׶Σ�
 * 1. �ȴ�����׼�� (Waiting for the data to be ready)
 * 2. �����ݴ��ں˿����������� (Copying the data from the kernel to the process)
 * 
 * ��һ�� ������IO blocking IO���ص������IO�������������׶Σ��û����̶��������š�
 * Java InputStream/OutputStream����������������IO
 * 
 * Java InputStream��Ϊ�˽�����Դ���������뵽���̵��ڴ���ȥ�������󣬵������������ʵҲ�ֳ�������
 * �ں���Ҫ�Ƚ�����Դ������׼���ã�Ȼ���ٽ�׼���õ����ݿ����������ڴ���ȥ��
 * ��ô�����ǵ�inʵ������read����ʱ�����۴�ʱ�ײ��ϵͳ���ô�����һ����ֻҪ��û����ɣ���ôread�����Ͳ���õ�
 * ����ֵ������һֱ���ڹ���״̬(����)��ֱ�����ݱ��ں˿������˽����ڴ���ȥ��read�����ſ��Ի�÷���ֵ��
 * 
 * ��ô����IOģ�͵���������ڣ�������������У���Ϊ�������⣬������IO���������������ô��ȡ���ݵĽ��̾ͻ�
 * һֱ��������ȥ��ֱ�����е����ݶ��������ڴ���ȥΪֹ��
 * 
 * �����ǵ�SimpleServer_4�����У�����ÿһ���̸߳��ͻ����������Ҫ�ķ�60s����ôÿһ���ͻ����ڽ�������ʱ��Ҫ����60s
 * ��������������õ����̳߳ز����̴߳���ͻ���������ô�����Ŀͻ�������ᱻ���뵽����������ȥ
 * �Ӷ����´����ͻ������ӳ�ʱ��
 * 
 * ��ô��SimpleServer_5�У����ǽ�Ҫ���Ըı����ִ�ͳ������IOģ��
 */
public class SimpleServer_4 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket incoming = null;
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		try {
			server = new ServerSocket(8191);
			System.out.println("server is running");
			while(true){
				try {
					incoming = server.accept();
					
					executor.execute(new SimpleServer_4_Handler(incoming));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			if(incoming !=null){
				incoming.close();
				incoming = null;
			}
			if(executor !=null){
				executor.shutdown();
				executor = null;
			}
			if(server !=null){
				server.close();
				server = null;
			}
		}

	}

}

class SimpleServer_4_Handler implements Runnable{
	private Socket incoming;
	
	public SimpleServer_4_Handler(Socket incoming){
		this.incoming  = incoming;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		PrintWriter out = null;
		try {
			out = new PrintWriter(incoming.getOutputStream());
			System.out.println("wait 10 seconds to send the msg");
			Thread.sleep(30000);
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