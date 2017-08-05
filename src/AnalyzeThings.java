package blockchain.edward;
/**
 * This class is implemented to distinguish the types of message which were received in the receiveThread.
 * After analyzing the types, the corresponding actions will be taken place if the parameters are legal.
 * 
 * @author Edward Zhang
 */
import java.io.*;

public class AnalyzeThings {

	private LocalState state;
	private SendThings send;
	private SendThread sendThread;
	
	private int amount = SystemParameter.EVENT_MAX;
	
	private int[] counter_log = new int[amount];
	private int[] counter_commit = new int[amount];
	private int[] counter_ticket = new int[amount];
	private boolean[] switch_off = new boolean[amount];
	
	private String log;
	
	PrintStream pointer_time, pointer_commit;
	
	public AnalyzeThings(LocalState state, SendThings send, SendThread sendThread, PrintStream pointer_time, PrintStream pointer_commit){
		this.state = state;
		this.send = send;
		this.sendThread = sendThread;
		this.pointer_time = pointer_time;
		this.pointer_commit = pointer_commit;
	}
	
	public void analyze(String receivedMessage, int port){
		
		String[] splitMessage = receivedMessage.split("-");
		
		if(state.getStateOfMaster()) {
			
			if(splitMessage[0].equals("LogReply")){
				
				int myLogIndex = Integer.parseInt(splitMessage[1]);
				int commitID = Integer.parseInt(splitMessage[2]);
				int myTerm = Integer.parseInt(splitMessage[3]);
				
				responseToLogReply(myLogIndex, commitID, myTerm);
			}
			
			if(splitMessage[0].equals("CommitBack")) {

				int commitID = Integer.parseInt(splitMessage[1]);
				int term = Integer.parseInt(splitMessage[2]);			
				
				if(term <= state.getCurrentLoalTerm()) {
					responseToCommitBack(commitID);
				}
			}
		}
		else if (!state.getStateOfMaster()) {
			
			if(splitMessage[0].equals("LogEntry")){

				int leaderLogIndex = Integer.parseInt(splitMessage[1]);
				int commitID = Integer.parseInt(splitMessage[2]);
				int leaderTerm = Integer.parseInt(splitMessage[3]);
				String record = splitMessage[4];
				
				responseToLogEntry(leaderLogIndex, commitID, leaderTerm, record);
			}
			
			if(splitMessage[0].equals("CommitEntry")) {

				int commitId = Integer.parseInt(splitMessage[1]);
				int leaderterm = Integer.parseInt(splitMessage[2]);			
				
				responseToCommitEntry(leaderterm, commitId);
			}
			
			if(splitMessage[0].equals("VoteRequest")) {
				
				int bitch_index = Integer.parseInt(splitMessage[1]);
				int bitch_term = Integer.parseInt(splitMessage[2]);
				
				responseToVoteRequest(bitch_index, bitch_term, port);
			}
			
			if(splitMessage[0].equals("VoteTicket")) {
				
				int its_term = Integer.parseInt(splitMessage[1]);
				
				responseToVoteTicket(its_term);
			}
		}
	}
	
	private void responseToLogReply(int myLogIndex, int commitID, int myTerm) {
		
		if(myTerm > state.getCurrentLoalTerm())
			state.setCurrentLocalTerm(myTerm);
		
		counter_log[commitID]++;
		
		if(counter_log[commitID] >= 1) {
			//state.setLocalCommitIndex(commitID);	
			try {
				sendThread.sendMessage(send.commitEntry(commitID, state.getCurrentLoalTerm(), state.getlogmessage()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void responseToCommitBack(int commitID) {
		
		counter_commit[commitID]++;
		
		if(counter_log[commitID] >= 1) {

			long latency = System.currentTimeMillis()-state.get_start_time(commitID);
			
			try {
				pointer_commit.println(commitID+" "+ new BufferedReader(new FileReader("message.txt")).readLine().toString());
			} catch (IOException e) {
	
				e.printStackTrace();
			}
			
			state.setLocalCommitIndex(commitID);
			System.out.println("Record-"+ commitID + "-has been commited!");
			
			pointer_time.println(commitID + "\t"+ latency);
		}
	}
	
	private void responseToLogEntry(int leaderlogindex, int commitId, int leaderterm, String record) {
		
		if(leaderterm >= state.getCurrentLoalTerm()) {
			state.setCurrentLocalTerm(leaderterm);
			if(leaderlogindex > state.getCurrentLocalLogIndex() && commitId > state.getCurrentLocalCommitIndex()) {
				log = record;
				state.setLocalLogIndexs(leaderlogindex);
				
				try {
					sendThread.sendMessage(send.logReply(state.getCurrentLocalLogIndex(), commitId, state.getCurrentLoalTerm()));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	private void responseToCommitEntry(int leaderterm, int commitId) {
		
		if(leaderterm >= state.getCurrentLoalTerm()) {
			
			pointer_commit.println(commitId+" "+ log);
			
			state.setLocalCommitIndex(commitId);
			
			try {
				sendThread.sendMessage(send.commitBack(commitId, state.getCurrentLoalTerm()));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			//System.out.println("Record-"+ commitId + "-has been committed!");
			
		}
	}
	
	private void responseToVoteRequest(int bitch_index, int bitch_term, int port) {
		
		int my_logindex = state.getCurrentLocalLogIndex();
		int my_term = state.getCurrentLoalTerm();
		
		if(bitch_term > my_term) {
			state.setCurrentLocalTerm(bitch_term);
			
			if(bitch_index >= my_logindex && switch_off[my_term] != true) {
				switch_off[my_term]= true;
				
				try {
					sendThread.sendToSpecialDestination(port, send.voteTicket(my_term, true));
				} catch (IOException e) {
					System.out.println("Fail to send Vote Ticket");;
				}
			}
		}
	}
	
	private void responseToVoteTicket(int its_term) {
		int my_term = state.getCurrentLoalTerm();
		
		if(its_term == my_term)
			counter_ticket[my_term]++;
		else if (its_term> my_term)
			state.setCurrentLocalTerm(its_term);
		
		if(counter_ticket[my_term]> SystemParameter.TOTAL_NODENUMBER/2) {
			try {
				sendThread.sendMessage(send.heartBeat(state.getCurrentLocalLogIndex(), state.getCurrentLoalTerm()));
			} catch (IOException e) {
				System.out.println("Fail to broadcast...");;
			}
			
			state.setStateToMaster();
		}
	}
}
