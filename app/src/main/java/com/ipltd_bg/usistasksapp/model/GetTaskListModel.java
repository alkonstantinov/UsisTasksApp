package com.ipltd_bg.usistasksapp.model;


/**
 * Created by alkon on 22-May-17.
 */

public class GetTaskListModel extends LoginModel {

    private String entityCode;

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }


    private String regionCode;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }
}
