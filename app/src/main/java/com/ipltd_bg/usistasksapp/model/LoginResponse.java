package com.ipltd_bg.usistasksapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by avlaev on 8/26/15.
 {
 "success": true,
 "data": {
 "EmployeeCode": 1,
 "FirstName": "Красимир",
 "MiddleName": "Монев",
 "LastName": "Сандев",
 "CITCenterCode": 11,
 "EmployeeCity": "София",
 "Region": "София",
 "CardID": "641671972",
 "CompanyCard": "3300-12067"
 },
 "errors": []
 }
 */
public class LoginResponse {
    private boolean success;
    private ArrayList<String> errors;
    private LoginData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public static class CITRoute {
        private int id;

        @JsonProperty("CITRouteCode")
        private String citRouteCode;

        @JsonProperty("CITRouteName")
        private String citRouteName;

        @JsonProperty("CITShiftsNumber")
        private String citShiftsNumber;

        @JsonProperty("CITCenterCode")
        private String citCenterCode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCitRouteCode() {
            return citRouteCode;
        }

        public void setCitRouteCode(String citRouteCode) {
            this.citRouteCode = citRouteCode;
        }

        public String getCitRouteName() {
            return citRouteName;
        }

        public void setCitRouteName(String citRouteName) {
            this.citRouteName = citRouteName;
        }

        public String getCitShiftsNumber() {
            return citShiftsNumber;
        }

        public void setCitShiftsNumber(String citShiftsNumber) {
            this.citShiftsNumber = citShiftsNumber;
        }

        public String getCitCenterCode() {
            return citCenterCode;
        }

        public void setCitCenterCode(String citCenterCode) {
            this.citCenterCode = citCenterCode;
        }
    }

    public static class LoginData {
        @JsonProperty("EmployeeCode")
        private int emplyeeCode;

        @JsonProperty("FirstName")
        private String firstName;

        @JsonProperty("MiddleName")
        private String middleName;

        @JsonProperty("LastName")
        private String lastName;

        @JsonProperty("CITCenterCode")
        private int citCenterCode;

        @JsonProperty("EmployeeCity")
        private String employeeCity;

        @JsonProperty("Region")
        private String region;

        @JsonProperty("CardID")
        private String cardID;

        @JsonProperty("CompanyCard")
        private String companyCard;

        @JsonProperty("GETCITRoutes")
        private ArrayList<CITRoute> citRoutes = new ArrayList<>();

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        @JsonProperty("RegionCode")
        private String regionCode;

        public ArrayList<CITRoute> getCitRoutes() {
            return citRoutes;
        }

        public void setCitRoutes(ArrayList<CITRoute> citRoutes) {
            this.citRoutes = citRoutes;
        }

        public int getEmplyeeCode() {
            return emplyeeCode;
        }

        public void setEmplyeeCode(int emplyeeCode) {
            this.emplyeeCode = emplyeeCode;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getCitCenterCode() {
            return citCenterCode;
        }

        public void setCitCenterCode(int citCenterCode) {
            this.citCenterCode = citCenterCode;
        }

        public String getEmployeeCity() {
            return employeeCity;
        }

        public void setEmployeeCity(String employeeCity) {
            this.employeeCity = employeeCity;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCardID() {
            return cardID;
        }

        public void setCardID(String cardID) {
            this.cardID = cardID;
        }

        public String getCompanyCard() {
            return companyCard;
        }

        public void setCompanyCard(String companyCard) {
            this.companyCard = companyCard;
        }
    }
}
