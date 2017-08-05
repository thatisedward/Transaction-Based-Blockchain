package blockchain.edward;

import java.io.IOException;
import java.util.*;  
import java.util.concurrent.*;  
  
public class Timer extends Thread{  
    
	private int limitSec = SystemParameter.TIMER_CLOCKTIME;
    private boolean boo = true;
    private LocalState state;
    
    private SendThread sendThread;
    private SendThings send;
    
    public Timer(LocalState state, SendThread sendThread, SendThings send) {  
    	
    		this.state=state;
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
        	  
	        	  while(limitSec > 0){  
	        		  
	        		  System.out.println("remains "+ --limitSec +" s");  
	        		  
	        		  if(state.getStateOfMaster() == true){
	        			  
	            		  /**
	            		   * 
	            		   * The following part is aiming to send heartbeat message to follower,
	            		   * Sending heartbeat is the way to maintain the leader's liveness when 
	            		   * there is no record needed to be committed.
	            		   * 
	            		   * We are not using this function when dealing with the peak performance.
	            		   */
	        			  
	        			  /*
	        			   * sendingHeartBeat();
	        			   */
	        			  
	            		  resetClock();
	            	  }
	        	  }
	          
	        	  boo = false;
	        	  
	          }
    }
   
    public void resetClock(){
    	
    		limitSec = SystemParameter.TIMER_CLOCKTIME;
    }
    
    public synchronized void resetBoo(){
    		boo = true;
    }
    
    public boolean getBoolean(){
		return boo;

    }
    
    public int getLimitSec(){
    		return limitSec;
    }
    
    public void addClockByManual(int sec){
    		limitSec = limitSec+sec;
    		System.out.println("-->"+sec+"more seconds.");
    }
    
    private void sendingHeartBeat() {
    		
    	int leaderLogIndex = state.getCurrentLocalLogIndex();
		int leaderTerm = state.getCurrentLoalTerm();
		  
		String HB = send.heartBeat(leaderLogIndex, leaderTerm);
		
		try {
			sendThread.sendMessage(HB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("Sent HB to nodes...");
    }	
  
}  