package com.ipltd_bg.usistasksapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by avlaev on 8/28/15.
 * {
 "success": true,
 "errors": null,
 "data": {
 "id": 2,
 "VehicleCode": "1",
 "RegPlate": "С9849КР",
 "Make": "Lada",
 "Model": "Niva",
 "YearProduced": "1997",
 "PurchaseDate": "2008-01-14T00:00:00",
 "Brand": "---",
 "Color": "Бял",
 "Comment": "Ю Сървисиз",
 "CostCenter": "201"
 }
 }
 */
public class VehicleInfo {
    private boolean success;
    private ArrayList<String> errors;
    private VehicleData data;

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

    public VehicleData getData() {
        return data;
    }

    public void setData(VehicleData data) {
        this.data = data;
    }

    public static class VehicleData {
        private int id;

        @JsonProperty("VehicleCode")
        private String vehicleCode;

        @JsonProperty("RegPlate")
        private String regPlate;

        @JsonProperty("Make")
        private String make;

        @JsonProperty("Model")
        private String model;

        @JsonProperty("YearProduced")
        private String yearProduced;

        @JsonProperty("PurchaseDate")
        private String purchaseDate;

        @JsonProperty("Brand")
        private String brand;

        @JsonProperty("Color")
        private String color;

        @JsonProperty("Comment")
        private String comment;

        @JsonProperty("CostCenter")
        private String costCenter;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVehicleCode() {
            return vehicleCode;
        }

        public void setVehicleCode(String vehicleCode) {
            this.vehicleCode = vehicleCode;
        }

        public String getRegPlate() {
            return regPlate;
        }

        public void setRegPlate(String regPlate) {
            this.regPlate = regPlate;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getYearProduced() {
            return yearProduced;
        }

        public void setYearProduced(String yearProduced) {
            this.yearProduced = yearProduced;
        }

        public String getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(String purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCostCenter() {
            return costCenter;
        }

        public void setCostCenter(String costCenter) {
            this.costCenter = costCenter;
        }
    }
}
