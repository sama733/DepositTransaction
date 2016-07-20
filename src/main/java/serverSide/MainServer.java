package serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MainServer {

    private static String outLog;
    private static int port;
    private static List<Deposit> deposits;
    public static void main(String args[]) {
        ServerSocket myServerSocket = null;
        MainServer mainServer = new MainServer();
        mainServer.Config();

        try {
            myServerSocket = new ServerSocket(port);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        while (true) {
            try {
                System.out.println("Server Waiting");
                Socket clientSocket = myServerSocket.accept();
                new Thread(new MultiThreadServer(clientSocket, deposits)).start();
            } catch (IOException ioe) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                ioe.printStackTrace();
            }
        }

    }
//
//    public String getOutLog() {
//        return outLog;
//    }
//
//    public void setOutLog(String outLog) {
//        this.outLog = outLog;
//    }
//
//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }

//    public Socket getConnection() {
//        Socket socket = null;
//        try {
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("Server Waiting");
//            socket = serverSocket.accept();
//            serverSocket.close();
//            System.out.println("MainServer.getConnection, is Successful.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("MainServer.getConnection, is Failed.");
//        }
//
//        return socket;
//    }

    public void Config() {
        MainJSonParser jSonParser = new MainJSonParser();
        jSonParser.jSonReader();
        outLog = jSonParser.getOutLogPath();
        port = jSonParser.getPort().intValue();
    }


}