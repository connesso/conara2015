package clientUI.controller;

import clientUI.Client;
import clientUI.model.Message;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by rconnesson on 28/03/15.
 */
public interface ClientNetworkController {

    public void sendNewMessage(String message);

    public void receiveNewMessage(String message);

}
