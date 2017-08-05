package blockchain.edward;
/**
 * Receive thread will be launched as soon as the thread is started and given the socket number.
 * 
 * @author Edward Zhang
 */
import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
public class ReceiveThread extends Thread{
	
	private DatagramSocket receiveSocket;	
	private AnalyzeThings analyst;
	
	public ReceiveThread(int receiveSocketPortNumber, AnalyzeThings analyst) throws IOException{
	
		receiveSocket = new DatagramSocket(receiveSocketPortNumber);
		this.analyst = analyst;
	}

	public void run(){

		while(true){
			try {
				receivingMessage();
			} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void receivingMessage() throws IOException, InterruptedException{
		
		byte[] receiveData = new byte[10240]; 
		
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		receiveSocket.receive(receivePacket);
		
		String message = new String(receivePacket.getData());	
		int port = receivePacket.getPort();

		analyst.analyze(message, port);
		System.out.println("-->" + message);
	}



}
