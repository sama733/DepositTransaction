package serverSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainServer implements Runnable {

    static FileHandler fileHandler = null;
    static Logger logger = Logger.getLogger(MainServer.class.getName());
    private static String outLog;
    private static int port;

    //---------------------------------------------------
 /*   public MainServer(int portServer) {
        setPort(portServer);
    }*/

    public void runServer() {
        MainServer mainServer = new MainServer();
        MainJSonParser mainJSonParser = new MainJSonParser();
        mainServer.Config();
        TransactionsValidation transactionsValidator = new TransactionsValidation();

        try {
            fileHandler = new FileHandler("src/main/" + mainServer.getOutLog(), true);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(MainServer.class.getName()).log(Level.ALL, null, e);
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);

        Socket socket = mainServer.getConnection();
        List<Transaction> transactions = mainServer.getRequest(socket);
        List<Deposit> deposits = mainJSonParser.getDeposits();
        List<Response> responses = transactionsValidator.findDeposit(transactions, deposits);
        mainServer.sendResponse(socket, responses);

//        System.out.println("transactions:" + transactions.toString());
        System.out.println("deposits:" + deposits);
        System.out.println("responses:" + responses);

        try {
            socket.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    //-------------------------------------------
    public String getOutLog() {
        return outLog;
    }

    //-------------------------------------------
    public void setOutLog(String outLog) {
        this.outLog = outLog;
    }

    //-------------------------------------------
    public int getPort() {
        return port;
    }

    //-------------------------------------------
    public void setPort(int port) {
        this.port = port;
    }

    //-------------------------------------------
    public Socket getConnection() {
        Socket socket = null;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Waiting");
            logger.info("serverSocket create");
            socket = serverSocket.accept();
            logger.info("serversSocket is listening to port:" + serverSocket.getLocalPort());
            serverSocket.close();
            System.out.println("MainServer.getConnection, is Successful.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainServer.getConnection, is Failed.");
        }

        return socket;
    }

    //-------------------------------------------

    public List<Transaction> getRequest(Socket socket) {
        try {
            ObjectInputStream objectInputStreamFromClient = new ObjectInputStream(socket.getInputStream());
            List<Transaction> transactions = (List<Transaction>) objectInputStreamFromClient.readObject();
            logger.info("get request from client");
            //objectInputStreamFromClient.close();
            // System.out.println("MainServer.getRequest, is Successful.");
            return transactions;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainServer.getRequest, is Failed.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MainServer.getRequest, is Failed.");
        }
        return null;
    }

    //-------------------------------------------

    public void sendResponse(Socket socket, List<Response> response) {
        try {
            ObjectOutputStream dataOutputToServer = new ObjectOutputStream(socket.getOutputStream());
            dataOutputToServer.writeObject(response);
            dataOutputToServer.flush();
            logger.info("send response to client");
            //dataOutputToServer.close();
            //System.out.println("MainServer.sendResponse, is Successful.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainServer.sendResponse, is Failed.");
        }
    }

    //-------------------------------------------

    public void Config() {
        MainJSonParser jSonParser = new MainJSonParser();
        jSonParser.jSonReader();
        jSonParser.getOutLogPathAndPort();
    }

    public void run() {
        runServer();
    }

}