package org.example.UDP_FTP;

import java.io.*;
import java.net.*;

public class UDP {
    public static void main(String args[]) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("109.167.241.225");
        int port = 61557;

        // Отправка данных
        for (int i = 1; i <= 100; i++) {
            String data = "<Фофанов>: " + generateData(i);
            byte[] sendData = data.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
            Thread.sleep(1000); // Пауза между отправками
        }

        clientSocket.close();
    }

    // Генерация данных
    public static int generateData(int iteration) {
        // Пример функции: квадрат числа
        return iteration * iteration;
    }
}



