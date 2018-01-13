package com.company;
import java.util.*;
import java.io.*;

public class Main {
    private int maxSize;
    private int[][] queArray;
    //private int nItems;
    private  int items;
    private String schAl;
    private int index = 0;
    private int timeSlice;
    private int priorityId;
    private int num;
    private  float totalWaitingTime =0;
    private int i = 0;
    private PrintWriter outFile = new PrintWriter("/Users/jduran/Documents/csc139/project3/src/com/company/output.txt");

    public Main() throws FileNotFoundException {
        //setUp();

    }


    public void Run() throws FileNotFoundException {


        // Scanner object
        Scanner input = new Scanner(new FileReader("/Users/jduran/Documents/csc139/project3/src/com/company/input.txt"));

        schAl = input.next();
        if(schAl.equals("RR")) timeSlice = input.nextInt();
        input.nextLine();
        num = input.nextInt();
        input.nextLine();

        // Init Priority
        // Set the size of the queue
        PriorityQueue();
            if(schAl.equals("SJF")) {

                while (input.hasNextInt())

                {

                    queArray[i][0] = input.nextInt();
                    queArray[i][1] = input.nextInt();
                    queArray[i][2] = input.nextInt();
                    queArray[i][3] = input.nextInt();
                    priorityByCpuBurst(queArray[i][0], queArray[i][1], queArray[i][2], queArray[i][3]);
                    index = i;
                    i++;

                }
            }  if (schAl.equals("RR")) {
            while (input.hasNextInt())

            {

                queArray[i][0] = input.nextInt();
                queArray[i][1] = input.nextInt();
                queArray[i][2] = input.nextInt();
                queArray[i][3] = input.nextInt();
                priorityByArrival(queArray[i][0], queArray[i][1], queArray[i][2], queArray[i][3]);
                index = i;
                i++;
            }
        } else {
                    while (input.hasNextInt())

                    {

                        queArray[i][0] = input.nextInt();
                        queArray[i][1] = input.nextInt();
                        queArray[i][2] = input.nextInt();
                        queArray[i][3] = input.nextInt();
                        priority(queArray[i][0], queArray[i][1], queArray[i][2], queArray[i][3]);
                        index = i;
                        i++;

                    }
                }
        switch (schAl)

        {
            case "RR":
               roundRobin();
               break;
            case "SJF":
                shortestJobFirst();
                break;
            case "PR_noPREMP":
               prNoPremp();
               break;
            case "PR_withPREMP":
                prwithPremp();
                break;
            default:
                break;
        }



        // Close the file
        input.close();
        outFile.close();


    }

