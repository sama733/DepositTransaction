package ir.dotin.school.core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class RunServer {

    private String outLog;
    private static int port;
    private static List<Deposit> deposits;

    public static void main(String args[]) {
        ServerSocket myServerSocket = null;
        RunServer mainServer = new RunServer();
        mainServer.Config();
        System.out.println("Main: " + Thread.currentThread().getName());
        try {
            myServerSocket = new ServerSocket(port);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        while (true) {
            try {
                System.out.println("Server Waiting");
                Socket clientSocket = myServerSocket.accept();
                new ThreadServer(clientSocket, deposits).start();
            } catch (IOException ioe) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }

    }

    public void Config() {
        ParseJSON jSonParser = new ParseJSON();
        jSonParser.jSonReader();
        deposits = jSonParser.getDeposits();
        outLog = jSonParser.getOutLogPath();
        port = jSonParser.getPort().intValue();
    }


}