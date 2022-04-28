package com.app.kuliga.data.api.entity;

import org.simpleframework.xml.Root;

@Root(name = "done", strict = false)
public class AuthEntity {

    boolean state = false;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AuthEntity{" +
                "state=" + state +
                '}';
    }
}
