using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;

namespace Socket通讯
{
    /// <summary>
    /// Description of UseAPI.
    /// </summary>
    public class UseAPI
    {

        public UseAPI()
        {
        }

        private const int OF_READWRITE = 2;
        private const int OF_SHARE_DENY_NONE = 0x40;
        private static readonly IntPtr HFILE_ERROR = new IntPtr(-1);


        /// <summary>
        /// 判断文件是否打开
        /// </summary>
        /// <param name="lpPathName">文件名称</param>
        /// <param name="iReadWrite"></param>
        /// <returns></returns>
        [DllImport("kernel32.dll")]
        private static extern IntPtr _lopen(string lpPathName, int iReadWrite);

        /// <summary>
        /// 关闭文件句柄
        /// </summary>
        /// <param name="hObject"></param>
        /// <returns></returns>
        [DllImport("kernel32.dll")]
        private static extern bool CloseHandle(IntPtr hObject);

        /// <summary>
        /// 检查指定的文件是否打开
        /// </summary>
        /// <param name="filePath">文件路径</param>
        /// <returns>True:打开状态；Flase:关闭状态</returns>
        public static bool IsOpen(string filePath)
        {
            IntPtr vHandle = _lopen(filePath, OF_READWRITE | OF_SHARE_DENY_NONE);
            if (vHandle == HFILE_ERROR)
            {
                CloseHandle(vHandle);
                return true;
            }
            else
                return false;
        }
    }
}
