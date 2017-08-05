package blockchain.edward;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SystemParameter{
	private static int timer_clock;
	private static int total_node_number;
	private static int this_node_number;
	private static int max_event;
	
	public final static String[] SERVER_NAME = new String[16];
	public final static int[] SERVER_SENDSOCKET_NUMBER = new int[16];
	public final static int[] SERVER_RECEIVESOCKET_NUMBER = new int[16];
	
	private void readSettings() throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader("Settings"));
		String record = reader.readLine();
		String[] names = new String[16];
		int[] send_port = new int[16];
		int[] receive_port = new int[16];
		
		int count_name = 0;
		int count_port = 0;
		
		while(record != "End") {
			String[] receiving = reader.readLine().split(" ");
			
			switch (receiving[0]) {
				case "Node_number":
					total_node_number = Integer.parseInt(receiving[1]);
					break;
				case "Clock_time":
					timer_clock = Integer.parseInt(receiving[1]);
					break;
				case "This_node_nubmer":
					this_node_number = Integer.parseInt(receiving[1]);
					break;
				case "Maximum_event_number":
					max_event = Integer.parseInt(receiving[1]);
					break;
				case "*":
					SERVER_NAME[count_name] = names[count_name] = receiving[1];
					count_name++;
					break;
				case "%":
					SERVER_SENDSOCKET_NUMBER[count_port] = send_port[count_port] = Integer.parseInt(receiving[1]);
					SERVER_RECEIVESOCKET_NUMBER[count_port] = receive_port[count_port] = Integer.parseInt(receiving[2]);
					count_port ++;
					break;
			}
		}
	}

	public final static int TIMER_CLOCKTIME = timer_clock;
	public final static int TOTAL_NODENUMBER = total_node_number;
	public final static int THIS_NODE_NUMBER = this_node_number;
	public final static int EVENT_MAX = max_event;
	
}
