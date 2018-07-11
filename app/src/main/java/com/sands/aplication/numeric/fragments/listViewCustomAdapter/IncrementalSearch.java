package com.sands.aplication.numeric.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 3/14/2017.
 */

public class IncrementalSearch {
    private final String n;
    private final String xn;
    private final String fXn;

    public IncrementalSearch(String n, String xn, String fXn) {
        this.n = n;
        this.xn = xn;
        this.fXn = fXn;
    }

    public String getN() {
        return n;
    }

    public String getXn() {
        return xn;
    }

    public String getFXn() {
        return fXn;
    }

}
