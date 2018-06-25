package com.example.sacrew.numericov4.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class FalsePosition {
    private final String n;
    private final String xi;
    private final String xs;
    private final String xm;
    private final String fXm;
    private final String error;

    public FalsePosition(String n, String xi, String xs, String xm, String fXm, String error) {
        this.n = n;
        this.xi = xi;
        this.xs = xs;
        this.xm = xm;
        this.fXm = fXm;
        this.error = error;
    }

    public String getN() {
        return n;
    }

    public String getXi() {
        return xi;
    }

    public String getXs() {
        return xs;
    }

    public String getXm() {
        return xm;
    }

    public String getFXm() {
        return fXm;
    }

    public String getError() {
        return error;
    }

}
