package com.example.janiszhang.apdatacollector;

/**
 * Created by janiszhang on 2016/3/8.
 */
public class APData {

    private String macAddr;
    private int level;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public APData(String macAddr, int level, String name) {
        this.macAddr = macAddr;
        this.level = level;
        this.name = name;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
