package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * Thread that establishes connection with clients.
 * @author Zac Tawfick
 */
public class ClientThread implements Runnable {
	/** Socket connection to the client. */
    private Socket socket;
    /** For sending messages to the client. */
    private PrintWriter clientOut;
    /** Reference to the chat server. */
    private ChatServer server;
    
    /**
     * Constructs a new client thread object.
     * @param server Reference to the chat server.
     * @param socket The socket on which the connection will be established.
     */
    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }
    
    /**
     * Gets the print writer object.
     * @return the print writer that sends messages to the client.
     */
    private PrintWriter getWriter(){
        return clientOut;
    }

    @Override
    public void run() {
        try{
            // setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) { // For connection errors.
            e.printStackTrace();
        }
    }
}
