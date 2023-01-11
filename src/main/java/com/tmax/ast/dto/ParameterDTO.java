package com.tmax.ast.dto;

public class ParameterDTO {
    private Long parameterId;
    private Long methodDeclId;
    private Integer index;
    private String name;
    private String type;
    private Position position;

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public Long getMethodDeclId() {
        return methodDeclId;
    }

    public void setMethodDeclId(Long methodDeclId) {
        this.methodDeclId = methodDeclId;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ParameterDTO{" +
                "parameterId: " + parameterId +
                ", methodDeclarationId: " + methodDeclId +
                ", index: " + index +
                ", name: '" + name + '\'' +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }
}
