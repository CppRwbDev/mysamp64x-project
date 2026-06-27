package com.flin.online.jsonenter;

public class IpData {
    public String ip; //

    // Конструктор
    public IpData(){

    }

    public class IPInfo {
        private boolean success;
        private String ip;
        private String type;

        public boolean isSuccess() {
            return success;
        }

        public String getIp() {
            return ip;
        }

        public String getType() {
            return type;
        }
    }



}