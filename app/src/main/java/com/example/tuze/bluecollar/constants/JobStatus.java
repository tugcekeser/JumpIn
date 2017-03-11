package com.example.tuze.bluecollar.constants;

/**
 * Created by tuze on 3/10/17.
 */

public class JobStatus {
    public static int RECEIVED = 1;
    public static int INPROGRESS = 2;
    public static int COMPLETED = 3;
    public static int REJECT = 4;

    public static int getStatusCode(String selected) {
        switch (selected) {
            case "RECEIVED":
                return RECEIVED;
            case "IN PROGRESS":
                return INPROGRESS;
            case "COMPLETED":
                return COMPLETED;
            case "REJECT":
                return REJECT;
        }

        return RECEIVED;
    }

    public static String getStatus(int code){
        switch (code) {
            case 1:
                return "RECEIVED";
            case 2:
                return "IN PROGRESS";
            case 3:
                return "COMPLETED";
            case 4:
                return "REJECTED";
        }
        return "RECEIVED";
    }
}
