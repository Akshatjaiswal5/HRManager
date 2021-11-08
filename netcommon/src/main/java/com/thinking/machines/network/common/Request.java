package com.thinking.machines.network.common;

import java.io.*;

public class Request implements Serializable {

    private String manager;
    private String action;
    private Object[] arguments;

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object ...arguments) {
        this.arguments = arguments;
    }
}