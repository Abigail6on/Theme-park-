package simModel;

import simulationModelling.SequelActivity;

class Travel extends SequelActivity {

	static ThemeParks model;
	int destStid;
	int tid;
	int stid;
	
	public Travel(int tid, int stid) {
		this.tid = tid;
		this.stid = stid;
	}
	
	@Override
	public void startingEvent() {

		udpUpdateTRJ();
		
		if (model.qStations[stid].n == 0) {
			model.output.ssovNumType1 += 1;
		}
		if (model.qStations[stid].n > 0 && model.qStations[stid].n < 25) {
			model.output.ssovNumType2 += 1;
		}
		if (model.qStations[stid].n > 24 && model.qStations[stid].n < 51) {
			model.output.ssovNumType3 += 1;
		}
		if (model.qStations[stid].n > 50) {
			model.output.ssovNumType4 += 1;
		}
		
		destStid = udpGetDestination(stid);
		model.qTrainTrack[stid].spRemoveQueue();
		
	}

	
	public double duration() {
		return dvpUtravelTime(stid);
	}
	
	public void terminatingEvent() {
		model.qTrainTrack[destStid].spInsertQueue(tid);
	}

	protected double dvpUtravelTime(int originID) {
		if (originID == Constants.FROG) {
			return 5.00;
		} else if (originID == Constants.SKUNK) {
			return 8.00;
		} else if (originID == Constants.GATOR) {
			return 7.00;
		} else if (originID == Constants.RACCOON) {
			return 6.00;
		}

		return 50000;
	}

	protected int udpGetDestination(int stnId) {
		if (stnId == Constants.FROG)  return  Constants.SKUNK;
		else if (stnId == Constants.SKUNK)  return  Constants.GATOR;
		else if (stnId == Constants.GATOR)  return  Constants.RACCOON;
		else if (stnId == Constants.RACCOON)  return  Constants.FROG;
		else return stnId;
	}

	//update the trajectory sequences
	protected void udpUpdateTRJ(){
		model.output.updateSequence();
	}



}
