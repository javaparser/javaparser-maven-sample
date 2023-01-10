package com.tmax.ast.dto;

public class ClassDTO{
    private Long classId;
    private Long packageId;
    private String name;
    private String modifier;
    private String accessModifier;
    private Long blockId;
    private String type;
    private Boolean isImplemented;
    private String implementClass;
    private Position position;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
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

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsImplemented() {
        return isImplemented;
    }

    public void setIsImplemented(Boolean implemented) {
        isImplemented = implemented;
    }

    public String getImplementClass() {
        return implementClass;
    }

    public void setImplementClass(String implementClass) {
        this.implementClass = implementClass;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ClassDTO : {" +
                "classId : " + classId +
                ", packageId : " + packageId +
                ", blockId : " + blockId +
                ", name : '" + name + '\'' +
                ", modifier : '" + modifier + '\'' +
                ", accessModifier : '" + accessModifier + '\'' +
                ", type : '" + type + '\'' +
                ", isImplemented : " + isImplemented +
                ", implementClass : '" + implementClass + '\'' +
                ", Position : '" + position +
                "}\n";
    }
}
