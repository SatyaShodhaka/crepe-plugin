package com.dsc.plugins.crepe.model;

import java.util.Map;

public class CollectorData {
    private Map<String, Collector> Collector;
    private Map<String, Data> Data;
    private Map<String, DataField> Datafield;
    private Map<String, User> User;

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
        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public String getCollectorId() {
            return collectorId;
        }

        public void setCollectorId(String collectorId) {
            this.collectorId = collectorId;
        }

        public String getCollectorStatus() {
            return collectorStatus;
        }

        public void setCollectorStatus(String collectorStatus) {
            this.collectorStatus = collectorStatus;
        }
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
        public String getCollectorId() {
            return collectorId;
        }

        public void setCollectorId(String collectorId) {
            this.collectorId = collectorId;
        }

        public String getDatafieldId() {
            return datafieldId;
        }

        public void setDatafieldId(String datafieldId) {
            this.datafieldId = datafieldId;
        }

        public String getGraphQuery() {
            return graphQuery;
        }

        public void setGraphQuery(String graphQuery) {
            this.graphQuery = graphQuery;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Data {
        private String dataContent;
        private String dataId;
        private String datafieldId;
        private long timestamp;
        private String userId;

        // Getters and setters
        public String getDataContent() {
            return dataContent;
        }

        public void setDataContent(String dataContent) {
            this.dataContent = dataContent;
        }

        public String getDatafieldId() {
            return datafieldId;
        }

        public void setDatafieldId(String datafieldId) {
            this.datafieldId = datafieldId;
        }
    }

    public static class User {
        private long lastHeartBeat;
        private String name;
        private String photoUrl;
        private long timeCreated;
        private String userId;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    // Getters and setters for main class
    public Map<String, Collector> getCollector() {
        return Collector;
    }

    public void setCollector(Map<String, Collector> collector) {
        this.Collector = collector;
    }

    public Map<String, Data> getData() {
        return Data;
    }

    public void setData(Map<String, Data> data) {
        this.Data = data;
    }

    public Map<String, DataField> getDatafield() {
        return Datafield;
    }

    public void setDatafield(Map<String, DataField> datafield) {
        this.Datafield = datafield;
    }

    public Map<String, User> getUser() {
        return User;
    }

    public void setUser(Map<String, User> user) {
        this.User = user;
    }

    // Helper method to get data fields (for backward compatibility)
    public Map<String, DataField> getDataFields() {
        return Datafield;
    }
}