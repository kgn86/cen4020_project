package edu.fsu.cs.fsutranz.ui.bus;

public class createItem {

    private String busStopName;
    private int busStopNum;
    //Creates the item
    public createItem(String busStopName, int num) {
        this.busStopName = busStopName;
        this.busStopNum = num;
    }

    //Get stop name
    public String getBusStopName() {
        return this.busStopName;
    }

    //Get stop num
    public int getBusStopNum() {return this.busStopNum; }
}
