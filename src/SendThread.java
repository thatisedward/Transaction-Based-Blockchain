package blockchain.edward;

import java.io.IOException;
import java.net.*;

public class SendThread {
	
	private DatagramSocket sendSocket;
	
	private InetAddress [] nodeAddress = new InetAddress[SystemParameter.TOTAL_NODENUMBER];
	private int[] portNumber = new int[SystemParameter.TOTAL_NODENUMBER];
	
	public SendThread(int sendSocketPortNumber) throws SocketException, UnknownHostException{
		
		sendSocket = new DatagramSocket(sendSocketPortNumber);
		
		for(int i = 0; i <SystemParameter.TOTAL_NODENUMBER; i++) {
			nodeAddress[i] = InetAddress.getByName(SystemParameter.SERVER_NAME[i]);
			portNumber[i] = SystemParameter.SERVER_RECEIVESOCKET_NUMBER[i];
		}
	}
	
	public void sendMessage(String messageNeedsToSend) throws IOException{
				
		byte[] sendData = new byte[1024];
		
		sendData = messageNeedsToSend.getBytes();
		
		for(int i = 0; i < SystemParameter.TOTAL_NODENUMBER; i ++){
			
			if(i == (SystemParameter.THIS_NODE_NUMBER - 1))
				continue;
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, nodeAddress[i], portNumber[i]);
			sendSocket.send(sendPacket);
			
		}
	}
	
	public void sendToSpecialDestination(int specialPort, String message) throws IOException{
		
		byte[] sendData = new byte[1024];

		sendData = message.getBytes();
		
		for(int i = 0; i <SystemParameter.TOTAL_NODENUMBER; i++) {
			
			if(specialPort == portNumber[i]) {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, nodeAddress[i], specialPort);
				sendSocket.send(sendPacket);
				break;
			}
		}
	}
	
	public boolean closeSendSocket(){
		sendSocket.close();
		return false;
	}
	

}
