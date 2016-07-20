package clientSide;

public class MainClients {
    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            public void run() {
//                MainThread.main(null);
//            }
//        }).start();
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        MainThread.main(null);

        Thread threadTerminal1 = new Thread(new MainThread("terminal.xml"));
        threadTerminal1.start();

        Thread threadTerminal2 = new Thread(new MainThread("terminal2.xml"));
        threadTerminal2.start();
    }
}
