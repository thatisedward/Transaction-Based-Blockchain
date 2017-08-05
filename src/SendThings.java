package blockchain.edward;

/**
 * This class defines the different types of the message which interacts between the servers in this blockchian.
 * 
 * @author Edward Zhang
 *
 */
public class SendThings {

	private String logEntry;
	private String logReply;
	private String commitEntry;
	private String commitBack;
	private String heartBeat;
	private String voteRequest;
	private String voteTicket;
	
	public SendThings(){
		
	}
	
	public String logEntry(int leaderLogIndex, int commitID, int leaderTerm, String record){
		
		logEntry = "LogEntry-"+leaderLogIndex + "-" + commitID + "-" + leaderTerm + "-" + record + "-";
		return logEntry;
	}
	
	public String logReply(int myLogIndex, int commitID, int myTerm){
		
		logReply = "LogReply-"+ myLogIndex + "-" + commitID + "-" + myTerm  + "-";	
		return logReply;
	}
	
	public String commitEntry(int commitID, int leaderTerm, String record){
		
		commitEntry = "CommitEntry-"+ commitID + "-"+ leaderTerm +"-"+ record +"-" ;
		return commitEntry;
	}
	
	public String commitBack(int commitID, int myTerm){
		
		commitBack = "CommitBack-" + commitID + "-" + myTerm + "-";	
		return commitBack;
	}
	
	public String heartBeat(int leaderLogIndex, int leaderTerm){
		
		heartBeat = "HeartBeat-" + leaderLogIndex +"-"+ leaderTerm +"-";
		return heartBeat;
	}
	
	public String voteRequest(int myLogIndex, int myTerm){
		
		voteRequest = "VoteRequest-"+ myLogIndex +"-"+ myTerm +"-";
		return voteRequest;
	}
	
	public String voteTicket(int myTerm, boolean boo){
		
		voteTicket = "VoteTicket-" + myTerm +"-"+ boo + "-";
		return voteTicket;
	}

}
