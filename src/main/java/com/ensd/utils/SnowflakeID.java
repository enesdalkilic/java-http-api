package com.ensd.utils;

import java.time.Instant;

import java.util.concurrent.TimeUnit;

public class SnowflakeID {

    static int machineID;
    static int epochStamp = 1728835510; // it is mostly still date for birthday or something by default the date that this code written

    static int seq = 0;

    int maxEpochBit = 41;
    static int maxMachineBit;
    static int maxSeqBit = 12;

    static int maxSeq = 4094;

    static double lastEpoch = 0;

    public static void setup(int EPOCH_STAMP, int MACHINE_ID, int MAX_MACHINE_BIT) {
        epochStamp = EPOCH_STAMP;
        machineID = MACHINE_ID;
        maxMachineBit = MAX_MACHINE_BIT;
    }
    //before setup, it will NOT work (according to last epoch. If it is 0.).


    public static String generateID()  {
        //It shouldn't run this error, never.
        if (machineID == 0) throw new Error("Didn't install properly.");
        if (seq > maxSeq) {
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        long DateNow = Instant.now().toEpochMilli();
        if (lastEpoch == 0) lastEpoch = DateNow;
        if (lastEpoch > DateNow) throw new Error("Machine's date/time is wrong.");
        if (DateNow != lastEpoch) seq = 0;
        if (DateNow == lastEpoch) seq++;


        String ID = Long.toBinaryString(DateNow) + decToBin(machineID, maxMachineBit) + decToBin(seq, maxSeqBit);
        lastEpoch = DateNow;
        return Long.toString(Long.parseLong(ID, 2));
    }

    public static String decToBin(int n, int bit) {
        int[] storeBin = new int[bit];
        int i = 0;
        while (n > 0) {
            storeBin[i] = n % 2;
            n = n / 2;
            i++;
        }

        int[] output = reverseArray(storeBin, storeBin.length);

        return joinArr(output);

    }

    private static int[] reverseArray(int[] arr, int l) {

        int[] a = new int[l];
        int j = l;
        for (int i = 0; i < l; i++) {

            a[j - 1] = arr[i];
            j--;
        }
        return a;
    }

    private static String joinArr(int[] arr) {

        StringBuilder output = new StringBuilder();
        for (int j : arr) {
            output.append(j);
        }
        return output.toString();
    }
}