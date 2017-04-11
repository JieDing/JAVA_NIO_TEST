package com.jieding.PAIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * @ClassName: SimpleServer.java 
 * @author JieDing dingjwilliams@gmail.com
 *
 * @Description:
 * 
 * PAIO Pseudo Asynchronous IO α�첽IOģ��
 * ��ģ����Ҫ�����̳߳ؼ��������BIOģ���е��߳���Դ���⡣ 
 * �����ģ���У�����˵��߳�����Ŀ�ڿͻ�����Ŀ������1:1
 * ����m:n ����m���̳߳����̵߳����������n�ǿͻ��˵�������n����ԶԶ����m
 * �����������̳߳غ��������ж����б߽�ģ���������ᵼ���̸߳������͵��ڴ������
 * 
 * ��֮���Ա���Ϊα�첽IO������Ϊ�ײ�ͨ����Ȼ��ͬ������BIOģ�ͣ�����ʹ���̳߳ؼ���������߳���Դ����
 * ���롢������������Ⲣû�н��
 * 
 * �̳߳ؼ��������ࣺ
 * ThreadPoolExecutor �̳��� AbstractExecutorService������
 * AbstractExecutorService ʵ���� ExecutorService�ӿ�
 * ExecutorService �̳��� Executor�ӿ�
 * 
 * Executor�ӿ�{
 * 	 void execute(Runnable command);  ����һ����������ִ�п�ִ��Object
 * }
 * 
 * ExecutorService�ӿ�{
 * 	execute()  //��������������̳߳��ύһ�����񣬽����̳߳�ȥִ�С�
 * 	submit()	//�������Ҳ���������̳߳��ύ����ģ���������execute()������ͬ�����ܹ���������ִ�еĽ��
 * 	shutdown()	//����������ֹ�̳߳أ�����Ҫ���������񻺴�����е�����ִ��������ֹ������Ҳ��������µ�����
 * 	shutdownNow()	//������ֹ�̳߳أ������Դ������ִ�е����񣬲���������񻺴���У�������δִ�е�����
 * }
 * 
 * Java�ٷ��Ƽ�����ʹ�ã�
 * Executors.newCachedThreadPool();        //����һ������أ������������СΪInteger.MAX_VALUE
 * Executors.newSingleThreadExecutor();   //��������Ϊ1�Ļ����
 * Executors.newFixedThreadPool(int size);    //�����̶�������СΪsize�Ļ����
 * 
 * 
 */
public class SimpleServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = null;
		try(ServerSocket server = new ServerSocket(8191);) {
			System.out.println("server is running");
			executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			
			while(true){
				Socket incoming = server.accept();
				System.out.println("client address: "+incoming.getRemoteSocketAddress());
				executor.execute(new SimpleServerHandler(incoming));
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
