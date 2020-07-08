package simModel;

import simulationModelling.*;


public class ThemeParks extends AOSimulationModel {

    /* Parameters */
    int numTrains;
    int[] numCars;
    int loadAndUnloadingOption;
    boolean alternateLogic;



    /* Create entity data structures */
    protected Train[] rgTrain;
    protected Stations[] qStations;
    protected TrainTrack[] qTrainTrack;


    /* Create RVP, DVP, UDP, Output */

    protected Output output = new Output();

    //Output values
    public double[] trainUtilization() { return output.trainUtilization(); }

    public double getPercent(int type) { return output.getPercentage(type); }

    // Constructor
    public ThemeParks(double t0time, double tftime, boolean altLogic, int numSideDoors, Seeds sd, int numTrains, int[] numCars, boolean log) {

		// Initiliase classes with model reference
		initialiseClasses(sd);
        // Initialise parameters here

        // For turning on logging
        logFlag = log;
        this.numCars = numCars;
        this.numTrains = numTrains;
        this.alternateLogic = altLogic;
        this.loadAndUnloadingOption = numSideDoors;
        this.rgTrain = new Train[numTrains];
        this.qTrainTrack = new TrainTrack[4];
        this.qStations = new Stations[4];
        closingTime = tftime; // record the closing time - see
        // implicitStopCondition




        // Initialise the simulation model
        initAOSimulModel(t0time, tftime+30); // set stop condition to ensure SBL is not empty.

        // Schedule the first arrivals
        Initialise init = new Initialise();
        scheduleAction(init);  // Should always be first one scheduled.
        // Schedule other scheduled actions and acitvities here
        PassengerArrivalsFG pArrFG = new PassengerArrivalsFG();
        scheduleAction(pArrFG);
        PassengerArrivalsGI pArrGI = new PassengerArrivalsGI();
        scheduleAction(pArrGI);
        PassengerArrivalsRC pArrRC = new PassengerArrivalsRC();
        scheduleAction(pArrRC);
        PassengerArrivalsSH pArrSH = new PassengerArrivalsSH();
        scheduleAction(pArrSH);

    }

	void initialiseClasses(Seeds sd)
	{
		Initialise.model = this;
		Output.model = this;
		Travel.model = this;
		PassengerArrivalsFG.model = this;
		PassengerArrivalsGI.model = this;
		PassengerArrivalsRC.model = this;
		PassengerArrivalsSH.model = this;
		BoardingUnboarding.model = this;
        PassengerArrivalsFG.initRVPs(sd);
        PassengerArrivalsSH.initRVPs(sd);
        PassengerArrivalsRC.initRVPs(sd);
        PassengerArrivalsGI.initRVPs(sd);

	}



    /************  Implementation of Data Modules***********/
    /*
     * Testing preconditions
     */
    public void testPreconditions(Behaviour behObj) {

        reschedule(behObj);
		// Check Activity Preconditions
		while (scanPreconditions() == true);
    }

    private Boolean scanPreconditions(){
		boolean statusChanged = false;

		if (BoardingUnboarding.precondition() == true) {
            BoardingUnboarding bu = new BoardingUnboarding();
            bu.startingEvent();
            scheduleActivity(bu);
            statusChanged = true;
        }
		if(statusChanged) scanInterruptPreconditions();
		else statusChanged = scanInterruptPreconditions();

		return(statusChanged);

	}

	private boolean scanInterruptPreconditions(){
		int num; // number of entries in list
		int interruptionNum;  // interruption number
		SBNotice nt;
		Behaviour obj;
		boolean statusChanged = false;

		num = esbl.size();
		for(int i = 0; i < num ; i++)
		{
			nt = esbl.get(i);
			obj = (esbl.get(i)).behaviourInstance;
			if(ExtConditionalActivity.class.isInstance(obj))
			{
				ExtConditionalActivity extCondObj = (ExtConditionalActivity) nt.behaviourInstance;
				interruptionNum = extCondObj.interruptionPreCond();
				if(interruptionNum != 0)
				{
					extCondObj.interruptionSCS(interruptionNum);
					statusChanged = true;
					unscheduleBehaviour(nt);
					i--; num--; // removed an entry, num decremented by 1 and need to look at same index for next loop
				}
			}
		}
		return(statusChanged);
		}

        protected double closingTime;
		public boolean implicitStopCondition(){
        boolean retval = false;
        int totalNUmPassenger = 0;
        for(int trainid = 0 ; trainid < numTrains ; trainid++){
            for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++)
                totalNUmPassenger += rgTrain[trainid].numPassengers[stid];
        }
        if(getClock() >= closingTime && totalNUmPassenger == 0)
            retval = true;
        return retval;
        }


    public void eventOccured() {
		if (logFlag)
		    printDebug();

    }

    // Standard Procedure to start Sequel Activities with no parameters
    protected void spStart(SequelActivity seqAct) {
        seqAct.startingEvent();
        scheduleActivity(seqAct);
    }


    boolean logFlag = false;
    protected void printDebug(){
        System.out.println("Clock: "+ getClock()+"\nnumTrains = "+ numTrains);
        System.out.println("\t\t\t<------------------------------>\t\t\t");
        trainTrace();
        System.out.println("\t\t\t<------------------------------>\t\t\t");
        stationTrace();
        System.out.println("\t\t\t<------------------------------>\t\t\t");
        trainTrackTrace();

        showSBL();
        showESBL();
    }

    //FUNCTIONS FOR PRINTING
    public void trainTrace(){
		    for(int tid = 0 ; tid < numTrains ; tid++){
                System.out.println("RG.Trains[tid = "+tid+"].Capacity = "+ rgTrain[tid].capacity +
                        "\nRG.Trains[tid = "+tid+"].numCars = "+ numCars[tid]+
                        "\nRG.Trains[tid = "+tid+"].numPassenger = "+getSumOfNumPassenger(tid)+
                        "\nRG.Trains[tid = "+tid+"].boarding = "+rgTrain[tid].boarding);
                System.out.println("----------------------");

            }
    }
    public void stationTrace(){
		    for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++){
                System.out.println(" Q.Stations[stid = "+stid+"].n = "+qStations[stid].n);
            }
    }
    public void trainTrackTrace(){
        for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++){
            System.out.println(" Q.TrainTrack[stid = "+stid+"].n = "+qTrainTrack[stid].getN());
        }
    }
    public int getSumOfNumPassenger(int tid){
        int totalNumPassenger = 0;
        for(int stid = Constants.FROG ; stid <= Constants.RACCOON ; stid++)
            totalNumPassenger += rgTrain[tid].numPassengers[stid];

            return totalNumPassenger;


    }


}
