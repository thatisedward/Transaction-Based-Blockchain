package blockchain.edward;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to change the state according to the LocalState's parameter and the Timer.
 * 
 * @author Edward Zhang
 */
public class StatesChange extends Thread{

	private Timer timer;
	private SendThread sendThread;
	private SendThings send;
	private LocalState state;
	private int times;
	private int n = SystemParameter.EVENT_MAX;
	
	public StatesChange(LocalState state, Timer timer, SendThread sendThread, SendThings send){
				
		this.state = state;
		this.timer = timer;
		this.sendThread = sendThread;
		this.send = send;
	}
	
	public void run(){
		
		while(true){
			
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if(state.getStateOfMaster() == true && times == 1){
				
				/**
				 * When this server's state turns to be a leader/master, this server begins to serving the whole system.
				 * 
				 * We uses disk I/O in order to simulate the real service process to the greatest extent possible,
				 * so we are not using the next part that is a memory I/O (Only for checking the demo). 
				 */
				
				/*
				 * 	for(...){
				 *  		testEntries(k);
				 *  }
				 */
				
				try {
					broadcasting(n);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				times++;
			}
			
			if(timer.getBoolean() == false){
			
				timer.resetClock();
				timer.resetBoo();
				
				state.increaseLocalTerm();
								
				int logIndex = state.getCurrentLocalLogIndex();
				int term = state.getCurrentLoalTerm();
				
				String voteRequest = send.voteRequest(logIndex, term);
				
				try {
					
					sendThread.sendMessage(voteRequest);
					//System.out.println("Send Vote Request!");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void testEntries(int k) throws IOException{
		int logIndex = k;
		int commintID = k;
		String message = k + "th building!";
		int term = state.getCurrentLoalTerm();
		
		state.setLocalLogIndexs(logIndex);
		
		String log = send.logEntry(logIndex, commintID, term, message);		
		sendThread.sendMessage(log);
	}
	
	private void broadcasting(int n) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader("message.txt"));
		String record = reader.readLine();
		
		state.setlogmessage(record);
		
		for(int i = 0; i < n; i ++) {
			state.setLocalLogIndexs(i+1);
			int commitId = state.getCurrentLocalLogIndex()+1;
			
			sendThread.sendMessage(send.logEntry(state.getCurrentLocalLogIndex(), commitId, state.getCurrentLoalTerm(), record));
			
			state.time_start(commitId, System.currentTimeMillis());
			//System.out.println(commitId + "sent.");
			
			//Thread.sleep(1);
			
		}
	}
}
