import java.net.InetAddress;

/**
 * Created by rconnesson on 23/03/15.
 */
public abstract class AbstractMultichatServer extends Thread implements MultichatServer{
    InetAddress inetAddress;
    int port;

    public AbstractMultichatServer(InetAddress ina, int p){
        inetAddress = ina;
        port = p;
    }
}
