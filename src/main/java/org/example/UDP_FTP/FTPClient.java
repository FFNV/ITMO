package org.example.UDP_FTP;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.net.ftp.FTP;

public class FTPClient {
    public static void main(String[] args) {
        String server = "109.167.241.225";
        int port = 21;
        String user = "Student";
        String pass = "FksG5$%^rgtdSDFH";
        String remoteFile = "UDP log.txt";

        org.apache.commons.net.ftp.FTPClient ftpClient = new org.apache.commons.net.ftp.FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            FileOutputStream outputStream = new FileOutputStream(remoteFile);
            boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
            outputStream.close();

            if (success) {
                System.out.println("Файл успешно скачан.");
            } else {
                System.out.println("Не удалось скачать файл.");
            }
        } catch (IOException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}