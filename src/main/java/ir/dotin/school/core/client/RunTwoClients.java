package ir.dotin.school.core.client;

public class RunTwoClients {
    public static void main(String[] args) {

        Thread threadTerminal1 = new ClientThread("terminal.xml");
        threadTerminal1.start();

        Thread threadTerminal2 = new Thread(new ClientThread("terminal2.xml"));
        threadTerminal2.start();
    }
}
