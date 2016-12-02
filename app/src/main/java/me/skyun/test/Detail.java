package me.skyun.test;

import java.io.Serializable;

/**
 * Created by linyun on 16/12/2.
 */

public class Detail implements Serializable {
    public Detail(String s) {
        this.s = s;
    }

    public String s;
}
