package ir.dotin.school.core.client;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ir.dotin.school.core.server.Response;
import ir.dotin.school.core.server.Transaction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Terminal {
    private int id;
    private String type;
    private String outLogPath;

    private ServerInfo serverInfo;

    private List<Transaction> transactions;

    public Document readXmlFile(String terminalFile) {
        File xmlFile = new File(terminalFile);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            return document;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setInformations(Document document) {

        transactions = new ArrayList<Transaction>();

        NodeList terminalList = document.getElementsByTagName("terminal");
        Node terminalNode = terminalList.item(0);
        NamedNodeMap terminalAttributes = terminalNode.getAttributes();
        this.id = (Integer.parseInt(terminalAttributes.getNamedItem("id").getTextContent()));
        this.type = (terminalAttributes.getNamedItem("type").getTextContent());


        NodeList serverList = document.getElementsByTagName("server");
        Node serverNode = serverList.item(0);
        NamedNodeMap serverAttributes = serverNode.getAttributes();
        String serverIp = serverAttributes.getNamedItem("ip").getTextContent();
        int serverPort = Integer.parseInt(serverAttributes.getNamedItem("port").getTextContent());
        serverInfo =  new ServerInfo(serverIp, serverPort);


        NodeList outLog = document.getElementsByTagName("outLog");
        Node outLogNode = outLog.item(0);
        NamedNodeMap outLogAttribute = outLogNode.getAttributes();
        this.outLogPath = (outLogAttribute.getNamedItem("path").getTextContent());


        NodeList transactions = document.getElementsByTagName("transaction");
        for (int i = 0; i < transactions.getLength(); i++) {
            Transaction transaction = new Transaction();
            Node node = transactions.item(i);
            transaction.setId((node.getAttributes().getNamedItem("id").getTextContent()));
            transaction.setDepositNumber((node.getAttributes().getNamedItem("deposit").getTextContent()));
            transaction.setType(node.getAttributes().getNamedItem("type").getTextContent());
            transaction.setAmount(new BigDecimal(node.getAttributes().getNamedItem("amount").getTextContent()));
            this.transactions.add(transaction);
        }
    }

    public void seveToXml(List<Response> responses) {

        Element finalResponse = new Element("response");
        org.jdom.Document doc = new org.jdom.Document(finalResponse);

        for (Response response : responses) {

            Element result = new Element("result");
            result.addContent(new Element("id").setText(response.getId()));
            result.addContent(new Element("responseType").setText(String.valueOf(response.getResponseType())));
            result.addContent(new Element("newBalance").setText(String.valueOf(response.getNewBalance())));
            result.addContent(new Element("description").setText(response.getDescription()));

            doc.getRootElement().addContent(result);
        }
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        try {
            xmlOutput.output(doc, new FileWriter("response.xml",true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println("save to xml :" + responses.toString());
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getServerIp() {
        return serverInfo.ip;
    }

    public int getServerPort() {
        return serverInfo.port;
    }

    public String getOutLogPath() {
        return outLogPath;
    }


    public void readInformationFromFile(String xmlFilePath) {
        setInformations(readXmlFile(xmlFilePath));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}