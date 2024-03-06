package client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Thread that handles communication with a client.
 * @author Zac Tawfick
 */
public class ServerThread implements Runnable {
	/** Socket used for connection. */
    private Socket socket;
    /** The username of the client. */
    private String userName;
    /** Messages that need to be sent to the client. */
    private final LinkedList<String> messagesToSend;
    /** Indicator of whether there are messages to be sent or not. */
    private boolean hasMessages = false;
    
    /**
     * Constructs a new server thread object.
     * @param socket connection to the client.
     * @param userName the username of the client.
     */
    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }
    
    /**
     * Adds a message to the list of messages that need to be sent.
     * @param message the message to be added to the list.
     */
    public void addNextMessage(String message){
        synchronized (messagesToSend){ 
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    public void run(){
        System.out.println("Welcome :" + userName);

        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
            }
            serverIn.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
}