package com.jieding.SBIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(Socket s = new Socket();) {
			s.connect(new InetSocketAddress("127.0.0.1", 8191));
			try(PrintWriter out = new PrintWriter(s.getOutputStream(),true);
				Scanner in  = new Scanner(s.getInputStream());
				BufferedReader buff = new BufferedReader(new InputStreamReader(System.in))){
				if(in.hasNextLine())
					System.out.println(in.nextLine());
				
				out.println(buff.readLine());
				out.flush();
				while(in.hasNextLine()){
					String line = in.nextLine();
					System.out.println(line);
					String output = buff.readLine();
					out.println(output);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
