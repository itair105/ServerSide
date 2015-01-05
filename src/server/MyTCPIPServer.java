package server;

import common.MyProperties;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTCPIPServer {
    private final static String FILE = "e://properties.txt";
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("createXml")) {
            try {
                FileOutputStream fos = new FileOutputStream(FILE);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                XMLEncoder xmlEncoder = new XMLEncoder(bos);
                xmlEncoder.writeObject(new MyProperties(5555, 10));
                xmlEncoder.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            return;
        }

        try {
            FileInputStream fis = new FileInputStream(FILE);
            BufferedInputStream bis = new BufferedInputStream(fis);
            XMLDecoder xmlDecoder = new XMLDecoder(bis);
            MyProperties properties = (MyProperties)xmlDecoder.readObject();
            new MyTCPIPServer(properties.getPort()).startServer(properties.getNumOfClients());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    private int port;

    public MyTCPIPServer(int port) {
        this.port = port;
    }

    public void startServer(int numOfClients) {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(numOfClients);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("Waiting for clients to connect...");
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientProcessingPool.submit(new ClientHandler(clientSocket));
                    }
                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }
}
