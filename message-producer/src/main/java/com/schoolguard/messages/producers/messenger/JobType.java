package com.schoolguard.messages.producers.messenger;

/**
 * Created by Rogers on 15-4-30.
 */
public enum JobType {
    ATTENDANCE(1, "Attendance"),
    ASSIGNMENT(2, "Assignment"),
    NOTICE(3, "Notice"),
    EXAM(4, "Exam"),
    OA_MSG(8, "OA Message");

    private int index;
    private String description;

    private JobType(int index, String description){
        this.index = index;
        this.description = description;
    }

    public String description(){
        return this.description;
    }

    public int getValue(){
        return this.index;
    }
}
