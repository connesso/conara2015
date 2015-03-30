import java.io.*;
import java.net.Socket;

/**
 * Created by rconnesson on 16/03/15.
 */
public class MyClientThread implements Runnable {

    Socket socket;
    MyThreadChatServer ms;
    BufferedWriter out;
    BufferedReader in;
    String message;
    String nickname;
    private static int cnt = 0;

    public MyClientThread(Socket s, MyThreadChatServer ms){
        this.socket = s;
        this.ms = ms;
        in = null;
        out = null;
        message = "";
        cnt++;
        nickname = "Client "+cnt;
    }

    @Override
    public void run() {
        try {
            String oldNick;
            out = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            out.write("***HELLO ON MY CHAT***\n",0,23);
            out.flush();
            ms.getSocketBufferedWriterMap().put(socket,out);
            while(true) {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                message = in.readLine();

                if(message != null){

                    if(message.startsWith("\\nick ")){
                        oldNick = this.nickname;
                        this.nickname = message.substring(6);
                        message = "*** "+oldNick+" changed his nickname to "+this.nickname+"***\n";

                    }else {
                        message = nickname + " : \"" + message + "\"\n";
                    }

                    ms.broadcast(message);
                    System.out.println(message);
                }else{
                    message = this.nickname + " : <left MYCHATSERVER>\n";
                    ms.broadcast(message);
                    socket.close();
                    System.out.println(message);
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
