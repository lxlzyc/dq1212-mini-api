package com.lxl.miniapi.tools;


public enum RedisSourceType {
    API_SOURCE("core"), RCMD_SOURCE("rcmd");

    private String type;

    private RedisSourceType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(this.type);
    }
}
