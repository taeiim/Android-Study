import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/** TCP/IP Server Socket
 *  Network Programming
 *
 * @author Kitkat
 *
 */
public class Server {
    private static final String Naver = "www.naver.com";
    private static ServerSocket serverSocket;
    private static Socket socket;

    public static void main(String[] args) {
        ipAddress();

        try {

            // Instantiate ServerSocket Class
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(2800));


            // Connection Wait for Multiple Client
            while(true) {
                System.out.println("Connetion wait..");
                socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("Connection Accepted! Client  [" + isa.getHostName() + ":" + isa.getPort() + "]");

                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();

                byte[] byteArr = new byte[512];
                String msg = null;

                int readByteCount = is.read(byteArr);

                if(readByteCount == -1)
                    throw new IOException();

                msg = new String(byteArr, 0, readByteCount, "UTF-8");
                System.out.println("Data Received OK!");
                System.out.println("Message : " + msg);

                msg = "Hello Client";
                byteArr = msg.getBytes("UTF-8");
                os.write(byteArr);
                System.out.println("Data Transmitted OK!");
                os.flush();

                is.close();
                os.close();
                socket.close();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try { socket.close(); } catch (IOException e1) { e1.printStackTrace(); }
        }

        if(!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void ipAddress() {
        try {

            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("LocalHost IP Address : " + inetAddress.getHostAddress());

            InetAddress[] iaArr = InetAddress.getAllByName(Naver);

            for(InetAddress ia : iaArr)
                System.out.println(Naver + " IP Address : " + ia.getHostAddress());

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}