package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String host = "10.6.128.173";
    private static final int portNumber = 55555;

    private String userName;
    private String serverHost;
    private int serverPort;
    @SuppressWarnings("unused")
	private Scanner userInputScanner;

    public static void main(String[] args){
    	System.out.println("Mr. E's Chat Bonzai (Prod. by Maulty)");
    	System.out.println("Version: a0.1.5");
        String readName = null;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input username:");
        while(readName == null || readName.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        Client client = new Client(readName, host, portNumber);
        client.startClient(scan);
    }

    private Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting for network communicating.

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(scan.hasNextLine()){
                    serverThread.addNextMessage(scan.nextLine());
                } else {
                    Thread.sleep(200);
                }
            }
        }catch(IOException e){
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        }catch(InterruptedException e){
            System.out.println("Interrupted");
        }
    }
}