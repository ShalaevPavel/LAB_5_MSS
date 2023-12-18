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

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String command;
            while ((command = reader.readLine()) != null) {
                if (command.startsWith("exec ")) {
                    String cmd = command.substring(5);
                    executeCommand(cmd, writer);
                } else if (command.startsWith("list ")) {
                    String dirPath = command.substring(5);
                    String fileList = listFilesInDirectory(dirPath);
                    writer.println(fileList);
                }
                if (command.equals("exit")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException ex) {
            System.out.println("Ошибка сервера: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void executeCommand(String command, PrintWriter writer) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        } catch (IOException e) {
            writer.println("Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    private String listFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        StringBuilder fileNames = new StringBuilder();

        if (files != null) {
            for (File file : files) {
                fileNames.append(file.getName()).append("\n");
            }
        } else {
            fileNames.append("Не удалось открыть папку: ").append(directoryPath);
        }
        return fileNames.toString();
    }




}
