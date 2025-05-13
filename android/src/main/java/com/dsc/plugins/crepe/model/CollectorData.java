package com.dsc.plugins.crepe.model;

import java.util.Map;

public class CollectorData {
    private Map<String, Collector> collectors;
    private Map<String, DataField> dataFields;
    private Map<String, Data> data;

    public static class Collector {
        private String appName;
        private String appPackage;
        private long collectorEndTime;
        private String collectorEndTimeString;
        private String collectorId;
        private long collectorStartTime;
        private String collectorStartTimeString;
        private String collectorStatus;
        private String creatorUserId;
        private boolean deleted;
        private String description;

        // Getters and setters
        public String getAppName() { return appName; }
        public void setAppName(String appName) { this.appName = appName; }
        public String getAppPackage() { return appPackage; }
        public void setAppPackage(String appPackage) { this.appPackage = appPackage; }
        public String getCollectorId() { return collectorId; }
        public void setCollectorId(String collectorId) { this.collectorId = collectorId; }
        public String getCollectorStatus() { return collectorStatus; }
        public void setCollectorStatus(String collectorStatus) { this.collectorStatus = collectorStatus; }
    }

    public static class DataField {
        private String collectorId;
        private String datafieldId;
        private boolean demonstrated;
        private String graphQuery;
        private String name;
        private long timeCreated;
        private long timelastEdited;

        // Getters and setters
        public String getCollectorId() { return collectorId; }
        public void setCollectorId(String collectorId) { this.collectorId = collectorId; }
        public String getDatafieldId() { return datafieldId; }
        public void setDatafieldId(String datafieldId) { this.datafieldId = datafieldId; }
        public String getGraphQuery() { return graphQuery; }
        public void setGraphQuery(String graphQuery) { this.graphQuery = graphQuery; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Data {
        private String dataContent;
        private String dataId;
        private String datafieldId;
        private long timestamp;
        private String userId;

        // Getters and setters
        public String getDataContent() { return dataContent; }
        public void setDataContent(String dataContent) { this.dataContent = dataContent; }
        public String getDatafieldId() { return datafieldId; }
        public void setDatafieldId(String datafieldId) { this.datafieldId = datafieldId; }
    }

    // Getters and setters for main class
    public Map<String, Collector> getCollectors() { return collectors; }
    public void setCollectors(Map<String, Collector> collectors) { this.collectors = collectors; }
    public Map<String, DataField> getDataFields() { return dataFields; }
    public void setDataFields(Map<String, DataField> dataFields) { this.dataFields = dataFields; }
    public Map<String, Data> getData() { return data; }
    public void setData(Map<String, Data> data) { this.data = data; }
} 