package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class Newton {
    private String n;
    private String xn;
    private String fXn;
    private String fdXn;
    private String error;

    public Newton(String n, String xn, String fXn, String fdXn, String error) {
        this.n = n;
        this.xn=xn;
        this.fXn=fXn;
        this.fdXn=fdXn;
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

    public String getFDXn() {
        return fdXn;
    }

    public void setFDXn(String fdXn) {
        this.fdXn = fdXn;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
