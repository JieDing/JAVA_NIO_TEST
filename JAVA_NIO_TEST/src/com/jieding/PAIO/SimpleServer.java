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
 * PAIO Pseudo Asynchronous IO 伪异步IO模型
 * 该模型主要利用线程池技术解决了BIO模型中的线程资源问题。 
 * 在这个模型中，服务端的线程数数目于客户端数目不再是1:1
 * 而是m:n 其中m是线程池中线程的最大数量，n是客户端的数量，n可以远远大于m
 * 由于设置了线程池和阻塞队列都是有边界的，因此它不会导致线程个数膨胀到内存溢出。
 * 
 * 它之所以被称为伪异步IO，是因为底层通信仍然是同步阻塞BIO模型，仅仅使用线程池技术解决了线程资源问题
 * 输入、输出流阻塞问题并没有解决
 * 
 * 线程池技术核心类：
 * ThreadPoolExecutor 继承了 AbstractExecutorService抽象类
 * AbstractExecutorService 实现了 ExecutorService接口
 * ExecutorService 继承了 Executor接口
 * 
 * Executor接口{
 * 	 void execute(Runnable command);  仅有一个方法用来执行可执行Object
 * }
 * 
 * ExecutorService接口{
 * 	execute()  //这个方法可以向线程池提交一个任务，交由线程池去执行。
 * 	submit()	//这个方法也是用来向线程池提交任务的，但是它和execute()方法不同，它能够返回任务执行的结果
 * 	shutdown()	//不会立即终止线程池，而是要等所有任务缓存队列中的任务都执行完后才终止，但再也不会接受新的任务
 * 	shutdownNow()	//立即终止线程池，并尝试打断正在执行的任务，并且清空任务缓存队列，返回尚未执行的任务
 * }
 * 
 * Java官方推荐我们使用：
 * Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
 * Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
 * Executors.newFixedThreadPool(int size);    //创建固定容量大小为size的缓冲池
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
