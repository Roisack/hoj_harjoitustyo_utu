package hoj_harjoitus_osa1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listens for instructions from a remote client
 * @author Jesse Kaukonen
 */
public class ListenerService extends Thread {
    
    /* Where our socket is listening */
    private int listen_port;
    /* How long we will wait for messages from remote */
    private int timeout;
    /* Address for the listen socket */
    private InetAddress listen_address;
    /* The socket which opens the connection */
    private ServerSocket server_socket;
    /* The socket which handles the connection */
    private Socket listen_socket;
    /* Data buffer for the socket */
    private byte[] buffer;
    /* A flag for stopping the listening service */
    private boolean interrupted;
    /* How many times we will try waiting for timeout duration */
    private int max_attempts = 5;
    

    /* Port at which the work distributor is waiting for initial connections */
    private int remote_port;
    /* Address of the work distibutor */
    private InetAddress remote_address;
    /* Message that is sent to remote to offer our services */
    private String service_banner = "palvelua tarjotaan portissa ";
    
    /**
     * Constructs a new ServiceListener
     * @param p = port which is listened
     * @param t = timeout which the socket waits
     * @param r = the port where we will make the initial connection
     */
    public ListenerService(int p, int t, int r) throws SocketException, UnknownHostException, IOException {
        System.out.println("ListenerService constructing");
        listen_port = p;
        timeout = t;
        remote_port = r;
        buffer = new byte[256];
        interrupted = false;
        
        // By default use localhost
        listen_address = InetAddress.getLocalHost();
        remote_address = InetAddress.getLocalHost();
        service_banner = service_banner + listen_port;
        
        server_socket = new ServerSocket(listen_port);
        server_socket.setSoTimeout(timeout);

        setDaemon(true);
    }
    
    /**
     * Override the Thread run function
     * Start listening for connections at the socket
     * Stop if the manager tells us to stop
     * If 5 attempts to listen have failed, exit
     */ 
    @Override
    public void run() {
        
        // First form a TCP connection to remote
        // If no connection is formed after 5 tries, exit
        int connections_failed = 0;
        boolean connected = false;
        
        InputStream iS = null;
        OutputStream oS = null;
        ObjectInputStream oIn = null;
        ObjectOutputStream oOut = null;
        
        try {
            iS = listen_socket.getInputStream();
            oS = listen_socket.getOutputStream();
            oIn = new ObjectInputStream(iS);
            oOut = new ObjectOutputStream(oS);
        } catch (IOException ex) {
            Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("ListenerService attempting initial connection to " + remote_address + ":" + remote_port);
        
        while (connections_failed < max_attempts && !connected)
        {
            try {
                contact_remote();
                listen_socket = server_socket.accept();
                connected = true;
            } catch (SocketTimeoutException ex) {
                connections_failed++;
                Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            } catch (IOException ex) {
                Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (!connected) {
            System.out.println("Connection failed 5 times, exiting");
            try {
                listen_socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(1);
        }
        
        System.out.println("Connection formned to remote after " + (connections_failed+1) + " tries");
        
        while (!interrupted) {
            try {
                oIn.readInt();
            } catch (InterruptedIOException e) {
                continue;
            } catch (IOException ex) {
                Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            oIn.close();
            oOut.close();
            iS.close();
            oS.close();
        } catch (IOException ex) {
            Logger.getLogger(ListenerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Opens a connection to a work distributor
     * Returns 0 if connection was opened
     * @throws IOException
     */
    public int contact_remote() throws IOException {
        // Use UDP for the connection
        DatagramSocket banner_socket = new DatagramSocket();
        String message_content = service_banner;
        
        DatagramPacket banner_packet = new DatagramPacket(message_content.getBytes(), message_content.length(), remote_address, remote_port);
        banner_socket.send(banner_packet);
        banner_socket.close();
        return 0;
    }
    
    public void stop_listening() {
        interrupted = true;
    }
}
