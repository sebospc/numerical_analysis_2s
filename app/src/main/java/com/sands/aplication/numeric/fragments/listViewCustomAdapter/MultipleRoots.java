package com.sands.aplication.numeric.fragments.listViewCustomAdapter;

/**
 * Created by stivenramireza on 22/04/2018.
 */

public class MultipleRoots {
    private final String n;
    private final String xn;
    private final String fXn;
    private final String fdXn;
    private final String fd2Xn;
    private final String error;

    public MultipleRoots(String n, String xn, String fXn, String fdXn, String fd2Xn, String error) {
        this.n = n;
        this.xn = xn;
        this.fXn = fXn;
        this.fdXn = fdXn;
        this.fd2Xn = fd2Xn;
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

    public String getFD2Xn() {
        return fd2Xn;
    }

    public String getError() {
        return error;
    }

}
