package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCP {

    public static void main(String[] args) {
        String ip = "109.167.241.225";
        int port = 6340;

        try {
            Socket socket = new Socket(ip, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            StringBuilder result = new StringBuilder();
            char[] buffer = new char[65536];

            while (true) {
                int n = reader.read(buffer);
                if (n == -1) {
                    break;
                }
                result.append(buffer, 0, n);
            }

            Pattern pattern = Pattern.compile("Student20\\s\\S+\\s\\S+");
            Matcher matcher = pattern.matcher(result.toString());

            int count = 0;
            while (matcher.find() && count < 10) {
                System.out.println(matcher.group());
                count++;
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
