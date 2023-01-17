package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;

import java.util.List;

public class MethodDeclarationDTO {
    private Long methodDeclId;
    private Long blockId;
    private Long belongedClassId;
    private String name;
    private String modifier;
    private String accessModifier;
    private ReturnMapperDTO returnMapper;
    private List<ParameterDTO> parameters;
    private Node node;
    private Position position;

    public Long getMethodDeclId() {
        return methodDeclId;
    }

    public void setMethodDeclId(Long methodDeclId) {
        this.methodDeclId = methodDeclId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getBelongedClassId() {
        return belongedClassId;
    }

    public void setBelongedClassId(Long belongedClassId) {
        this.belongedClassId = belongedClassId;
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

    public ReturnMapperDTO getReturnMapper() {
        return returnMapper;
    }

    public void setReturnMapper(ReturnMapperDTO returnMapper) {
        this.returnMapper = returnMapper;
    }

    public List<ParameterDTO> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterDTO> parameters) {
        this.parameters = parameters;
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
        return "MethodDeclarationDTO{" +
                "methodDeclarationId: " + methodDeclId +
                ", blockId: " + blockId +
                ", belongedClassId: " + belongedClassId +
                ", name: '" + name + '\'' +
                "', nodeType: '" + node.getMetaModel().getTypeName() +
                ", modifier: '" + modifier + '\'' +
                ", accessModifier: '" + accessModifier + '\'' +
                ", returnMappers: " + returnMapper +
                ", parameters: " + parameters +
                ", position: " + position +
                "}\n";
    }
}
