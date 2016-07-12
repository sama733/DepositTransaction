package clientSide;

import serverSide.Response;
import serverSide.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class MainClient implements Serializable {
    static FileHandler fileHandler = null;
    static Logger logger = Logger.getLogger(MainClient.class.getName());
    MainTerminal mainTerminal = new MainTerminal();
    List<Transaction> clientTransactions = mainTerminal.setInformations(mainTerminal.readXmlFile());

    //---------------------------------------------
    public static void main(String[] args) {
        MainClient mainClient = new MainClient();
        Socket mainSocket = mainClient.getConnection();

        try {
            fileHandler = new FileHandler("src/main/" + mainClient.mainTerminal.getOutLogPath(), true);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(MainClient.class.getName()).log(Level.ALL, null, e);
        }

        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);

        mainClient.sendRequest(mainSocket);
        mainClient.getResponse(mainSocket);
    }

    //-------------------------------------------
    public Socket getConnection() {
        Socket client = null;
        try {
            client = new Socket(mainTerminal.getServerIp(), mainTerminal.getServerPort());
            logger.info("client connected to server on port :" + client.getLocalPort());
            //System.out.println("MainClient.getConnection, is Success.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainClient.getConnection, is Faild.");
            logger.info("Connecting to server faild");
        }
        return client;
    }

    //-------------------------------------------
    public void sendRequest(Socket socket) {
        try {
            ObjectOutputStream dataOutputToServer = new ObjectOutputStream(socket.getOutputStream());
            dataOutputToServer.writeObject(clientTransactions);
            dataOutputToServer.flush();
            //dataOutputToServer.close();
            System.out.println("MainClient.SendRequest, is Success.");
            logger.info("send request to server ...");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainClient.SendRequest, is Faild.");
        }
    }

    //-------------------------------------------
    public void getResponse(Socket mainSocket) {
        try {
            System.out.println("mainSocket.isClosed:" + mainSocket.isClosed());
            System.out.println("mainSocket.isConnected:" + mainSocket.isConnected());

            ObjectInputStream objectInputStreamFromServer = new ObjectInputStream(mainSocket.getInputStream());
            List<Response> clientResponse = (List<Response>) objectInputStreamFromServer.readObject();
            logger.info("get response from server");
            mainTerminal.seveToXml(clientResponse);
            //objectInputStreamFromServer.close();
            // System.out.println("MainClient.getResponse, is Success.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("MainClient.getResponse, is Falid.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MainClient.getResponse, is Falid.");
        }
    }
}


