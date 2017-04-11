package com.jieding.PAIO;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SimpleClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(Socket s = new Socket("127.0.0.1",8191);
			Scanner in = new Scanner(s.getInputStream())) {
			//
			if(in.hasNextLine())
				System.out.println(in.nextLine());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
