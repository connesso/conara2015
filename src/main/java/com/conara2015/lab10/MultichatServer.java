import java.net.InetAddress;

/**
 * Created by rconnesson on 23/03/15.
 */
public interface MultichatServer {

    /**
     * Lance le server
     */
    public void start();

    /**
     * Diffuse le message à tous les utilisateurs connectés.
     * @param message
     */
    public void broadcast(String message);
}
