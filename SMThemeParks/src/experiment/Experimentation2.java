package experiment;

import cern.jet.random.engine.RandomSeedGenerator;
import outputAnalysis.ConfidenceInterval;
import simModel.Seeds;
import simModel.ThemeParks;

public class Experimentation2 {
    public static void main(String[] args) 	{
        int i, NUMRUNS = 40;
        int MAX_NumTrain = 8;
        double startTime=0.0, endTime= 720;  //run for seven days
        Seeds[] sds = new Seeds[NUMRUNS];
        ThemeParks thmPrk;  // Simulation object
        double[] valPercentType1 = new double[NUMRUNS],
                valPercentType2 = new double[NUMRUNS],
                valPercentType3 = new double[NUMRUNS],
                valPercentType4 = new double[NUMRUNS];
        double[][] valTrainUtil = new double[NUMRUNS][MAX_NumTrain];
        ConfidenceInterval ciPercentType1, ciPercentType2, ciPercentType3, ciPercentType4;





        // Lets get a set of uncorrelated seeds
        RandomSeedGenerator rsg = new RandomSeedGenerator();
        for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);


        // Loop for NUMRUN simulation runs for each case
        // Case 1
        int NUM_INITIAL_CARS = 15;
        int numTrain = 1;
        System.out.println("\t\t\t\t--------------------------------------");
        System.out.println("\t\t\t\t      Case 2 - Two Side Door");
        System.out.println("\t\t\t\t--------------------------------------");

        while (numTrain <= 8){
            int[] numCars = new int[numTrain];
            numCars = distributeCars(NUM_INITIAL_CARS,numTrain,numCars);
            while (true){
                for(i=0 ; i < NUMRUNS ; i++)
                {
                    thmPrk = new ThemeParks(startTime,endTime, false, 1, sds[i], numTrain, numCars,false);
                    thmPrk.runSimulation();
                    valPercentType1[i] = thmPrk.getPercent(1);
                    valPercentType2[i] = thmPrk.getPercent(2);
                    valPercentType3[i] = thmPrk.getPercent(3);
                    valPercentType4[i] = thmPrk.getPercent(4);
                    valTrainUtil[i] = thmPrk.trainUtilization();

                }
                ciPercentType1 = new ConfidenceInterval(valPercentType1,0.90);
                ciPercentType2 = new ConfidenceInterval(valPercentType2,0.90);
                ciPercentType3 = new ConfidenceInterval(valPercentType3,0.90);
                ciPercentType4 = new ConfidenceInterval(valPercentType4,0.90);

                System.out.println("\n\nNumber of Trains = "+numTrain+"\nNumber of Cars = "+totalNumberOfCars(numCars)+"\n---------------");

                ConfidenceInterval[] ciTrainUtil = new ConfidenceInterval[numTrain];
                double[] arrayofPointEstimates = new double[numTrain];
                for(int tid=0; tid<numTrain; tid++){
                    double[]trainUtil = new double[NUMRUNS];
                    for(int run=0; run<NUMRUNS; run++){
                        trainUtil[run]=valTrainUtil[run][tid];
                    }
                    ciTrainUtil[tid] = new ConfidenceInterval(trainUtil,0.90);
                    arrayofPointEstimates[tid] = ciTrainUtil[tid].getPointEstimate();
                    System.out.println("RG.Train[tid = "+tid+"].numCar = "+ numCars[tid]+ "\ttrainUtilization[tid = "+tid+"] = "+arrayofPointEstimates[tid]);

                }
                if(ciPercentType4.getPointEstimate() <= 2 &&
                        ciPercentType1.getPointEstimate()>=73 &&
                        ciPercentType3.getPointEstimate()<=3 &&
                        ciPercentType2.getPointEstimate()<=15) {
                    System.out.print("PercentType1 = "+ciPercentType1.getPointEstimate() +" PercentType2 = "+ciPercentType2.getPointEstimate()+
                            " PercentType3 = "+ciPercentType3.getPointEstimate()+" PercentType4 = "+ciPercentType4.getPointEstimate()+
                            "\n Total Cost = $/day "+totalCost(numCars,numTrain,1));
                    numTrain++;
                    break;
                }
                else{
                    int tid = findHighestUtil(numTrain,arrayofPointEstimates);
                    numCars[tid] ++;}
            }
        }

    }

    //method for distributing the initial number of cars between trains
    public static int[] distributeCars(int initialCars, int numTrain, int[] numCars)
    {
        int carPerTrain = initialCars/numTrain;
        int remainingCars = initialCars % numTrain;
        for(int tid = 0; tid < numTrain ; tid++)
            numCars[tid] = carPerTrain;
        for (int tid = 0 ; tid < remainingCars ; tid++)
            numCars[tid]++;
        return numCars;
    }

    //method for finding the train with highest utilization
    public static int findHighestUtil(int numTrain, double[] trainUtil){
        double max = 0;
        int index = 0;
        for(int tid = 0 ; tid < numTrain ; tid++){
            if(trainUtil[tid]>max){
                max = trainUtil[tid];
                index = tid;
            }
        }
        return index;
    }

    public static double totalCost(int[] cars,int numTrain, int ts){
        int totalNumCars = totalNumberOfCars(cars);
        return ((numTrain * 800) + (totalNumCars * 500) + (ts * totalNumCars * 20));

    }
    public static int totalNumberOfCars(int[] cars){
        int totalNumCars = 0;
        for(int tid = 0 ; tid < cars.length ; tid++)
            totalNumCars += cars[tid];
        return totalNumCars;
    }

}
