using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.IO;
using System.Windows;
using System.Runtime.InteropServices;
using System.Net;

namespace Socket通讯
{
    /// <summary>
    /// 功能：客户端Socket传送文件工具类
    /// </summary>
    class FileUtils
    {
        /// <summary>
        ///IP地址
        /// </summary>
        public static string Ip = "127.0.0.1";
        /// <summary>
        /// 端口号
        /// </summary>
        public static int Port = 60000;

        /// <summary>
        /// 发送文件 
        /// </summary>
        /// <param name="flag"> 1-代表上传   2-代表下载  0-代表测试</param>
        /// <param name="path">文件全路径</param>
        /// <param name="fileName">文件名</param>
        /// <returns>返回执行结果0成功，-1文件不存在，-2连接失败，-3IO异常，-4未知异常</returns>
        public static int StartUpload(String flag, String path, String fileName, out string receiveInfo)
        {
            if (!File.Exists(path))
            {
                receiveInfo = "上传文件不存在";
                return -1;
            }
            NetworkStream stream = null;

            BinaryWriter sw = null;
            FileStream fsMyfile = null;
            BinaryReader brMyfile = null;
            BinaryReader br = null;
            BinaryWriter bw = null;

            try
            {
                // IP地址.
                IPAddress localAddr = IPAddress.Parse(Ip);
                // 接入点.
                IPEndPoint ephost = new IPEndPoint(localAddr, Port);
                Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                //尝试连接主机
                socket.Connect(ephost);

                //第一步：写入通讯标志(用来区分是下载还是上传文件)  1-代表上传   2-代表下载  0-代表测试
                byte[] flagBytes = Encoding.Default.GetBytes(flag);
                byte[] flagBytesArray = new byte[flagBytes.Length];
                Array.Copy(flagBytes, flagBytesArray, flagBytes.Length);
                //socket.Send(flagBytesArray, 0, flagBytesArray.Length, SocketFlags.None);

                //第二步：写入文件的名称(用来服务端下载或者上传文件)
                //取得文件名字节数组
                byte[] fileNameBytes = Encoding.Default.GetBytes(fileName);
                byte[] fileNameBytesArray = new byte[1024];
                Array.Copy(fileNameBytes, fileNameBytesArray, fileNameBytes.Length);
                //socket.Send(fileBytesArray, 0, fileBytesArray.Length, SocketFlags.None);


                byte[] fileDataBytesArray;
                //第三步：读取文件流数据
                using (fsMyfile = new FileStream(path, FileMode.OpenOrCreate, FileAccess.ReadWrite))
                {
                    fileDataBytesArray = new byte[fsMyfile.Length];
                    brMyfile = new BinaryReader(fsMyfile);
                    ///写入流
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    while ((count = brMyfile.Read(buffer, 0, 1024)) > 0)
                    {
                        Array.Copy(buffer, fileDataBytesArray, fileDataBytesArray.Length);
                        buffer = new byte[1024];
                    }
                    //延时获取全部数据
                    Delay(1.0);
                    receiveInfo = DateTime.Now.ToString() + "文件上传成功";

                }

                byte[] sendData = new byte[flagBytesArray.Length + fileNameBytesArray.Length + fileDataBytesArray.Length];



                //   TcpClient client = new TcpClient(Ip, Port);
                //   stream = client.GetStream();
                //   sw = new BinaryWriter(stream);
                ////   br = new BinaryReader(stream);

                //   //第一步：写入通讯标志(用来区分是下载还是上传文件)  1-代表上传   2-代表下载  0-代表测试
                //   byte[] flagBytes = Encoding.Default.GetBytes(flag);
                //   byte[] flagBytesArray = new byte[10];
                //   Array.Copy(flagBytes, flagBytesArray, flagBytes.Length);
                //   sw.Write(flagBytesArray, 0, flagBytesArray.Length);
                //   sw.Flush();//发送标志


                //   //第二步：写入文件的名称(用来服务端下载或者上传文件)
                //   //取得文件名字节数组
                //   byte[] fileNameBytes = Encoding.Default.GetBytes(fileName);
                //   byte[] fileBytesArray = new byte[1024];
                //   Array.Copy(fileNameBytes, fileBytesArray, fileNameBytes.Length);
                //   sw.Write(fileBytesArray, 0, fileBytesArray.Length);
                //   sw.Flush();//发送文件名称


                //   //第三步：读取文件流数据
                //   using (fsMyfile = new FileStream(path, FileMode.OpenOrCreate, FileAccess.ReadWrite))
                //   {

                //       brMyfile = new BinaryReader(fsMyfile);
                //       ///写入流
                //       byte[] buffer = new byte[1024];
                //       int count = 0;
                //       while ((count = brMyfile.Read(buffer, 0, 1024)) > 0)
                //       {
                //           sw.Write(buffer, 0, count);
                //           sw.Flush();
                //           buffer = new byte[1024];
                //       }
                //       //延时获取全部数据
                //       Delay(1.0);
                //       receiveInfo = DateTime.Now.ToString() + "文件上传成功";

                //   }

                //   //if (client.Connected)
                //   //{
                //   //    MessageBox.Show("连接着--");
                //   //}

                //   //第二步：开始接受服务端返回的数据
                //   byte[] bytes = new byte[1024];
                //   //开始接受服务端的返回数据
                //   int bytesRead = stream.Read(bytes, 0, bytes.Length);

                //   receiveInfo = Encoding.Default.GetString(bytes).TrimEnd('\0');//注意去掉接收的特殊结尾符
                //   MessageBox.Show("服务器返回信息：" + Encoding.Default.GetString(bytes).TrimEnd('\0'));
                receiveInfo = "ceshi";
                return 0;
            }
            catch (SocketException se)
            {
                receiveInfo = se.StackTrace;
                return -2;
            }
            catch (IOException ioe)
            {
                receiveInfo = ioe.StackTrace;
                return -3;
            }
            catch (Exception ex)
            {
                receiveInfo = ex.StackTrace;

                return -4;
            }
            finally
            {
                if (sw != null)
                {
                    sw.Close();
                }
                if (brMyfile != null)
                {
                    brMyfile.Close();
                }
                if (fsMyfile != null)
                {
                    fsMyfile.Close();
                }
                if (stream != null)
                {
                    stream.Close();
                }
                if (bw != null)
                {
                    bw.Close();
                }
            }

        }

