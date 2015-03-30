package clientUI.controller;

import clientUI.Client;
import clientUI.model.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by rconnesson on 28/03/15.
 */
public class MultiCastClientNetworkController extends AbstractClientNetworkController {

    MulticastSocket multicastSocket;

    @Override
    public void run() {
        try{
            multicastSocket = new MulticastSocket(this.getClient().getPort());
            multicastSocket.joinGroup(this.getClient().getInetAddress());

            while(true){
                byte[] bbuf = new byte[2000];
                DatagramPacket messagePkt  = new DatagramPacket(bbuf, bbuf.length);
                multicastSocket.receive(messagePkt);
                receiveNewMessage( new String(messagePkt.getData()).trim());
            }


        }catch (Exception e){

        }

    }

    public void sendNewMessage(String message) {
        DatagramPacket messagePkt = new DatagramPacket(message.getBytes(),message.length(), getClient().getInetAddress(), getClient().getPort());
        try {
            multicastSocket.send(messagePkt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveNewMessage(String message) {
        getClient().getMessageData().add(0, new Message(message));
    }


}
