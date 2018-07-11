package com.sands.aplication.numeric.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class FixedPoint {
    private final String n;
    private final String xn;
    private final String fXn;
    private final String error;

    public FixedPoint(String n, String xn, String fXn, String error) {
        this.n = n;
        this.xn = xn;
        this.fXn = fXn;
        this.error = error;
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

    public String getError() {
        return error;
    }

}
