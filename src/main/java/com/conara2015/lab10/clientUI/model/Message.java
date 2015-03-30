package clientUI.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by rconnesson on 27/03/15.
 */
public class Message {

    private final StringProperty message;

    public Message(String msg){
        this.message = new SimpleStringProperty(msg);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }
}
