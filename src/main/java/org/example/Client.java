package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private JFrame frame;
    private JTextField textField;
    private JTextArea textArea;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {
        Client client = new Client();
        client.initialize();
    }

    public void initialize() {
        try {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Создаем GUI
            frame = new JFrame("Client Application");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

            textField = new JTextField();
            textField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage();
                }
            });
            frame.add(textField);

            textArea = new JTextArea();
            textArea.setEditable(false);
            frame.add(new JScrollPane(textArea));

            frame.setVisible(true);

            // Чтение сообщений от сервера
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        textArea.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = textField.getText();
        out.println(message);
        textField.setText("");
    }
}
