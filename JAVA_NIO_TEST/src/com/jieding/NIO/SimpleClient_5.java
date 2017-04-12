package com.jieding.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SimpleClient_5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SocketChannel socketChannel = null;
		Selector sel = null;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			sel = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		try {
			if(socketChannel.connect(new InetSocketAddress("127.0.0.1",8191))){
				socketChannel.register(sel, SelectionKey.OP_READ);
			}else{
				socketChannel.register(sel, SelectionKey.OP_CONNECT);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

}
