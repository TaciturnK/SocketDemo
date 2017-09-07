using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Net.Sockets;
using System.Threading;
using System.Net;
using Microsoft.Win32;
using System.IO;

namespace Socket通讯
{
    /// <summary>
    /// MainWindow.xaml 的交互逻辑
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            txt_ip.Text = "127.0.0.1";
            txt_port.Text = "60000";
        }

        /// <summary>
        /// 单纯测试字符串传输
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void button1_Click(object sender, RoutedEventArgs e)
        {

        }


        private void btn_Send_Click(object sender, RoutedEventArgs e)
        {
            //  MessageBox.Show(txt_sendInfo.Text.Substring(txt_sendInfo.Text.LastIndexOf('\\') + 1));

            if (string.IsNullOrEmpty(txt_sendInfo.Text.Trim()))
            {
                MessageBox.Show("文件路径不能为空！");
                return;
            }

            //设置IP和端口号
            FileUtils.Ip = txt_ip.Text.Trim();

            int port = 60000;
            if (Int32.TryParse(txt_port.Text.Trim(), out port))
            {
                FileUtils.Port = port;
            }
            else
            {
                MessageBox.Show("端口号格式不正确，请检查！");
                return;
            }


            //注意此处的测试内容需和服务端保持一致  否则无法判断是否成功
            if (FileUtils.TestConnection("test message"))
            {
                int result = FileUtils.StartSend(txt_sendInfo.Text, "Socket" + txt_sendInfo.Text.Substring(txt_sendInfo.Text.LastIndexOf('\\') + 1));
                if (result == 0)
                {
                    MessageBox.Show("文件发送成功！");
                }
                else if (result == -1)
                {
                    MessageBox.Show("文件不存在！");

                }
                else if (result == -2)
                {
                    MessageBox.Show("连接失败！");

                }
                else if (result == -3)
                {
                    MessageBox.Show("IO异常！");

                }
                else if (result == -4)
                {
                    MessageBox.Show("未知异常！");

                }
            }
            else
            {
                MessageBox.Show("无法连接服务器！");
            }


        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            //设置IP和端口号
            FileUtils.Ip = txt_ip.Text.Trim();

            int port = 60000;
            if (Int32.TryParse(txt_port.Text.Trim(), out port))
            {
                FileUtils.Port = port;
            }
            else
            {
                MessageBox.Show("端口号格式不正确，请检查！");
                return;
            }

            if (FileUtils.TestConnection("test message"))
            {
                MessageBox.Show("通讯测试成功 ");
            }
            else
            {
                MessageBox.Show("通讯测试失败");
            }
        }

        private void 选择文件_Click(object sender, RoutedEventArgs e)
        {
            string path = string.Empty;
            OpenFileDialog dialog = new OpenFileDialog();
            dialog.Title = "选择发送文件";
            dialog.FileName = string.Empty;
            bool? nullable = dialog.ShowDialog();
            if (nullable.GetValueOrDefault() && ((bool)nullable.HasValue))
            {
                path = dialog.FileName;
            }

            txt_sendInfo.Text = path;
        }

    }
}
