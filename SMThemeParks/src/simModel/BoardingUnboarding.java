package simModel;

import simulationModelling.*;

public class BoardingUnboarding extends ExtConditionalActivity {

    static ThemeParks model;
    int stid;
    int tid;



    public static boolean precondition(){
        boolean retval = false;
        if (udpStationBoardingUnboarding() != -1){
            retval = true;
        }
        return retval;
    }

    public void startingEvent() {
        stid = udpStationBoardingUnboarding();
        tid = model.qTrainTrack[stid].getList(0);
        model.rgTrain[tid].boarding = true;
        model.rgTrain[tid].numPassengers[stid] = 0;
    }

    public double duration() {
        return dvpBoardingUnboardingTime();
    }


    @Override
    public int interruptionPreCond() {
        int retVal = 0;


        if ((model.alternateLogic == true && model.qTrainTrack[stid].getN() > 1) || (udpCanInterruptBoardingUnboarding(tid,stid))){
            retVal = 1;
        } return retVal;
    }


    @Override
    public void interruptionSCS(int id) {
        // Only one id valid possible 1
        udpBoardTrain(tid,stid);
        // Sequel Travel activity
        Travel trvl = new Travel(tid, stid);
        model.spStart(trvl);
        model.rgTrain[tid].boarding = false;

    }

    public void terminatingEvent() {
        udpBoardTrain(tid,stid);

        // Sequel Travel activity
        Travel trvl = new Travel(tid, stid);
        model.spStart(trvl);
        model.rgTrain[tid].boarding = false;

    }


    protected static int udpStationBoardingUnboarding(){
        for(int stid = Constants.FROG; stid <= Constants.RACCOON ; stid++){

            if(model.qTrainTrack[stid].getN()!= 0){
                if (((model.rgTrain[model.qTrainTrack[stid].getList(0)]).boarding) == false){
                    return stid;
                }

            }
        }


        return -1;
    }

    protected double dvpBoardingUnboardingTime() {
        if (model.loadAndUnloadingOption == 0) {
            return 1.25;
        } else if (model.loadAndUnloadingOption == 1) {
            return 2.00;
        } else {
            return 2.5; // Train waiting policy is enabled then
        }

    }

    protected void udpBoardTrain(int trainId, int stnId) {
        double[][] percentUnBoard = {
                {0.0, 0.4, 0.35, 0.25},
                {0.37, 0.0, 0.39, 0.24},
                {0.42, 0.29, 0.0, 0.29},
                {0.41, 0.28, 0.31, 0.0}};

        int totalNumPassenger = 0;
        for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++)
            totalNumPassenger += model.rgTrain[trainId].numPassengers[stid];

        int numCanBoard = model.rgTrain[trainId].capacity - totalNumPassenger;
        if (numCanBoard > model.qStations[stnId].n) {
            numCanBoard = model.qStations[stnId].n;
        }

        model.qStations[stnId].n -= numCanBoard;

//      update the entities
        for (int destStnId = Constants.FROG; destStnId <= Constants.RACCOON; destStnId++) {

//            model.rgTrain[trainId].numPassengers[destStnId] += Math.ceil(percentUnBoard[stnId][destStnId] * numCanBoard);
            model.rgTrain[trainId].numPassengers[destStnId] += percentUnBoard[stnId][destStnId] * numCanBoard;
        }

    }

    protected boolean udpCanInterruptBoardingUnboarding(int trainId, int stId){
        int totalNumPassengers = 0;
        int spaceOnTrain;
        boolean interrupt = false;
        for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++)
            totalNumPassengers += model.rgTrain[trainId].numPassengers[stid];
        spaceOnTrain = model.rgTrain[trainId].capacity - totalNumPassengers;
        if(spaceOnTrain == model.qStations[stId].n)
            interrupt = true;
        return interrupt;
    }




}




