package com.taowd.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 功能：Socket通讯客户端
 * @author Taowd
 *
 */
public class SocketClient {

	public static void main(String[] args) {

		try {
			// 1、创建Socket实例，向服务端发送信息
			Socket socket = new Socket("127.0.0.1", 8888);
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("这里是客户端，你好啊！");
			pw.flush();
			socket.shutdownOutput();

			// 获取一个输入流，接收客户端发送的数据
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				System.out.println("这里是客户端，接收服务端信息为：" + temp);

			}
			socket.shutdownInput();

			br.close();
			isr.close();
			is.close();
			os.close();
			pw.close();

			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
