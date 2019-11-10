package edu.fsu.cs.fsutranz.ui.bus;

public class createItem {

    private String busStopName;
    private int busStopNum;
    private double arrivalTime;
    //Creates the item
    public createItem(String busStopName, int num, double arrivalTime) {
        this.busStopName = busStopName;
        this.busStopNum = num;
        this.arrivalTime = arrivalTime;
    }

    //Get stop name
    public String getBusStopName() {
        return this.busStopName;
    }

    //Get stop num
    public int getBusStopNum() {return this.busStopNum; }

    //Get predicted times
    public double getArrivalTime(){ return this.arrivalTime; }
}
