package ir.dotin.school.core.client;

import ir.dotin.school.core.server.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread {


    private Logger clientLogger = Logger.getLogger(ClientThread.class.getName());
    private String xmlFilePath;
    private Socket mainSocket;
    private Terminal terminal;

    public ClientThread(String xmlFilePathParameter) {
        this.xmlFilePath = xmlFilePathParameter;

        terminal = new Terminal();
        terminal.readInformationFromFile(xmlFilePath);

    }

    public void run() {
        mainSocket = getConnection();
        sendRequest();
        getResponse();
        closeConnection();
        logFileInClient();
    }

    private Socket getConnection() {
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(terminal.getServerIp(), terminal.getServerPort());
            System.out.println("ir.dotin.school.core.client connected to ir.dotin.school.core.server on port :" + clientSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ClientThread.getConnection, is Failed.");
        }
        return clientSocket;
    }

    private void sendRequest() {
        try {
            ObjectOutputStream dataOutputToServer = new ObjectOutputStream(mainSocket.getOutputStream());
            dataOutputToServer.writeObject(terminal.getTransactions());
            dataOutputToServer.flush();
            clientLogger.log(Level.INFO, " ClientThread.SendRequest, is successful to : " + Thread.currentThread() + ".");
        } catch (IOException e) {
            e.printStackTrace();
            clientLogger.log(Level.INFO, " ClientThread.SendRequest, is failed to : " + Thread.currentThread() + ".");
        }
    }

    private void getResponse() {
        try {
            ObjectInputStream objectInputStreamFromServer = new ObjectInputStream(mainSocket.getInputStream());
            List<Response> clientResponses = (List<Response>) objectInputStreamFromServer.readObject();
            clientLogger.log(Level.INFO, " ClientThread.getResponse, is successful from : " + Thread.currentThread() + ".");

            System.out.println("Response" + clientResponses.toString());
            terminal.seveToXml(clientResponses);
            clientLogger.log(Level.INFO, " ClientThread.getResponse, is successful from : " + Thread.currentThread() + ".");
        } catch (IOException e) {
            e.printStackTrace();

            clientLogger.log(Level.INFO, " ClientThread.SendRequest, is failed : " + Thread.currentThread() + ".");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            clientLogger.log(Level.INFO, " ClientThread.SendRequest, is failed : " + Thread.currentThread() + ".");
        }
    }

    private void closeConnection() {
        try {
            mainSocket.close();
        } catch (IOException e) {
            System.err.println("Could not close connection to ir.dotin.school.core.server.");
            e.printStackTrace();
        }
    }

    private void logFileInClient() {
        clientLogger.setUseParentHandlers(false);

    }
}