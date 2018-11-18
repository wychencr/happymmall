package com.cr.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FTPTest {
    public static void main(String[] args) {
        boolean isSuccess = false;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("127.0.0.1", 21);
            isSuccess = ftpClient.login("ftpuser", "ftpuser");
            System.out.println("isSuccess = " + isSuccess);

            ftpClient.changeWorkingDirectory("img");
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            /************************** 很重要 ***********************/
            ftpClient.enterLocalPassiveMode();
            ftpClient.setUseEPSVwithIPv4(true);
            /********************************************************/

            String path = "E:\\Java Projects\\HappyMMall\\happymmall\\target\\happymmall\\upload";
            String uploadFileName = "f3b5ec2b-5c72-42f9-a9ed-23974816da9f.png";
            File targetFile = new File(path, uploadFileName);
            System.out.println("targetFile = " + targetFile.isFile());
            System.out.println("targetFile = " + targetFile.getName());

            System.out.println("ftpClient.isConnected() = " + ftpClient.isConnected());
            System.out.println("ftpClient.isAvailable() = " + ftpClient.isAvailable());
            System.out.println("ftpClient.printWorkingDirectory() = " + ftpClient.printWorkingDirectory());
            System.out.println(ftpClient.getStatus());


            isSuccess = ftpClient.storeFile(targetFile.getName(), new FileInputStream(targetFile));

            System.out.println("isSuccess = " + isSuccess);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
