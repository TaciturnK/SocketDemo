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
 * TODO  Socket通讯服务端
 * 2017年9月2日 下午12:04:37
 */
public class SocketServer {

	/**
	 * 接收文件存储路径
	 */
	private static String path;
	/**
	 * 端口号，可在配置文件中配置
	 */
	private static int port;

	static {
		System.out.print("读取配置文件...");
		// 资源绑定
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
				System.out.println("端口获取失败，已变更为默认端口60000");
				port = 60000;
			}
		} else {
			System.out.println("未配置端口，已变更为默认端口60000");
			port = 60000;
		}
		System.out.println("读取完成:" + "接收端口" + port + ",接收路径" + path);
	}

	public static void main(String[] args) {

		new SocketServer().receiveFile();
	}

	/**
	 * 
	 * @author Taowd
	 * TODO 接收文件操作
	 * 2017年9月2日 下午12:04:57
	 */
	@SuppressWarnings("resource")
	public void receiveFile() {
		ServerSocket ss = null;
		Socket s = null;
		BufferedInputStream in = null;
		FileOutputStream out = null;

		try {
			ss = new ServerSocket(port);
			System.out.println("服务已正常启动!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("服务启动失败!");

		}
		// 永不停歇地运行
		while (true) {
			// 注意：此处需要和客户端保持一致
			byte[] buffer = new byte[1024];
			String fileName = null;

			// 获取通讯标志
			byte[] flagByte = new byte[10];
			String Flag = "";

			try {
				s = ss.accept();
				in = new BufferedInputStream(s.getInputStream());
				// 先取通讯标志
				in.read(flagByte, 0, flagByte.length);
				Flag = new String(flagByte).trim();

				System.out.println("获取到通讯标志：" + Flag);

				// 1-代表上传 2-代表下载 0-代表测试
				switch (Flag) {
				case "0": {
					System.out.println("进入服务端测试逻辑********");

					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("接收到测试信息：" + fileName);

					// 检查是否为测试数据：
					if (fileName.equals("test message")) {
						System.out.println("接受到客户端发送的消息，通讯成功!");

						// 获得输出流
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
					System.out.println("上传逻辑**********");
					// 取得文件名
					in.read(buffer, 0, buffer.length);
					fileName = new String(buffer).trim();
					System.out.println("接收到文件名称：" + fileName);

					// 定义文件流
					out = new FileOutputStream(
							new File(path + new SimpleDateFormat("yyyymmddhhmmss").format(new Date()) + fileName));
					out.flush();
					buffer = new byte[1024];
					// 写文件内容
					int count = 0;
					while ((count = in.read(buffer, 0, buffer.length)) > 0) {
						out.write(buffer, 0, count);
						out.flush();
						buffer = new byte[1024];
					}

				}
					break;
				case "2": {
					System.out.println("下载逻辑");

				}
					break;

				default:
					break;
				}

			} catch (Exception ex) {
				System.out.println("传输异常");

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
					System.out.println("接收文件" + fileName + "失败" + ex);
				}
			}
		}
	}
}
