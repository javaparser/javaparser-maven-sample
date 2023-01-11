package com.tmax.ast.dto;

public class ReturnMapperDTO {
    private Long returnMapperId;
    private Long methodDeclId;
    private Long classId;
    private String type;
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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
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
        return "ReturnMapperDTO{" +
                "returnMapperId: " + returnMapperId +
                ", methodDeclId: " + methodDeclId +
                ", classId: " + classId +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }
}
