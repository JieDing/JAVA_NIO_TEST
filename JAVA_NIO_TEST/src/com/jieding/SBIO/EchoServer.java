package com.jieding.SBIO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/**
 * 
* @ClassName: EchoServer 
* @author JieDing dingjwilliams@gmail.com
* 
* @Description: 同步阻塞式I/O实现EchoServer BIO
 */
public class EchoServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(8191);
			System.out.println("Server is running");
			Socket incoming = null;
			while(true){
				incoming = serverSocket.accept();
				new Thread(new EchoServerHandler(incoming)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(serverSocket!=null){
				try {
					serverSocket.close();
					serverSocket = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
