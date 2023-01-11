package com.tmax.ast.dto;

public class ParameterDTO {
    private Long parameterId;
    private Long functionId;
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

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
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
                "parameterId=" + parameterId +
                ", functionId=" + functionId +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", position=" + position +
                '}';
    }
}
