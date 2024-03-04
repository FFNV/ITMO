package org.example;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MQTTSubscriber {

    public static void main(String[] args) {
        String[] topics = new String[32];
        for (int i = 0; i < 30; i++) {
            topics[i] = "ITMO/Student" + i + "/Value3";
        }
        topics[30] = "ITMO/Student20/Value1";
        topics[31] = "ITMO/Student20/Value2";

        MqttClient client;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient("tcp://broker.hivemq.com:1883", MqttClient.generateClientId(), persistence);
            client.connect();

            MqttMessageHandler messageHandler = new MqttMessageHandler();

            for (String topic : topics) {
                client.subscribe(topic, messageHandler);
            }
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    static class MqttMessageHandler implements IMqttMessageListener {
        private HashMap<Integer, Integer> values3 = new HashMap<>();
        private int value1;
        private int value2;
        private Set<String> seen = new HashSet<>();

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            if (seen.contains(topic)) {
                return;
            }
            seen.add(topic);

            String[] parts = topic.split("/");
            String student = parts[1];
            int studentId;
            try {
                studentId = Integer.parseInt(student.substring(student.length() - 2));
            } catch (NumberFormatException e) {
                try {
                    studentId = Integer.parseInt(student.substring(student.length() - 1));
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid student ID format in topic: " + topic);
                    return;
                }
            }

            int valueId;
            try {
                valueId = Integer.parseInt(parts[2].substring(parts[2].length() - 1));
            } catch (NumberFormatException e) {
                System.err.println("Invalid value ID format in topic: " + topic);
                return;
            }

            int payload;
            try {
                payload = Integer.parseInt(new String(message.getPayload()));
            } catch (NumberFormatException e) {
                System.err.println("Invalid payload format in topic: " + topic);
                return;
            }

            if (valueId == 3) {
                values3.put(studentId, payload);
            } else if (topic.endsWith("Value1")) {
                value1 = payload;
            } else if (topic.endsWith("Value2")) {
                value2 = payload;
            }

            if (seen.size() == 32) { // 30 StudentN/Value3 + Student20/Value1 и Student20/Value2
                printValues();
            }
        }

        private void printValues() {
            System.out.println("Value1: " + value1);
            System.out.println("Value2: " + value2);
            System.out.println("Value3:");
            for (int studentId : values3.keySet()) {
                System.out.println("Student" + studentId + ": " + values3.get(studentId));
            }
        }
    }
}
