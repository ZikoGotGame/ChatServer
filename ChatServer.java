package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Chat Server that listens for client connections.
 * @author Zac Tawfick
 */
public class ChatServer {
	/** Port number used by this server. */
    private int serverPort;
    /**List of clients connected to the server. */
    private List<ClientThread> clients; 
    /**
     * Creates a new Chat Server that uses port 4444 and starts it.
     */
    public static void main(String[] args){
        ChatServer server = new ChatServer(4444);
        server.startServer();
    }
    /**
     * Constructs a new chat server on a specific port.
     * @param port the port the server will use.
     */
    public ChatServer(int port){
        this.serverPort = port;
    }
    /**
     * Gets the list of clients connected.
     * @return list of clients.
     */
    public List<ClientThread> getClients(){
        return clients;
    }
    /**
     * creates a server socket to listen for incoming client connections.
     */
    private void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }
    /**
     * Accepts client connections and creates a client thread to handle communication with the client.
     * @param serverSocket the server socket used to listen for client connections.
     */
    private void acceptClients(ServerSocket serverSocket){
        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+serverPort);
            }
        }
    }
}
