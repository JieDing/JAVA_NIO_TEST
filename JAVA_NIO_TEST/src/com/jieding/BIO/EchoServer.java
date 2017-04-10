package com.jieding.BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (ServerSocket serverSocket = new ServerSocket(8191);) {
			System.out.println("server is running");
			
			while(true){
				try(Socket incoming = serverSocket.accept();){
					
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
