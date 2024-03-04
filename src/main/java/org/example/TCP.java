package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCP {
    public static void main(String[] args) {
        String ip = "109.167.241.225";
        int port = 6340;

        try {
            Socket socket = new Socket(ip, port);
            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[65536];
            byte[] result = new byte[0];

            while (true) {
                int n = inputStream.read(buffer);
                if (n == -1) {
                    break;
                }
                byte[] temp = new byte[result.length + n];
                System.arraycopy(result, 0, temp, 0, result.length);
                System.arraycopy(buffer, 0, temp, result.length, n);
                result = temp;
                if (n != 4) {
                    break;
                }
                Thread.sleep(1000);
            }

            String resultString = new String(result);
            Pattern pattern = Pattern.compile("Student20\\s\\S+\\s\\S+");
            Matcher matcher = pattern.matcher(resultString);

            int count = 0;
            while (matcher.find() && count < 10) {
                System.out.println(matcher.group());
                count++;
            }

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
