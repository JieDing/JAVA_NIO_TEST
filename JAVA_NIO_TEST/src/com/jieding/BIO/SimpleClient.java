package com.jieding.BIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("connecting to the server");
		try(Socket s = new Socket();){
			s.connect(new InetSocketAddress("127.0.0.1", 8191),10000);
			//s.setSoTimeout(10000);
			try(Scanner in = new Scanner(s.getInputStream()) ){
				System.out.println("connection built successfully");
				//
				if(in.hasNextLine())
					System.out.println(in.nextLine());
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("连接超时");
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("连接超时");
		}
		
	}

}
