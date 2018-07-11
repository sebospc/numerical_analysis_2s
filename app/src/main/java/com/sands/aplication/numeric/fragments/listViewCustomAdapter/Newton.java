package com.sands.aplication.numeric.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class Newton {
    private final String n;
    private final String xn;
    private final String fXn;
    private final String fdXn;
    private final String error;

    public Newton(String n, String xn, String fXn, String fdXn, String error) {
        this.n = n;
        this.xn = xn;
        this.fXn = fXn;
        this.fdXn = fdXn;
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

    public String getFDXn() {
        return fdXn;
    }

    public String getError() {
        return error;
    }

}
