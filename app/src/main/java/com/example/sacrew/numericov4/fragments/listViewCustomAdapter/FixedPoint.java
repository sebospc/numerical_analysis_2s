package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class FixedPoint {
    private String n;
    private String xn;
    private String fXn;
    private String gXn;
    private String error;

    public FixedPoint(String n, String xn, String fXn, String gXn, String error) {
        this.n = n;
        this.xn=xn;
        this.fXn=fXn;
        this.gXn=gXn;
        this.error=error;
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

    public String getGXn() {
        return gXn;
    }

    public void setGXn(String gXn) {
        this.gXn = gXn;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
