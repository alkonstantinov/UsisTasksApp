package com.ipltd_bg.usistasksapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by avlaev on 8/6/15.
 {
 "TaskId": 1,
 "CITRouteCode": "142",
 "CITOrder": 47552,
 "CITOrderLine": 1,
 "PartnerEntityFrom": @PartnerEntity,
 "PartnerEntityTo": @PartnerEntity,
 "ActionType": "Освобождаване (<>АТМ)",
 "CITOrderType": 1,
 "CancelFlag": 0,
 "TaskState": 0
 }
 */
public class TaskListItem {

    public List<TaskListItem> Subitems;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @JsonProperty("TaskId")
    private int taskId;

    @JsonProperty("CITRouteCode")
    private String citRouteCode;

    @JsonProperty("CITOrder")
    private int citOrder;

    @JsonProperty("CITOrderLine")
    private int citOrderLine;

    @JsonProperty("PartnerEntityFrom")
    private PartnerEntity partnerEntityFrom;

    @JsonProperty("PartnerEntityTo")
    private PartnerEntity partnerEntityTo;

    @JsonProperty("ActionType")
    private String actionType;

    @JsonProperty("CITOrderType")
    private int CITOrderType;

    @JsonProperty("CancelFlag")
    private int cancelFlag;

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    @JsonProperty("TaskState")
    private int taskStatus;

    public TaskListItem() {

    }

    public String getCitRouteCode() {
        return citRouteCode;
    }

    public void setCitRouteCode(String citRouteCode) {
        this.citRouteCode = citRouteCode;
    }

    public int getCitOrder() {
        return citOrder;
    }

    public void setCitOrder(int citOrder) {
        this.citOrder = citOrder;
    }

    public int getCitOrderLine() {
        return citOrderLine;
    }

    public void setCitOrderLine(int citOrderLine) {
        this.citOrderLine = citOrderLine;
    }

    public PartnerEntity getPartnerEntityFrom() {
        return partnerEntityFrom;
    }

    public void setPartnerEntityFrom(PartnerEntity partnerEntityFrom) {
        this.partnerEntityFrom = partnerEntityFrom;
    }

    public PartnerEntity getPartnerEntityTo() {
        return partnerEntityTo;
    }

    public void setPartnerEntityTo(PartnerEntity partnerEntityTo) {
        this.partnerEntityTo = partnerEntityTo;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getCITOrderType() {
        return CITOrderType;
    }

    public void setCITOrderType(int CITOrderType) {
        this.CITOrderType = CITOrderType;
    }

    public int getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(int cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /*
    {
         "PartnerEntityCode": 3812,
         "name": "ЮСИС - Депо Ботевград",
         "city": "Ботевград"
     }
     */
    public static class PartnerEntity {

        public PartnerEntity() {

        }

        @JsonProperty("PartnerEntityCode")
        private int partnerEntityCode;

        @JsonProperty("name")
        private String name;

        @JsonProperty("city")
        private String city;

        public int getPartnerEntityCode() {
            return partnerEntityCode;
        }

        public void setPartnerEntityCode(int partnerEntityCode) {
            this.partnerEntityCode = partnerEntityCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
