package blockchain.edward;
/**
 * This class storages the server's parameters including boolean state, logIndex, commitIndex and etc.
 * For building a real blockchain system, these variables should be stored into the disk.
 * As for this demo, these parameters are saving on the memory.
 * 
 * @author zhanggengrui
 *
 */
public class LocalState{

	private static int event = SystemParameter.EVENT_MAX;
	
	public long [] time_start = new long[event];
	private long [] time_end = new long[event];
	private int currentLocalLogIndex = 0;
	private int currentLocalCommitIndex = 0;
	private int localTermPointer = 1;
	private String log;
	private boolean master = false;
	
	public LocalState(){
		
	}
	
	public void setStateToMaster(){
		master = true;
	}
	
	public void setStateToNOTMater(){
		master = false;
	}
	
	public boolean getStateOfMaster(){
		return master;
	}
	
	//----------------Log State-------------------
	
	public void setlogmessage(String log) {
		this.log = log;
	}
	public void setLocalLogIndexs(int logIndex){

		currentLocalLogIndex = logIndex;
	}
	
	public int getCurrentLocalLogIndex(){
		return currentLocalLogIndex;
	}
	
	//------------------------------------------------
	
	
	//--------------------Commit State----------------------
	
	public void setLocalCommitIndex(int commitIndex){
		currentLocalCommitIndex = commitIndex;
	}
	
	public int getCurrentLocalCommitIndex(){
		return currentLocalCommitIndex;
	}
	
	//-------------------------------------------------------
	
	//---------------------Term State------------------------
	
	public void increaseLocalTerm(){
		localTermPointer ++;
	}
	
	public void setCurrentLocalTerm(int term){
		localTermPointer = term;
	}
	
	public int getCurrentLoalTerm(){
		return localTermPointer;
	}
	
	//-----------------------------------------------------------
	
	//---------------------Local State------------------------
	
	public String getlogmessage() {
		return log;
	}
	
	public void time_start(int commitId, long time_start) {
		this.time_start[commitId]= time_start;
	}
	public long get_start_time(int commitId) {
		return time_start[commitId];
	}
	public void time_end(int commitId, long time_end) {
		this.time_end[commitId] = time_end;
	}
	
	public long get_end_time(int commitId) {
		return time_end[commitId];
	}
	
	//-----------------------------------------------------------------
	

}

