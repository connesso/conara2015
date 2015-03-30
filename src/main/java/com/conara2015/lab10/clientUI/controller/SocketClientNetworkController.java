package clientUI.controller;

import clientUI.Client;
import clientUI.model.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by rconnesson on 28/03/15.
 */
public class SocketClientNetworkController extends AbstractClientNetworkController {

    private boolean established = false;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;
    private Socket socket;

    @Override
    public void run(){
        try {

            socket = new Socket(this.getClient().getInetAddress(),this.getClient().getPort());
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            established = true;

            while (true) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                message = in.readLine();
                if(message!=null){
                    receiveNewMessage(message);
                }
            }
        } catch (IOException e) {
            this.getClient().getMessageData().add(new Message("SERVER /!\\ " + e.getMessage()));
        }
    }

    public void sendNewMessage(String message){
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveNewMessage(String message){
        if(established)this.getClient() .getMessageData().add(0,new Message(message));
    }
}
