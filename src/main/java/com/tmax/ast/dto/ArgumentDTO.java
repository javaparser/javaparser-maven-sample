package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;

public class ArgumentDTO {
    private Long argumentId;
    private Long methodCallExprId;
    private Integer index;
    private String name;
    private String type;

    private Node node;
    private Position position;

    public Long getArgument() {
        return argumentId;
    }

    public void setArgumentId(Long argumentId) {
        this.argumentId = argumentId;
    }

    public Long getMethodCallExprId() {
        return methodCallExprId;
    }

    public void setMethodCallExprId(Long methodCallExprId) {
        this.methodCallExprId = methodCallExprId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ArgumentDTO{" +
                "argumentId: " + argumentId +
                ", methodCallExprId: " + methodCallExprId +
                ", index: " + index +
                ", name: '" + name + '\'' +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }
}