    private void prwithPremp() {
        boolean isDone = false;
        int doneCounter=0;
        int timer =0;
        int pId;
        int pIndex = 0;
        totalWaitingTime = 0;
        int incrTimer = 1;
        int[] executeTime = new int[num];

        // init to zero
        for(int i =0; i <num-1; i++){
            executeTime[i]=0;
        }
        //int startNewProcess =1; // cost of starting a new process

        printToFileName(schAl);

        while (!isDone) {
            if (queArray[pIndex][1] <= timer && queArray[pIndex][3] != -1) {

                // Id of current process
                pId = queArray[pIndex][0];
                try {
                    printToFile(timer, pId);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //
                while (queArray[pIndex][3] != -1) {
                    // Check if a higher priority process arrived
                    for (int i = 0; i < num - 1; i++) {
                        if (queArray[i][1] <= timer && queArray[i][3] < queArray[pIndex][3] && queArray[i][3] !=-1) {

                            // give the cpu to the new process
                            pIndex = i;
                            pId = queArray[pIndex][0];

                            // add cost of adding starting new process;
                            //timer += startNewProcess;
                            try {
                                printToFile(timer, pId);

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            // exit the for loop
                            break;

                        }

                    }
                    timer = timer + incrTimer;
                    executeTime[pIndex] += incrTimer;
                    // decrement cpu burst left by -1
                    queArray[pIndex][2] = queArray[pIndex][2] - incrTimer;
                    if (queArray[pIndex][2] == 0) {
                        calculateTime((timer-(executeTime[pIndex]+queArray[pIndex][1])));
                        queArray[pIndex][3] = -1;
                        doneCounter++;

                    }
                }
                // Reset the index
                pIndex=-1;
            }
            if (doneCounter == num) {
                isDone = true;
            }
            pIndex++;
        }
        printWaitTime();
    }

    private void prNoPremp() {
        boolean isDone = false;
        int doneCounter = 0;
        int timer = 0;
        int pId;
        int pIndex = 0;
        totalWaitingTime = 0;
        int startNewProcess= 1;


            printToFileName(schAl);



        while (!isDone) {
            //  queArray[pIndex][3] ==-1 is process completed
            if (queArray[pIndex][1] <= timer && queArray[pIndex][3] != -1) {

                pId = queArray[pIndex][0];
                doneCounter++;
                try {
                    printToFile(timer, pId);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                calculateTime((timer-queArray[pIndex][1]));
                timer = timer + (queArray[pIndex][2] - 1);
                queArray[pIndex][3] = -1;
                timer += startNewProcess;
                pIndex=-1; // restart the index
            }
            if (doneCounter == num) {
                isDone = true;
            }

            pIndex++;
        }
        printWaitTime();

    }


    private void shortestJobFirst() throws  FileNotFoundException{
        boolean isDone = false;
        int doneCounter = 0;
        int timer = 0;
        int startNewProcess = 1;
        int pId;
        int pIndex = 0;
        totalWaitingTime = 0;


        printToFileName(schAl);

        while (!isDone) {
            //  queArray[pIndex][3] ==-1 is process completed
            if (queArray[pIndex][1] <= timer && queArray[pIndex][3] != -1)
            {

                // check if there is an eligible process with lower arrival time
                int i  = pIndex+1;
                 while(i <= num-1){
                    if(queArray[i][1] <= timer && queArray[i][2] <= queArray[pIndex][2] &&
                              queArray[i][1] < queArray[pIndex][1])
                    {
                         pIndex =i;
                        }
                        i++;
                        }




                pId = queArray[pIndex][0];
                doneCounter++;
                try {
                    printToFile(timer, pId);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                calculateTime((timer-queArray[pIndex][1]));
                timer = timer + (queArray[pIndex][2] - 1);
                queArray[pIndex][3] = -1;
                timer += startNewProcess;
                pIndex=-1; // restart the index
            }
            if (doneCounter == num) {
                isDone = true;
            }

            pIndex++;




        }
        printWaitTime();
    }

    private void roundRobin() throws FileNotFoundException {
        boolean isDone =false;

        int doneCounter = 0;
        int timer = 0;
        int pId;
        int pIndex = 0;
        int cpuBurts =0;
        int timeQuantum=0;
        int[] executeTime = new int[num];
        int front=-1;
        int rear=0;
        int readyCnt=0;




        try {
            printToFileInfo(schAl,timeSlice);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


         while (!isDone) {
            if (queArray[pIndex][1] <= timer && queArray[pIndex][3] != -1) {
                pId = queArray[pIndex][0];

                // go back to the start of the queue


                // print to file the start of the process
                try {
                    printToFile(timer, pId);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                cpuBurts = queArray[pIndex][2];
                for (int ts = 1; ts <= timeSlice; ts++) {
                    if (cpuBurts == 0) {
                        break;
                    }
                    timeQuantum++;
                    cpuBurts--;
                }

                queArray[pIndex][2] = cpuBurts;
                timer += timeQuantum;
                executeTime[pIndex] += timeQuantum;

                if (queArray[pIndex][2] == 0) {
                    queArray[pIndex][3] = -1;
                    doneCounter++;
                    calculateTime((timer - (executeTime[pIndex] + queArray[pIndex][1])));
                }

                // reset
                timeQuantum = 0;
                if(pIndex==num-1){
                    pIndex=-1;
                }
                /*
                // go back to the start of the queue
                int i  = pIndex-1;
                int ptr =pIndex+1;

                while(i >= 0 && ptr != num-1) {
                    if (queArray[i][1] <= timer && queArray[i][3] != -1 &&
                            queArray[i][1] < queArray[ptr][1]) {
                        pIndex = i-1;
                    }
                    i--;
                }
                */
            }
            if(doneCounter == num){
                isDone =true;
            }


            pIndex++;



            if(pIndex>num-1) {
                pIndex = 0;
            }


        }
        printWaitTime();



    }
    private void printToFileName(String schAl) {

        outFile.println(schAl);
    }


        private void printWaitTime() {
        outFile.printf("AVG Waiting Time: %.2f\n", getWaitTime());
    }

    private void calculateTime(int wait) {


      totalWaitingTime += wait;





    }
    private double getWaitTime(){
        return totalWaitingTime = (totalWaitingTime/num);
    }

    private void printToFile(int time, int process) throws FileNotFoundException {

        outFile.println(time + "  "+ process);
    }
    private void printToFileInfo(String algro, int processCount) throws FileNotFoundException {

        outFile.println(algro + "  "+ processCount);
    }



    private void PriorityQueue() {
            maxSize = num;
            queArray = new  int[maxSize][4];
            items= 0;

        }
        private void priorityByCpuBurst(int pId, int arrival, int burst, int priority) {
            if (items == 0) {
                insert(pId, arrival,burst, priority);
            }
            else {
                int i =items-1;
                while(i>=0)
                {
                    if(burst < queArray[i][2] ) {
                        queArray[i+1][0] = queArray[i][0];
                        queArray[i+1][1] = queArray[i][1];
                        queArray[i+1][2] = queArray[i][2];
                        queArray[i+1][3] = queArray[i][3];
                        i--;



                    }
                    else
                        // if Cpu burst is bigger do nothing
                        break;
                }

                queArray[i+1][0] = pId;
                queArray[i+1][1] = arrival;
                queArray[i+1][2] = burst;
                queArray[i+1][3] = priority;
                items++;
            }
        }
    private void priorityByArrival(int pId, int arrival, int burst, int priority) {
        if (items == 0) {
            insert(pId, arrival,burst, priority);
        }
        else {
            int i =items-1;
            while(i>=0)
            {
                if(arrival < queArray[i][1]) {
                    queArray[i+1][0] = queArray[i][0];
                    queArray[i+1][1] = queArray[i][1];
                    queArray[i+1][2] = queArray[i][2];
                    queArray[i+1][3] = queArray[i][3];
                    i--;



                }
                else
                    // if arrival  time is bigger do nothing
                    break;
            }

            queArray[i+1][0] = pId;
            queArray[i+1][1] = arrival;
            queArray[i+1][2] = burst;
            queArray[i+1][3] = priority;
            items++;
        }
    }
        // priority  the key
        private void priority(int pId, int arrival, int burst,int priority) {
            if (items == 0) {
                insert(pId, arrival,burst, priority);
            }
            else {
                int i =items-1;
                while(i>=0)
                {
                    if(priority < queArray[i][3] ) {
                        queArray[i+1][0] = queArray[i][0];
                        queArray[i+1][1] = queArray[i][1];
                        queArray[i+1][2] = queArray[i][2];
                        queArray[i+1][3] = queArray[i][3];
                        i--;


                    }
                    else
                        // if smaller  priority do nothing
                        break;
                }
                queArray[i+1][0] = pId;
                queArray[i+1][1] = arrival;
                queArray[i+1][2] = burst;
                queArray[i+1][3] = priority;
                items++;
            }


        }
        private void insert (int pId, int arrival, int burst,int priority) {
            queArray[items][0] = pId;
            queArray[items][1] = arrival;
            queArray[items][2] = burst;
            queArray[items][3] = priority;
            items++;
        }
        public void printQueue() {
            for(int i = 0; i < items; i++) {
                System.out.println("---------------------");
                System.out.println(queArray[i][0] + " " +queArray[i][1]+ " " + queArray[i][2]+ " " + queArray[i][3]);
            }
        }

        public int getArray(int i, int j) {
            return queArray[i][j];
        }
    public  int ComparableByCpuBurst() {
        int temp=0;
        int result;
        int index = 0;
        // find the process with the biggest priority
        for (int i = 1; i < num; i++) {
            if (queArray[i][2] < queArray[index][1]) {
                index = i;
            }
            if(queArray[i][2] == queArray[index][2]) {
                if (queArray[i][1] < queArray[index][1]) {
                    index = i;
                }
                if (queArray[i][1] == queArray[index][1]) {
                    index = i;
                }
            }


        }
        result =index;
        return  result;
    }
    int getSize(){
        return num;
    }

    }









