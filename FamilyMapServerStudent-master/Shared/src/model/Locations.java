package model;

import java.util.ArrayList;

public class Locations {
    private ArrayList<Location> data;

    public Locations(ArrayList<Location> data) {
        this.data = data;
    }

    public ArrayList<Location> getData(){
        return data;
    }
}
