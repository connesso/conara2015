import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rconnesson on 16/03/15.
 */
public class MyThreadChatServer extends AbstractMultichatServer{

    ServerSocket serverSocket;
    ArrayList<Thread> threadArrayList;
    private Map<Socket,BufferedWriter> socketBufferedWriterMap;


    public MyThreadChatServer(InetAddress ina, int p){
        super(ina,p);
        this.socketBufferedWriterMap = new HashMap<Socket, BufferedWriter>();
    }

    public Map<Socket,BufferedWriter> getSocketBufferedWriterMap(){return socketBufferedWriterMap;}

    public void start(){
        System.out.println("Server : mode MultiThread : address "+inetAddress.getHostAddress()+" port "+port);
        try {
            serverSocket = new ServerSocket(port, 5, inetAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket client = serverSocket.accept();
                if(client.isConnected()) {

                    MyClientThread mcc = new MyClientThread(client,this);
                    Thread t = new Thread(mcc);
                    t.setDaemon(true);
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String message){

        socketBufferedWriterMap.keySet().stream().forEach(socket -> {
            try {
                if (!socket.isClosed()) {
                    socketBufferedWriterMap.get(socket).write(message);
                    socketBufferedWriterMap.get(socket).flush();
                }
            } catch (IOException e) {
            }
        });
    }



}
