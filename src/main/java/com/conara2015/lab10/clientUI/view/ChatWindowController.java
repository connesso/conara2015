package clientUI.view;

import clientUI.Client;
import clientUI.model.Message;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by rconnesson on 27/03/15.
 *
 *
 * @TODO : RECUPER LE TEXTE QUAND LE USER APPUIE SUR ENTER
 *
 *
 */
public class ChatWindowController {
    @FXML
    private TextField sendTextField;

    @FXML
    private TableView chatTable;

    @FXML
    private TableColumn<Message,String> chatColumn;


    private Client client;

    public ChatWindowController(){

    }

    @FXML
    private void initialize() {
        chatColumn.setCellValueFactory(cellData -> cellData.getValue().messageProperty());

        sendTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().toString() == "ENTER"){
                    handleSendMessage();
                }
            }
        });
    }


    public void setClient(Client client) {
        this.client = client;

        chatTable.setItems(client.getMessageData());
    }

    @FXML
    private void handleSendMessage() {
        String message = sendTextField.getText();
        client.getClientNetworkController().sendNewMessage(message);
        sendTextField.setText("");
    }
}