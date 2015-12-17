package com.hengye.share.module;

import java.io.Serializable;

public class ParentInherit implements Serializable{

    private static final long serialVersionUID = -7844490464382581467L;

    private Parent parent;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
