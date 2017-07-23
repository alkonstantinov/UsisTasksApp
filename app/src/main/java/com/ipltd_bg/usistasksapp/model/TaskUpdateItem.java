package com.ipltd_bg.usistasksapp.model;

/**
 * Created by alkon on 23-May-17.
 */

public class TaskUpdateItem {
    private int taskId;
    private int taskStatus;
    private long taskOffset;
    private long enterTime;
    private boolean objectFinished;

    public TaskUpdateItem()
    {

        setObjectFinished(false);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public long getTaskOffset() {
        return taskOffset;
    }

    public void setTaskOffset(long taskOffset) {
        this.taskOffset = taskOffset;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }

    public boolean isObjectFinished() {
        return objectFinished;
    }

    public void setObjectFinished(boolean objectFinished) {
        this.objectFinished = objectFinished;
    }
}
