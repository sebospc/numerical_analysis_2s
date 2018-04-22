package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class Bisection {
    private String n;
    private String xi;
    private String xs;
    private String xm;
    private String fXm;
    private String error;

    public Bisection(String n, String xi, String xs, String xm,  String fXm, String error) {
        this.n = n;
        this.xi=xi;
        this.xs=xs;
        this.xm=xm;
        this.fXm=fXm;
        this.error=error;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getXi() {
        return xi;
    }

    public void setXi(String xi) {
        this.xi = xi;
    }

    public String getXs() {
        return xs;
    }

    public void setXs(String xs) {
        this.xs = xs;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getFXm() {
        return fXm;
    }

    public void setFXm(String fXm) {
        this.fXm = fXm;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
