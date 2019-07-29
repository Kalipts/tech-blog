package com.coding.techblog.model;

import java.io.Serializable;

public class Relationship  implements Serializable {
    private Integer cid;

    private Integer mid;

    public Relationship() {}

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
