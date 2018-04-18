package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 3/14/2017.
 */

public class IncrementalSearch {
    private String n;
    private String xn;
    private String fXn;

    public IncrementalSearch(String n, String xn,  String fXn) {
        this.n = n;
        this.xn = xn;
        this.fXn = fXn;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getFXn() {
        return fXn;
    }

    public void setFXn(String fXn) {
        this.fXn = fXn;
    }
}
