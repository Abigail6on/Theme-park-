package simModel;

import java.util.*;

public class TrainTrack {

	ArrayList<Integer> queue = new ArrayList<Integer>();


	protected void spInsertQueue(int tid) {
		queue.add(tid);
	}
	protected void spRemoveQueue() {queue.remove(0);}
	public int getN(){
		return queue.size();
	}

	int getList(int i){
		return queue.get(i);
	}

}
