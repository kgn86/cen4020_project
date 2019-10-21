package edu.fsu.cs.fsutranz.ui.parking;

public class createItem {
    private int imageResourceInt; //Icon
    private int progressNum; //Progress bar - Parking spaces taken
    private String openSlots; //Parking spots open
    private String parkingText; //The parking garage

    //Creates the item
    public createItem(int imageResource, int progress, String slots, String text1) {
        imageResourceInt = imageResource;
        progressNum = progress;
        parkingText = text1;
        openSlots = slots;
    }

    //Sets the progress bars progress
    public void setProgress(int progress) {
        progressNum = progress;
    }
    //Number of open spots
    public String getOpenSlots() {
        return openSlots;
    }
    //Set icon
    public int getImageResource() {
        return imageResourceInt;
    }
    //Set progress bar
    public int getProgressNum() {
        return progressNum;
    }
    //Set parking garage
    public String getText1() {
        return parkingText;
    }
}