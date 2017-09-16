package com.taowd.socketserver;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
	/**
	 * �����ļ�·��
	 */
	private static String downLoad;

	static {
		System.out.println("��ȡ�����ļ�...");
		// ��Դ��
		ResourceBundle rb = ResourceBundle.getBundle("config", Locale.getDefault());

		path = rb.getString("path");
		String portStr = rb.getString("port");
		downLoad = rb.getString("dowmload").toString();

		if (path == null || path.trim().equals("")) {
			path = "E:/";
		} else if (!path.endsWith("/")) {
			path = path + "/";
		}

		if (downLoad == null || downLoad.trim().equals("")) {
			downLoad = "E:/";
		} else if (!downLoad.endsWith("/")) {
			downLoad = downLoad + "/";
		}

		File file = new File(downLoad);
		SocketServer.judeDirExists(file);

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
		System.out.println("��ȡ���:" + "���ն˿�" + port + ",�ϴ��ļ��洢·��" + path + ",�����ļ���ȡ·��" + downLoad);
	}

	public static void main(String[] args) {

		new SocketServer().receiveFile();
	}

	// �ж��ļ����Ƿ����
	public static void judeDirExists(File file) {

		if (file.exists()) {
			if (file.isDirectory()) {
				// System.out.println("dir exists");
			} else {
				System.out.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("����·�������ڣ����ڴ���...");
			file.mkdir();
		}

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
		FileInputStream fins = null;
		DataOutputStream dout = null;
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

					// ��һ�������տͻ��˷��͵Ĳ�������
					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("���յ�������Ϣ��" + fileName);

					// �ڶ����� ����Ƿ�Ϊ�������ݣ�
					if (fileName.equals("test message")) {
						System.out.println("���ܵ��ͻ��˷��͵���Ϣ��ͨѶ�ɹ�!");

						// ����������ͻ��˷��ͷ���˵Ĳ�������
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
					// ��һ���� ȡ���ļ���
					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("���յ��ļ����ƣ�" + fileName);

					// �ڶ����� �����ļ���(׼���洢�ļ�)
					out = new FileOutputStream(
							new File(path + new SimpleDateFormat("yyyymmddhhmmss").format(new Date()) + fileName));
					out.flush();
					buffer = new byte[1024];

					// ����������ȡ�ϴ����ļ�����Ϣ�� �����ļ�����
					System.out.println("���յ��ļ��Ĵ�С��" + in.available());
					int count = 0;
					while ((count = in.read(buffer, 0, buffer.length)) > 0) {
						out.write(buffer, 0, count);
						out.flush();
						buffer = new byte[1024];
					}

					// ���Ĳ������ͻ��˷�����Ϣ������ɹ�
					// ��������
					// System.out.println("������ݽ��գ�׼�����ͷ��ص�������Ϣ");
					// PrintWriter pw = new PrintWriter(s.getOutputStream());
					// pw.println("SUCCESS");
					// pw.flush();
					// pw.close();

				}
					break;
				case "2": {
					System.out.println("�����߼�");
					// ��һ���� ȡ������·��
					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("�����ļ�Ϊ��" + fileName);

					// ��������·��
					fileName = downLoad + "/" + fileName;

					System.out.println("���յ�����·��Ϊ��" + fileName);

					dout = new DataOutputStream(s.getOutputStream());
					System.out.println("��ʼ�����ļ�----" + new Date().toString());
					// �ڶ����� ��������·����ȡ�ļ���
					if (new File(fileName).exists()) {// ����ļ��Ƿ����
						fins = new FileInputStream(new File(fileName));
						int length = 0;
						byte[] sendByte = new byte[1024];
						while ((length = fins.read(sendByte, 0, sendByte.length)) > 0) {
							dout.write(sendByte, 0, length);
							dout.flush();
						}
						buffer = new byte[1024];
					}
					System.out.println("�����ļ�����----" + new Date().toString());

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
					if (dout != null) {
						dout.close();
					}
					if (fins != null) {
						fins.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("�����ļ�" + fileName + "ʧ��" + ex);
				}
			}
		}
	}
}
