using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.IO;
using System.Windows;

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
        /// <param name="path">文件全路径</param>
        /// <param name="fileName">文件名</param>
        /// <returns>返回执行结果0成功，-1文件不存在，-2连接失败，-3IO异常，-4未知异常</returns>
        public static int StartSend(String path, String fileName)
        {
            if (!File.Exists(path))
            {
                return -1;
            }
            NetworkStream stream = null;
            
            BinaryWriter sw = null;
            FileStream fsMyfile = null;
            BinaryReader brMyfile = null;
            try
            {
                TcpClient client = new TcpClient(Ip, Port);
                stream = client.GetStream();
                sw = new BinaryWriter(stream);

              

                //第一步：写入通讯标志(用来区分是下载还是上传文件)  1-代表上传   2-代表下载
                byte[] flagBytes = Encoding.Default.GetBytes("1");
                byte[] flagBytesArray = new byte[10];
                Array.Copy(flagBytes, flagBytesArray, flagBytes.Length);
                sw.Write(flagBytesArray, 0, flagBytesArray.Length);
                sw.Flush();//发送标志


                //第二步：写入文件的名称(用来服务端下载或者上传文件)
                //取得文件名字节数组
                byte[] fileNameBytes = Encoding.Default.GetBytes(fileName);
                byte[] fileBytesArray = new byte[1024];
                Array.Copy(fileNameBytes, fileBytesArray, fileNameBytes.Length);
                sw.Write(fileBytesArray, 0, fileBytesArray.Length);
                sw.Flush();//发送文件名称


                //第三步：读取文件流数据
                fsMyfile = new FileStream(path, FileMode.OpenOrCreate, FileAccess.ReadWrite);
                brMyfile = new BinaryReader(fsMyfile);
                ///写入流
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = brMyfile.Read(buffer, 0, 1024)) > 0)
                {
                    sw.Write(buffer, 0, count);
                    sw.Flush();
                    buffer = new byte[1024];
                }


            }
            catch (SocketException se)
            {
                Console.WriteLine(se.StackTrace);
                return -2;
            }
            catch (IOException ioe)
            {
                Console.WriteLine(ioe.StackTrace);
                return -3;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
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
            }
            return 0;
        }

        /// <summary>
        /// 测试通讯
        /// </summary>
        /// <param name="name">测试内容需和服务端校验内容保持一致</param>
        /// <returns></returns>
        public static bool TestConnection(string name)
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



                return true;
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
                return false;
            }
        }

    }
}
