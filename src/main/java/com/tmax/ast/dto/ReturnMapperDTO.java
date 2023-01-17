package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;

public class ReturnMapperDTO {
    private Long returnMapperId;
    private Long methodDeclId;
    private Long typeClassId;
    private String type;
    private Node node;
    private Position position;

    public Long getReturnMapperId() {
        return returnMapperId;
    }

    public void setReturnMapperId(Long returnMapperId) {
        this.returnMapperId = returnMapperId;
    }

    public Long getMethodDeclId() {
        return methodDeclId;
    }

    public void setMethodDeclId(Long methodDeclId) {
        this.methodDeclId = methodDeclId;
    }

    public Long getTypeClassId() {
        return typeClassId;
    }

    public void setTypeClassId(Long typeClassId) {
        this.typeClassId = typeClassId;
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
        return "ReturnMapperDTO{" +
                "returnMapperId: " + returnMapperId +
                ", methodDeclId: " + methodDeclId +
                ", typeClassId: " + typeClassId +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }
}
