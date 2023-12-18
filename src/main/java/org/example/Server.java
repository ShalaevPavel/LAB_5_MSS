package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Сервер запущен и ожидает подключения...");

        while (true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket).start();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            do {
                text = reader.readLine();
                // Здесь можно добавить обработку команд (например, запуск программы, просмотр папок)
                writer.println("Сервер получил: " + text);
            } while (!text.equals("exit"));

            socket.close();
        } catch (IOException ex) {
            System.out.println("Ошибка сервера: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            // Здесь можно добавить обработку результатов выполнения команды
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String listFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        StringBuilder fileNames = new StringBuilder();

        for (File file : files) {
            fileNames.append(file.getName()).append("\n");
        }
        return fileNames.toString();
    }

}
