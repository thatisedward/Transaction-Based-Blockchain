package blockchain.edward;
/**
 * @author Edward Zhang
 * 
 * This blockchain demo simulates the operation of the multi-node block chain by implementing the consensus algorithm and storage strategy.
 * Based on multi-thread, StatesChange determines whether the running server can change its state of leader, follower and candidate.
 * Each server runs only one state at any given time and the Timer will count down when the state is follower and candidate.
 * 
 * The problem confuses me is the balance between the peak performance and the latency. Hope it will be solved on the next version.
 * The notes are given to the specific lines and the beginning part of each class.
 * Good luck and welcome to this project.
 * 
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class MyBlockChain {
	
	public static void main(String args[] ) throws IOException, InterruptedException{
		
		FileOutputStream out = new FileOutputStream(new File("timelog.txt"));
		FileOutputStream commit = new FileOutputStream(new File("commit.txt"));
		
		PrintStream pointer_timelog = new PrintStream(out);
		PrintStream pointer_commit = new PrintStream(commit);
		
		new SystemParameter();
		
		SendThread sendthread = new SendThread(SystemParameter.SERVER_SENDSOCKET_NUMBER[0]);
		
		SendThings send = new SendThings();
		LocalState state = new LocalState();
		AnalyzeThings analyst = new AnalyzeThings(state, send, sendthread, pointer_timelog, pointer_commit);
		
		Timer timer = new Timer(state, sendthread, send);
		timer.start();
		
		StatesChange change = new StatesChange(state, timer, sendthread, send);
		change.start();
		
		ReceiveThread receivethread = new ReceiveThread(SystemParameter.SERVER_RECEIVESOCKET_NUMBER[0], analyst);
		receivethread.start();

		FileOutputStream createtxt = new FileOutputStream(new File("message.txt"));
		
		@SuppressWarnings("resource")
		PrintStream p_c = new PrintStream(createtxt);
		
		//Imitating the record message of 1k
		for(long i = 0; i < 1024; i++) {
			p_c.print("a");
		}
	}
	
}
