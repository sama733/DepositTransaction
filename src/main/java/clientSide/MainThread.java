package clientSide;

import serverSide.Response;
import serverSide.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainThread implements Serializable, Runnable {


    private String xmlFilePath;
    private Socket mainSocket;
    private MainTerminal mainTerminal = new MainTerminal();
    private List<Transaction> clientTransactions;
    Logger clientLogger = Logger.getLogger(MainThread.class.getName());

    public MainThread(String xmlFilePathParameter) {
        this.xmlFilePath = xmlFilePathParameter;
    }

    public synchronized void initialize() {
        clientTransactions = mainTerminal.setInformations(mainTerminal.readXmlFile(xmlFilePath));
        mainSocket = getConnection();
    }

    public Socket getConnection() {
        Socket client = null;
        try {
            client = new Socket(mainTerminal.getServerIp(), mainTerminal.getServerPort());
            System.out.println("client connected to server on port :" + client.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainThread.getConnection, is Failed.");
        }
        return client;
    }

    public  void sendRequest() {
//        System.out.println("send mainSocket.isClosed:" + mainSocket.isClosed());
//        System.out.println("send mainSocket.isConnected:" + mainSocket.isConnected());

        try {
            ObjectOutputStream dataOutputToServer = new ObjectOutputStream(mainSocket.getOutputStream());
            dataOutputToServer.writeObject(clientTransactions);
            dataOutputToServer.flush();
//            System.out.println("MainThread.SendRequest, is successful.");
            clientLogger.log(Level.INFO," MainThread.SendRequest, is successful to : " + Thread.currentThread() + ".");
        } catch (IOException e) {
            e.printStackTrace();
//            System.err.println("MainThread.SendRequest, is Failed.");
            clientLogger.log(Level.INFO," MainThread.SendRequest, is failed to : " + Thread.currentThread() + ".");
        }
    }

    public  void getResponse() {
        try {
            System.out.println("receive mainSocket.isClosed:" + mainSocket.isClosed());
            System.out.println("receive mainSocket.isConnected:" + mainSocket.isConnected());
            ObjectInputStream objectInputStreamFromServer = new ObjectInputStream(mainSocket.getInputStream());
            List<Response> clientResponse = (List<Response>) objectInputStreamFromServer.readObject();
            clientLogger.log(Level.INFO," MainThread.getResponse, is successful from : " + Thread.currentThread() + ".");
//            System.out.println("get response from server");
            System.out.println("Response" + clientResponse.toString());
            mainTerminal.seveToXml(clientResponse);
//            System.out.println("MainThread.getResponse, is successful.");
            clientLogger.log(Level.INFO," MainThread.getResponse, is successful from : " + Thread.currentThread() + ".");
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("MainThread.getResponse, is Falied.");
            clientLogger.log(Level.INFO," MainThread.SendRequest, is failed : " + Thread.currentThread() + ".");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
//            System.out.println("MainThread.getResponse, is Falied.");
            clientLogger.log(Level.INFO," MainThread.SendRequest, is failed : " + Thread.currentThread() + ".");
        }
    }

    private void closeConnection() {
        try {
            mainSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close connection to server.");
            e.printStackTrace();
        }
    }

    public void run() {
        MainThread mainClient = new MainThread(xmlFilePath);
        mainClient.initialize();
        mainClient.sendRequest();
        mainClient.getResponse();
        mainClient.closeConnection();

    }
}