        /// <summary>
        /// 发送文件 
        /// </summary>
        /// <param name="flag"> 1-代表上传   2-代表下载  0-代表测试</param>
        /// <param name="path">文件全路径</param>
        /// <param name="fileName">文件名</param>
        /// <returns>返回执行结果0成功，-1文件不存在，-2连接失败，-3IO异常，-4未知异常</returns>
        public static int DownLoad(String flag, String fileName, out string receiveInfo)
        {
            NetworkStream stream = null;
            BinaryWriter sw = null;
            FileStream fsMyfile = null;
            BinaryReader brMyfile = null;
            BinaryReader br = null;
            BinaryWriter bw = null;

            try
            {
                TcpClient client = new TcpClient(Ip, Port);
                stream = client.GetStream();
                sw = new BinaryWriter(stream);
                br = new BinaryReader(stream);

                //第一步：写入通讯标志(用来区分是下载还是上传文件)  1-代表上传   2-代表下载  0-代表测试
                byte[] flagBytes = Encoding.Default.GetBytes(flag);
                byte[] flagBytesArray = new byte[10];
                Array.Copy(flagBytes, flagBytesArray, flagBytes.Length);
                sw.Write(flagBytesArray, 0, flagBytesArray.Length);
                sw.Flush();//发送标志


                //第二步：写入下载文件名称(用来服务端下载或者上传文件)  下载路径在服务端的配置文件中已经配置
                //取得文件名字节数组
                byte[] fileNameBytes = Encoding.Default.GetBytes(fileName);
                byte[] fileBytesArray = new byte[1024];
                Array.Copy(fileNameBytes, fileBytesArray, fileNameBytes.Length);
                sw.Write(fileBytesArray, 0, fileBytesArray.Length);
                sw.Flush();//发送文件名称


                //延时获取全部数据
                Delay(1.0);

                //检查本地存储路径是否正确
                if (!Directory.Exists(@"F:\upload\"))//如果不存在就创建file文件夹
                {
                    Directory.CreateDirectory(@"F:\upload\");
                }

                fileName = DateTime.Now.ToString("yyyyMMddHHmmss") + fileName;
                //判断文件的存在
                if (!File.Exists(@"F:\upload\" + fileName))
                {
                    //不存在文件   注意此处：应从此处获取文件流
                    using (File.Create(@"F:\upload\" + fileName)) { }//创建该文件
                }

                string savePath = @"F:\upload\" + fileName;

                if (UseAPI.IsOpen(savePath))
                {
                    MessageBox.Show("文件正处于打开状态--不允许操作");
                }


                fsMyfile = new FileStream(savePath, FileMode.Open, FileAccess.ReadWrite, FileShare.ReadWrite);


                //第三步：读取文件流数据
                bw = new BinaryWriter(fsMyfile);
                ///写入流
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = br.Read(buffer, 0, 1024)) > 0)
                {
                    bw.Write(buffer, 0, count);
                    bw.Flush();
                    buffer = new byte[1024];
                }





                receiveInfo = DateTime.Now.ToString() + "文件下载成功";
                return 8;

                //第二步：开始接受服务端返回的数据
                //byte[] bytes = new byte[1024];
                ////开始接受服务端的返回数据
                //int bytesRead = br.Read(bytes, 0, bytes.Length);

                //receiveInfo = Encoding.Default.GetString(bytes).TrimEnd('\0');//注意去掉接收的特殊结尾符
                //MessageBox.Show("服务器返回信息：" + Encoding.Default.GetString(bytes));


            }
            catch (SocketException se)
            {
                receiveInfo = se.StackTrace;
                return -2;
            }
            catch (IOException ioe)
            {
                receiveInfo = ioe.StackTrace;
                return -3;
            }
            catch (Exception ex)
            {
                receiveInfo = ex.StackTrace;

                return -4;
            }
            finally
            {
                if (sw != null)
                {
                    sw.Close();
                }
                if (brMyfile != null)
                {
                    brMyfile.Close();
                }
                if (fsMyfile != null)
                {
                    fsMyfile.Close();
                }
                if (stream != null)
                {
                    stream.Close();
                }
                if (bw != null)
                {
                    bw.Close();
                }
            }

        }


        /// <summary>
        /// 测试通讯
        /// </summary>
        /// <param name="name">测试内容需和服务端校验内容保持一致</param>
        /// <returns></returns>
        public static bool TestConnection(string name, out string receiveInfo)
        {

            NetworkStream stream = null;
            BinaryWriter sw = null;
            try
            {
                TcpClient client = new TcpClient(Ip, Port);
                stream = client.GetStream();
                sw = new BinaryWriter(stream);

                //第一步：写入通讯标志(用来区分是下载还是上传文件)  1-代表上传   2-代表下载  0-代表测试
                byte[] flagBytes = Encoding.Default.GetBytes("0");
                byte[] flagBytesArray = new byte[10];
                Array.Copy(flagBytes, flagBytesArray, flagBytes.Length);
                sw.Write(flagBytesArray, 0, flagBytesArray.Length);
                sw.Flush();//发送标志

                byte[] fileNameBytes = Encoding.Default.GetBytes(name);
                sw.Write(fileNameBytes, 0, fileNameBytes.Length);
                sw.Flush();


                //延时获取全部数据
                Delay(0.1);


                //第二步：开始接受服务端返回的数据
                byte[] bytes = new byte[1024];
                //开始接受服务端的返回数据
                int bytesRead = stream.Read(bytes, 0, bytes.Length);

                receiveInfo = Encoding.Default.GetString(bytes).TrimEnd('\0');//注意去掉接收的特殊结尾符
                //这里可以获取到服务端返回的数据 MessageBox.Show("服务器返回信息：" + Encoding.Default.GetString(bytes));




                return true;
            }
            catch (Exception ex)
            {

                receiveInfo = ex.Message;
                return false;
            }
        }
        private static void Delay(double second)
        {

            DateTime now = DateTime.Now;

            while (now.AddSeconds(second) > DateTime.Now)
            {

            }

        }




    }
}
