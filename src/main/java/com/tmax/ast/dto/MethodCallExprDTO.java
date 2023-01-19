package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;

import java.util.List;

public class MethodCallExprDTO {
    private String name;
    private Long blockId;
    private Long methodCallExprId;
    private Long nameExprTypeClassId;
    private Position position;
    private String nameExpr;
    private List<ArgumentDTO> arguments;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getBlockId() {
        return blockId;
    }
    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }
    public Long getMethodCallExprId() {
        return methodCallExprId;
    }
    public void setMethodCallExprId(Long methodCallExprId) {
        this.methodCallExprId = methodCallExprId;
    }
    public Long getNameExprTypeClassId() {
        return nameExprTypeClassId;
    }
    public void setNameExprTypeClassId(Long nameExprTypeClassId) {
        this.nameExprTypeClassId = nameExprTypeClassId;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }

    public List<ArgumentDTO> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentDTO> arguments) {
        this.arguments = arguments;
    }

    public String getNameExpr() {
        return nameExpr;
    }
    public void setNameExpr(String nameExpr) {
        this.nameExpr = nameExpr;
    }

    @Override
    public String toString() {
        return "MethodCallExprDTO : {" +
                "methodCallExprId : " + methodCallExprId +
                ", blockId : " + blockId +
                ", nameExprTypeClassId : " + nameExprTypeClassId +
                ", name : '" + name + '\'' +
                ", arguments : '" + arguments + '\'' +
                ", Position : '" + position +
                ", NameExpr : " + nameExpr +
                "}\n";
    }

}