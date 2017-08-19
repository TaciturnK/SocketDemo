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
 * ���ܣ�SocketͨѶ�ͻ���
 * @author Taowd
 *
 */
public class SocketClient {

	public static void main(String[] args) {

		try {
			// 1������Socketʵ���������˷�����Ϣ
			Socket socket = new Socket("127.0.0.1", 8888);
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("�����ǿͻ��ˣ���ð���");
			pw.flush();
			socket.shutdownOutput();

			// ��ȡһ�������������տͻ��˷��͵�����
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				System.out.println("�����ǿͻ��ˣ����շ������ϢΪ��" + temp);

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
