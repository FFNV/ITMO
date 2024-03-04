package org.example;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;

public class ModbusTCP {

    public static void main(String[] args) {
        String ipAddress = "109.167.241.225";
        int port = 601;
        int slaveId = 1;
        int studentNumber = 20;
        int startRegister = studentNumber * 100;

        try {
            InetAddress addr = InetAddress.getByName(ipAddress);
            TCPMasterConnection connection = new TCPMasterConnection(addr);
            connection.setPort(port);
            connection.connect();

            ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(startRegister, 7);
            request.setUnitID(slaveId);

            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            transaction.setRequest(request);

            transaction.execute();

            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
            if (response != null) {
                int firstNumber = response.getRegisterValue(0);
                int secondNumber = response.getRegisterValue(1);
                String asciiString = new String(response.getRegister(2).toBytes());

                System.out.println("Current Time: " + System.currentTimeMillis());
                System.out.println("First Number: " + firstNumber);
                System.out.println("Second Number: " + secondNumber);
                System.out.println("ASCII String: " + asciiString);
            }

            connection.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



