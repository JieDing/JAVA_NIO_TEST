package com.jieding.PAIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EchoServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(ServerSocket server = new ServerSocket(8191)){
			System.out.println("server is running");
			EchoServerThreadPool singleExecutor = new EchoServerThreadPool(4, 1000);
			Socket socket = null;
			while(true){
				socket = server.accept();
				singleExecutor.execute(new EchoServerHandler(socket));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class EchoServerHandler implements Runnable{
	private Socket incoming;
	public EchoServerHandler(Socket incoming){
		this.incoming = incoming;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try(PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);
			Scanner in = new Scanner(incoming.getInputStream());) {
			out.println("This is EchoServer, enter BYE to exit");
			
			boolean done = false;
			while(!done && in.hasNextLine()){
				String line = in.nextLine();
				if(line.trim().equals("BYE")){
					done = true;
					break;
				}
				out.println("FROM EchoServer: "+line);
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}


class EchoServerThreadPool{
	private ExecutorService executor;
	public EchoServerThreadPool(int maxPoolSize, int queueSize){

		executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
	}
	
	public void execute(Runnable task){
		executor.execute(task);
	}
}
