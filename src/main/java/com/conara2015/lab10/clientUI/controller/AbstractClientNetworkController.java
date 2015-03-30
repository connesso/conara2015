package clientUI.controller;

import clientUI.Client;

/**
 * Created by rconnesson on 28/03/15.
 */
public abstract class AbstractClientNetworkController implements ClientNetworkController,Runnable {

    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AbstractClientNetworkController(){

    }

}
