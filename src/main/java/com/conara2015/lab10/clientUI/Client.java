package clientUI;

import clientUI.controller.AbstractClientNetworkController;
import clientUI.controller.ClientNetworkController;
import clientUI.controller.MultiCastClientNetworkController;
import clientUI.controller.SocketClientNetworkController;
import clientUI.model.Message;
import clientUI.view.ChatWindowController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * Created by rconnesson on 27/03/15.
 */
public class Client extends Application{

    InetAddress inetAddress;
    int port;
    boolean multicast;

    private ObservableList<Message> messageData = FXCollections.observableArrayList();
    AnchorPane layout;

    private Stage primaryStage;


    public AbstractClientNetworkController cnc;

    public Client(){

    }

    @Override
    public void start(Stage primaryStage){
        Parameters param = getParameters();
        if(param != null) {
            try {
                inetAddress = InetAddress.getByName(param.getRaw().get(0));
                port = Integer.parseInt(param.getRaw().get(1));
                multicast = Boolean.getBoolean(param.getRaw().get(2));
                //Initialization du controlleur.
                if( multicast ) new MultiCastClientNetworkController();
                else cnc = new SocketClientNetworkController();
                cnc.setClient(this);
                Thread t = new Thread(cnc);
                t.setDaemon(true);
                t.start();
            } catch (UnknownHostException e) {
                messageData.add(new Message("SERVER /!\\ "+e.getMessage()));
            }

        } else System.out.println("NO PARAM");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MyChatClient");

        initLayout();
    }

    private void initLayout(){
        try{
            // LOAD LAYOUT
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource("/view/ChatWindow.fxml"));
            layout = (AnchorPane) loader.load();

            // SHOW LAYOUT
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();

            ChatWindowController controller = loader.getController();
            controller.setClient(this);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public ObservableList<Message> getMessageData() {
        return messageData;
    }

    public ClientNetworkController getClientNetworkController() { return cnc; }


}
