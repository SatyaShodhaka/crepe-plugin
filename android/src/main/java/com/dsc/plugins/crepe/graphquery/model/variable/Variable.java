package com.dsc.plugins.crepe.graphquery.model.variable;

import java.io.Serializable;

/**
 * @author toby
 * @date 7/11/16
 * @time 4:53 PM
 */
public class Variable implements Serializable {
    private String name;
    private VariableContext variableContext;
    final static public int USER_INPUT = 1, LOAD_RUNTIME = 2;
    private int type;

    public Variable(String name) {
        super();
        type = USER_INPUT;
        this.name = name;
    }

    public Variable(int type) {
        super();
        this.type = type;
    }

    public Variable(int type, String name) {
        super();
        this.type = type;
        this.name = name;
    }

    public void setVariableContext(VariableContext variableContext) {
        this.variableContext = variableContext;
    }

    public VariableContext getVariableContext() {
        return variableContext;
    }

    public int getVariableType() {
        return type;
    }

    public Variable() {
        type = USER_INPUT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}