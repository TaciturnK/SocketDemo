package com.taowd.socketserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author Taowd
 * TODO  SocketͨѶ�����
 * 2017��9��2�� ����12:04:37
 */
public class SocketServer {

	/**
	 * �����ļ��洢·��
	 */
	private static String path;
	/**
	 * �˿ںţ����������ļ�������
	 */
	private static int port;

	static {
		System.out.print("��ȡ�����ļ�...");
		// ��Դ��
		ResourceBundle rb = ResourceBundle.getBundle("config", Locale.getDefault());

		path = rb.getString("path");
		String portStr = rb.getString("port");
		if (path == null || path.trim().equals("")) {
			path = "E:/";
		} else if (!path.endsWith("/")) {
			path = path + "/";
		}
		if (portStr != null || !portStr.trim().equals("")) {
			try {
				port = Integer.parseInt(portStr);
			} catch (Exception ex) {
				System.out.println("�˿ڻ�ȡʧ�ܣ��ѱ��ΪĬ�϶˿�60000");
				port = 60000;
			}
		} else {
			System.out.println("δ���ö˿ڣ��ѱ��ΪĬ�϶˿�60000");
			port = 60000;
		}
		System.out.println("��ȡ���:" + "���ն˿�" + port + ",����·��" + path);
	}

	public static void main(String[] args) {

		new SocketServer().receiveFile();
	}

	/**
	 * 
	 * @author Taowd
	 * TODO �����ļ�����
	 * 2017��9��2�� ����12:04:57
	 */
	@SuppressWarnings("resource")
	public void receiveFile() {
		ServerSocket ss = null;
		Socket s = null;
		BufferedInputStream in = null;
		FileOutputStream out = null;

		try {
			ss = new ServerSocket(port);
			System.out.println("��������������!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("��������ʧ��!");

		}
		// ����ͣЪ������
		while (true) {
			// ע�⣺�˴���Ҫ�Ϳͻ��˱���һ��
			byte[] buffer = new byte[1024];
			String fileName = null;

			// ��ȡͨѶ��־
			byte[] flagByte = new byte[10];
			String Flag = "";

			try {
				s = ss.accept();
				in = new BufferedInputStream(s.getInputStream());
				// ��ȡͨѶ��־
				in.read(flagByte, 0, flagByte.length);
				Flag = new String(flagByte).trim();

				System.out.println("��ȡ��ͨѶ��־��" + Flag);

				// 1-�����ϴ� 2-�������� 0-�������
				switch (Flag) {
				case "0": {
					System.out.println("�������˲����߼�********");

					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("���յ�������Ϣ��" + fileName);

					// ����Ƿ�Ϊ�������ݣ�
					if (fileName.equals("test message")) {
						System.out.println("���ܵ��ͻ��˷��͵���Ϣ��ͨѶ�ɹ�!");

						// ��������
						OutputStream os = s.getOutputStream();
						PrintWriter pw = new PrintWriter(os);

						pw.write("server Test");
						pw.flush();

						os.close();
						pw.close();

					}

				}
					break;
				case "1": {
					System.out.println("�ϴ��߼�**********");
					// ȡ���ļ���
					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("���յ��ļ����ƣ�" + fileName);

					// �����ļ���
					out = new FileOutputStream(
							new File(path + new SimpleDateFormat("yyyymmddhhmmss").format(new Date()) + fileName));
					out.flush();
					buffer = new byte[1024];
					// д�ļ�����
					int count = 0;
					while ((count = in.read(buffer, 0, buffer.length)) > 0) {
						out.write(buffer, 0, count);
						out.flush();
						buffer = new byte[1024];
					}

				}
					break;
				case "2": {
					System.out.println("�����߼�");

				}
					break;

				default:
					break;
				}

			} catch (Exception ex) {
				System.out.println("�����쳣");

				ex.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
					if (s != null) {
						s.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("�����ļ�" + fileName + "ʧ��" + ex);
				}
			}
		}
	}
}
