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
 * 这是我们第4个SimpleServer程序，与前一个程序相比，这个程序在保留了并行处理多个客户端的能力的同时，
 * 使用线程池技术，改变了服务端线程与客户端数量1:1的模型，转变成了m:n的模型
 * 其中m为服务端线程的最大数量，n为客户端的数量，n可以远大于m。
 * 尽管我们从SimpleServer_1~4服务端程序都在不停地进步，越来越接近真正的并发情况下的服务端，但是我们总是
 * 可以找出一些地方让程序变得更加优秀。
 * 
 * 对于SimpleServer_4而言，它最大的限制在于IO模型是阻塞的。
 * 那么什么叫做阻塞型的IO(blocking IO)呢？这需要从Unix操作系统的五种IO模型说起。
 * 
 * 首先我们需要先知道，IO操作分为两个阶段：
 * 1. 等待数据准备 (Waiting for the data to be ready)
 * 2. 将数据从内核拷贝到进程中 (Copying the data from the kernel to the process)
 * 
 * 第一种 阻塞型IO blocking IO的特点就是在IO操作的这两个阶段，用户进程都在阻塞着。
 * Java InputStream/OutputStream就属于这种阻塞型IO
 * 
 * Java InputStream是为了将数据源的数据输入到进程的内存中去的流对象，但是这个过程其实也分成了两步
 * 内核需要先将数据源的数据准备好，然后再将准备好的数据拷贝到进程内存中去。
 * 那么当我们的in实例调用read方法时，无论此时底层的系统调用处于哪一步，只要还没有完成，那么read方法就不会得到
 * 返回值，而是一直处于挂起状态(阻塞)，直到数据被内核拷贝到了进程内存中去后，read方法才可以获得返回值。
 * 
 * 那么这种IO模型的问题就在于，如果在网络编程中，因为网络问题，而导致IO输入输出较慢，那么读取数据的进程就会
 * 一直被阻塞下去，直到所有的数据都拷贝到内存中去为止。
 * 
 * 在我们的SimpleServer_4程序中，假如每一个线程给客户端作输出需要耗费60s，那么每一个客户端在接收数据时就要阻塞60s
 * 由于我们这里采用的是线程池产生线程处理客户端请求，那么大量的客户端请求会被放入到阻塞队列中去
 * 从而导致大量客户端连接超时。
 * 
 * 那么在SimpleServer_5中，我们将要尝试改变这种传统的阻塞IO模型
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