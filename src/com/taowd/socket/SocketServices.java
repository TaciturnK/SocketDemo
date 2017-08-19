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
 * ���ܣ�Socket�����
 * @author Taowd
 *
 */
public class SocketServices {

	public static void main(String[] args) {

		try {
			// 1������ServiceSocket�����������˿�8888
			ServerSocket serviceSocket = new ServerSocket(8888);

			System.out.println("******��������������ڼ���8888�˿�******");
			Socket socket = serviceSocket.accept();// ��������

			// ��ȡһ�������������տͻ��˷��͵�����
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				System.out.println("���Ƿ���ˣ��ͻ���˵��" + temp);

			}
			socket.shutdownInput();

			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("�����Ƿ���˷��͵���Ϣ���Һܺã�");
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
