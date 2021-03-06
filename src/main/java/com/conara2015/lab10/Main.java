import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import clientUI.Client;
import gnu.getopt.*;
import javafx.stage.Stage;

/**
 * Created by rconnesson on 16/03/15
 * @TODO : envoyer meesaage au serveur
 */

public class Main {

    final static String ipRegex = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    public static void main(String[] args){

        AbstractMultichatServer mcs = null;

        String arg;
        Integer port = -1;
        boolean multicast = false;
        boolean server = false;
        boolean nio = false;
	boolean quit=false;
        InetAddress ina = null;


        LongOpt[] longOpts = new LongOpt[7];
        StringBuffer sb = new StringBuffer();
        longOpts[0] = new LongOpt("address" , LongOpt.REQUIRED_ARGUMENT, sb, 'a');
        longOpts[1] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longOpts[2] = new LongOpt("nio", LongOpt.NO_ARGUMENT, null, 'n');
        longOpts[3] = new LongOpt("port", LongOpt.REQUIRED_ARGUMENT, sb , 'p');
        longOpts[4] = new LongOpt("server", LongOpt.NO_ARGUMENT, null, 's');
        longOpts[5] = new LongOpt("multicast", LongOpt.NO_ARGUMENT,null, 'm');
        longOpts[6] = new LongOpt("debug", LongOpt.NO_ARGUMENT, null, 'd');

        Getopt g = new Getopt("lab8nio", args, "ha:p:nsmd",longOpts);

        int c;

        while ( ( c = g.getopt()) != -1){
            switch (c) {
                case 'a' :
                    arg = g.getOptarg();
                    if(  (ina = getINAfromIPString(arg)) == null) {
                        System.out.println("ERROR ILLEGAL IPv4 ADDRESS");
                        System.exit(-1);
                    }
                    break;
                    //
                case 'h' :
                        System.out.println("*****HELP******\n" +
                                "[-h] -a address -p port [n]s|c\n" +
                                "-a,\t--address    \tset the IP address\n" +
                                "-h,\t--help       \tprintt this help\n" +
                                "-n,\t--nio        \tuse NIOs for the server\n" +
                                "-p,\t--port=PORT  \tset the port\n" +
                                "-s,\t--server     \tstart the server\n" +
                                "-c,\t--client     \tstart as client\n" +
                                "-m,\t--multicast  \tstart as client in multicast mode\n" +
                                "**************\n");
			quit = true;
                    break;
                    //
                case 'n' :
                    nio = true;
                    break;
                    //
                case 'p' :
                    arg = g.getOptarg();
                    if( (port = getPORTfromString(arg)) == -1 ) System.exit(-1);
                    break;
                    //
                case 's' :
                    server = true;
                    break;
                    //
                case 'd' :
                    System.out.println("Not disponnible.");
                    break;
                    //
                case 'm' :
                    multicast = true;
                    break;
                    //
                default:
                    break;
                    //
            }
        }
            Scanner sc = new Scanner(System.in);
            String str = null;
	if( ! quit ){
            if (ina == null){
                do {
                    System.out.println("Enter a valid ip address. : ");
                    str = sc.nextLine();
                }while( (ina = getINAfromIPString(str)) == null);
            }

            if (port == -1){
                do {
                    System.out.println("Enter a valid port. : ");
                    str = sc.nextLine();
                }while ( (port =  getPORTfromString(str)) == -1 );
            }

            if(server){
                if(nio){
                    mcs = new MyNioChatServer(ina,port);
                }else{
                    mcs = new MyThreadChatServer(ina, port);
                }
                mcs.start();
            }else{
                String[] param = new String[3];
                param[0] = ina.getHostAddress();
                param[1] = (new Integer(port)).toString();
                param[2] = (new Boolean(multicast)).toString();
                javafx.application.Application.launch(Client.class, param);
            }
	}
    }

    private static InetAddress getINAfromIPString(String ip) {
        InetAddress ina;
        if (ip.matches(ipRegex)) {
            try {
                ina = InetAddress.getByName(ip);
                return ina;
            } catch (UnknownHostException e) {
                System.out.println("IP address : " + e.getMessage());
                ina = null;
                return ina;
            }
        } else {
            ina = null;
            System.out.println("ILLEGAL IP ADDRESS : exit.");
            return ina;
        }
    }

    private static Integer getPORTfromString(String p){
        Integer port;
        try {
            port = Integer.parseInt(p);
        }catch (Exception e){
            System.out.println("Bad Port number : UNKNOWN");
            return -1;
        }
        if(!( 0 < port && port < 65535)){
            System.out.println("Bad Port number : "+port);
            return -1;
        }
        return port;
    }
}
