import java.net.*;
import java.io.*;

public class SumServiceThread extends Thread{
	
	private Socket sumSocket;
	private ServerSocket sumServer;
	private int port;
	private int orderN;
	private boolean stopread = false;
	
	public SumServiceThread(int n){
		
		orderN = n;
		try{
	
			sumServer = new ServerSocket(0);
		}
		catch(Exception e){
			System.out.println("Error:" + e);
		}
		port = sumServer.getLocalPort();
		
	}
	
	public void run(){
	
		try{
		
			sumSocket = sumServer.accept();	
			InputStream inS = sumSocket.getInputStream();
			ObjectInputStream oIn = new ObjectInputStream(inS);
			
			while(!stopread){

				int i = oIn.readInt();
				if(i == 0){
					stopread = true;
				}
				else{
					SumServiceManager.sumArray[orderN] =+ i;
					SumServiceManager.sumCounterArray[orderN]++;
				}
			}
			oIn.close();
			sumSocket.close();
			sumServer.close();
			
		}catch(Exception e){
			System.out.println("Error:" + e);
		}
		
	}
	
	public int getPort(){
		return port;
	}
}