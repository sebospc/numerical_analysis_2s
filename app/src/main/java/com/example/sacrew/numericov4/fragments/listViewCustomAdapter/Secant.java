package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class Secant {
    private String n;
    private String xn;
    private String fXn;
    private String fdXn;
    private String fd2Xn;
    private String error;

    public Secant(String n, String xn, String fXn, String fdXn,String fd2Xn, String error) {
        this.n = n;
        this.xn=xn;
        this.fXn=fXn;
        this.fdXn=fdXn;
        this.fd2Xn=fd2Xn;
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

    public String getFD2Xn() {
        return fd2Xn;
    }

    public void setFD2Xn(String fd2Xn) {
        this.fd2Xn = fd2Xn;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
