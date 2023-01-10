package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;

import java.util.Optional;

public class VariableDTO{
    private Long variableId;
    private Long blockId;
    private Long classId;
    private Long importId;
    private String name;
    private String modifier;
    private String accessModifier;
    private String type;
    private Type variableType;
    private Node node;
    private Optional<Expression> initializer;
    private Position position;

    public Long getVariableId() {
        return variableId;
    }

    public void setVariableId(Long variableId) {
        this.variableId = variableId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Type getVariableType() {
        return variableType;
    }

    public void setVariableType(Type variableType) {
        this.variableType = variableType;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Optional<Expression> getInitializer() {
        return initializer;
    }

    public void setInitializer(Optional<Expression> initializer) {
        this.initializer = initializer;
    }

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "VariableDTO : {" +
                "variableId : " + variableId +
                ", blockId : " + blockId +
                ", classId : " + classId +
                ", importId : " + importId +
                ", name : '" + name + '\'' +
                ", modifier : '" + modifier + '\'' +
                ", accessModifier : '" + accessModifier + '\'' +
                ", type : '" + type + '\'' +
//                ", variableType : " + variableType.getClass().getSimpleName() +
//                ", node : " + node.getClass().getSimpleName() +
                ", initializer : " + initializer +
                ", Position : '" + position +
                "}\n";
    }
}
