package com.example.plantonic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShipmentResponse {
    @SerializedName("ShipmentData")
    private ShipmentData shipmentData;

    public ShipmentData getShipmentData() {
        return shipmentData;
    }

    public static class ShipmentData {

        @SerializedName("Shipment")
        private Shipment shipment;

        public Shipment getShipment() {
            return shipment;
        }
    }

    public static class Shipment {

        @SerializedName("Prodcode")
        private String prodcode;

        @SerializedName("Service")
        private String service;

        @SerializedName("PickUpDate")
        private String pickUpDate;

        @SerializedName("PickUpTime")
        private int pickUpTime;

        @SerializedName("Origin")
        private String origin;

        @SerializedName("OriginAreaCode")
        private String originAreaCode;

        @SerializedName("Destination")
        private String destination;

        @SerializedName("DestinationAreaCode")
        private String destinationAreaCode;

        @SerializedName("ProductType")
        private String productType;

        @SerializedName("SenderName")
        private String senderName;

        @SerializedName("ToAttention")
        private String toAttention;

        @SerializedName("Weight")
        private double weight;

        @SerializedName("Status")
        private String status;

        @SerializedName("StatusType")
        private String statusType;

        @SerializedName("ExpectedDeliveryDate")
        private String expectedDeliveryDate;

        @SerializedName("StatusDate")
        private String statusDate;

        @SerializedName("StatusTime")
        private String statusTime;

        @SerializedName("ReceivedBy")
        private String receivedBy;

        @SerializedName("Instructions")
        private String instructions;

        @SerializedName("Scans")
        private Scans scans;

        public String getProdcode() {
            return prodcode;
        }

        public String getService() {
            return service;
        }

        public String getPickUpDate() {
            return pickUpDate;
        }

        public int getPickUpTime() {
            return pickUpTime;
        }

        public String getOrigin() {
            return origin;
        }

        public String getOriginAreaCode() {
            return originAreaCode;
        }

        public String getDestination() {
            return destination;
        }

        public String getDestinationAreaCode() {
            return destinationAreaCode;
        }

        public String getProductType() {
            return productType;
        }

        public String getSenderName() {
            return senderName;
        }

        public String getToAttention() {
            return toAttention;
        }

        public double getWeight() {
            return weight;
        }

        public String getStatus() {
            return status;
        }

        public String getStatusType() {
            return statusType;
        }

        public String getExpectedDeliveryDate() {
            return expectedDeliveryDate;
        }

        public String getStatusDate() {
            return statusDate;
        }

        public String getStatusTime() {
            return statusTime;
        }

        public String getReceivedBy() {
            return receivedBy;
        }

        public String getInstructions() {
            return instructions;
        }

        public Scans getScans() {
            return scans;
        }
    }

    public static class Scans {

        @SerializedName("ScanDetail")
        private List<ScanDetail> scanDetailList;

        public List<ScanDetail> getScanDetailList() {
            return scanDetailList;
        }
    }

    public static class ScanDetail {

        @SerializedName("Scan")
        private String scan;

        @SerializedName("ScanCode")
        private int scanCode;

        @SerializedName("ScanType")
        private String scanType;

        @SerializedName("ScanGroupType")
        private String scanGroupType;

        @SerializedName("ScanDate")
        private String scanDate;

        @SerializedName("ScanTime")
        private String scanTime;

        @SerializedName("ScannedLocation")
        private String scannedLocation;

        @SerializedName("ScannedLocationCode")
        private String scannedLocationCode;

        public String getScan() {
            return scan;
        }

        public int getScanCode() {
            return scanCode;
        }

        public String getScanType() {
            return scanType;
        }

        public String getScanGroupType() {
            return scanGroupType;
        }

        public String getScanDate() {
            return scanDate;
        }

        public String getScanTime() {
            return scanTime;
        }

        public String getScannedLocation() {
            return scannedLocation;
        }

        public String getScannedLocationCode() {
            return scannedLocationCode;
        }
    }

}
