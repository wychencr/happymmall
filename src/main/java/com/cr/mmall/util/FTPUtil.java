package com.cr.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftppass = PropertiesUtil.getProperty("ftp.pass");

    private FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    // 静态方法
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftppass);
        logger.info("开始连接ftp服务器");
        String remotePath = "/img";
        boolean result = ftpUtil.uploadFile(remotePath, fileList);

        logger.info("结束上传，上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = false;
        FileInputStream fis = null;
        // 连接FTP服务器
        if (connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                logger.info("连接ftp服务器成功");
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                /************************** 很重要 ***********************/
                ftpClient.enterLocalPassiveMode();
                ftpClient.setUseEPSVwithIPv4(true);
                /********************************************************/

                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
                uploaded = true;
            } catch (IOException e) {
                logger.info("上传文件异常", e);
                uploaded = false;
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        logger.info("尝试连接FTP服务器");
        try {
            ftpClient.connect(ip, port);
            isSuccess = ftpClient.login(user, pwd);
            logger.info("连接FTP服务器状态：{}", isSuccess);
        } catch (IOException e) {
            logger.info("连接FTP服务器异常", e);
        }
        return isSuccess;

    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }


//    /**
//     * Test FTP Server
//     * @param args:
//     */
//    public static void main(String[] args) {
//        boolean isSuccess = false;
//
//        FTPClient ftpClient = new FTPClient();
//        try {
//            ftpClient.connect("127.0.0.1", 21);
//            isSuccess = ftpClient.login("ftpuser", "ftpuser");
//            System.out.println("isSuccess = " + isSuccess);
//
//            ftpClient.changeWorkingDirectory("img");
//            ftpClient.setBufferSize(1024);
//            ftpClient.setControlEncoding("UTF-8");
//            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//
//            /************************** 很重要 ***********************/
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.setUseEPSVwithIPv4(true);
//            /********************************************************/
//
//            String path = "E:\\Java Projects\\HappyMMall\\happymmall\\target\\happymmall\\upload";
//            String uploadFileName = "f3b5ec2b-5c72-42f9-a9ed-23974816da9f.png";
//            File targetFile = new File(path, uploadFileName);
//            System.out.println("targetFile = " + targetFile.isFile());
//            System.out.println("targetFile = " + targetFile.getName());
//
//            System.out.println("ftpClient.isConnected() = " + ftpClient.isConnected());
//            System.out.println("ftpClient.isAvailable() = " + ftpClient.isAvailable());
//            System.out.println("ftpClient.printWorkingDirectory() = " + ftpClient.printWorkingDirectory());
//            System.out.println(ftpClient.getStatus());
//
//
//            isSuccess = ftpClient.storeFile(targetFile.getName(), new FileInputStream(targetFile));
//
//            System.out.println("isSuccess = " + isSuccess);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
