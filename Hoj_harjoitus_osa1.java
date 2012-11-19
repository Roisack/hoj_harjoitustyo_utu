package hoj_harjoitus_osa1;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class launches the ServiceManager and handles command line arguments
 * @author Jesse Kaukonen
 */
public class Hoj_harjoitus_osa1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("hello");
        
        ServiceManager sm = new ServiceManager();
        
         // Try to initialize the listener service
        try {
             if (sm.start_listener_service() < 0) {
                 System.exit(1);
             }
        } catch (SocketException ex) {
            Logger.getLogger(Hoj_harjoitus_osa1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Hoj_harjoitus_osa1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Hoj_harjoitus_osa1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
