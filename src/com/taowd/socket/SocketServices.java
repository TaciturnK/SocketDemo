package com.taowd.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 功能：Socket服务端
 * @author Taowd
 *
 */
public class SocketServices {

	public static void main(String[] args) {

		try {
			// 1、创建ServiceSocket，开启监听端口8888
			ServerSocket serviceSocket = new ServerSocket(8888);

			System.out.println("******服务端启动，正在监听8888端口******");
			Socket socket = serviceSocket.accept();// 开启监听

			// 获取一个输入流，接收客户端发送的数据
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				System.out.println("我是服务端，客户端说：" + temp);

			}
			socket.shutdownInput();

			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("这里是服务端发送的信息，我很好！");
			pw.flush();
			socket.shutdownOutput();

			os.close();
			pw.close();
			br.close();
			isr.close();
			is.close();
			serviceSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

}
