import com.sun.corba.se.spi.activation.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by rconnesson on 23/03/15.
 */
public class MyNioChatServer extends AbstractMultichatServer{

    private Selector select;
    private ServerSocketChannel ssc;
    private ByteBuffer bbuf;
    private ByteBuffer banderoleBuf;
    private int clientCount;

    public MyNioChatServer(InetAddress ina, int p) {
        super(ina,p);
        try {
            ssc = ServerSocketChannel.open();
            select = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(select, SelectionKey.OP_ACCEPT);
            ssc.bind(new InetSocketAddress(this.inetAddress.getHostAddress().toString(), this.port));
            bbuf = ByteBuffer.allocate(8192);
            banderoleBuf = ByteBuffer.wrap("***WELCHOME TO MYCHATSERVER!***\n".getBytes());
            clientCount = 0;
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void whenAccepted(SelectionKey key) throws IOException {

        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();

        client.configureBlocking(false);

        clientCount++;

        client.register(select, SelectionKey.OP_READ).attach("Client" + clientCount);

        client.write(banderoleBuf);
        banderoleBuf.rewind();
    }

    private void whenRead(SelectionKey key) throws IOException {
        ((SocketChannel) key.channel()).read(bbuf);
        Charset charset = Charset.defaultCharset();

        String message = (String) key.attachment();

        bbuf.flip();
        CharBuffer cbuf = charset.decode(bbuf);
        bbuf.compact();

        if(cbuf.hashCode() == 1){
            message += "<left MYCHATSERVER>\n";
            key.channel().close();
        }else if(cbuf.toString().startsWith("\\nick ")){
            key.attach(cbuf.toString().substring(6).replace('\n',' '));
            message += "< changes his nickname to "+key.attachment()+">\n";
        }else{
            message += ": " + cbuf;
        }

        broadcast(message);
        System.out.print(message);

    }

    public void broadcast(String message){
        ByteBuffer m =ByteBuffer.wrap(message.getBytes());
        select.keys().stream().
                filter(key -> key.isValid() && key.channel() instanceof SocketChannel).
                forEach(key -> {
                    try {
                        ((SocketChannel) key.channel()).write(m);
                        m.rewind();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public void start(){
        System.out.println("Server : mode NIO : address "+inetAddress.getHostAddress()+" port "+port);
        try {
            while(ssc.isOpen()){
                    select.select();
                    Set<SelectionKey> keys = select.selectedKeys();
                    Iterator<SelectionKey> keyIterator = keys.iterator();
                    while (keyIterator.hasNext()){

                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()) {
                            whenAccepted(key);
                        }
                        if (key.isReadable()) {
                            whenRead(key);
                        }

                        keyIterator.remove();

                    }
                }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
