package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Chat client that connects to the server.
 * @author Zac Tawfick
 */
public class Client {
	/**The port used to connect to the server. */
    private static final int port = 4444;
    /** Client username. */
    private String userName;
    /**Hostname of the server. */
    private String serverHost;
    /**Port number of the server. */
    private int serverPort;

    /**
     * Asks user for username and creates a client object and starts it.
     */
    public static void main(String[] args){
        String name = null;
        Scanner console = new Scanner(System.in);
        System.out.println("Please input username:");
        while(name == null || name.trim().equals("")){
            // null, empty, whitespace(s) not allowed.
            name = console.nextLine();
            if(name.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        Client client = new Client(name, "LocalHost", port);
        client.startClient(console);
    }
    /**
     * Creates a new Client object. 
     * @param userName username of the client.
     * @param host hostname of the server.
     * @param portNumber the port used to connect to the server.
     */
    private Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }
    /**
     * Connects to the server using a socket.
     * @param console used to read input and send it to the server.
     */
    private void startClient(Scanner console){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); // waiting to establish network communication.

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()){
                if(console.hasNextLine()){
                    serverThread.addNextMessage(console.nextLine());
                }
            }
        }catch(IOException ex){ // For connection errors.
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){ // For thread interruptions.
            System.out.println("Interrupted");
        }
    }
}