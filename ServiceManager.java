package hoj_harjoitus_osa1;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Launches service threads and handles communication to a remote work distributor
 * @author gekko
 */
public class ServiceManager {

    /* A listener which handles messages from the remote */
    private ListenerService listenerService;
    
    /**
     * Constructs the ServiceManager which will be managing the listener service
     */
    public ServiceManager() {
    }
    
    public static SummingService create_summing_service(int port) {
        System.out.println("ServiceManager creating a new SummingService");
        SummingService s = new SummingService(port);
        return s;
    }
    
    /**
     * Constructs a ListenerService and orders it to contact the remote
     * If the connection was opened, then begin listening
     * Returns negative if failed
     * Returns 0 if success
     */
    public int start_listener_service() throws SocketException, UnknownHostException, IOException {
        System.out.println("ServiceManager constructing a new ListenerService");
        listenerService = new ListenerService(3333, 5000, 3126);
        listenerService.start();
        
        return 0;
    }
}
