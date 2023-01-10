package com.tmax.ast.dto;

public class PackageDTO{
    private Long packageId;
    private Long blockId;
    private String name;
    private Position position;

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "PackageDTO : {" +
                "packageId : " + packageId +
                ", blockId : " + blockId +
                ", name : '" + name + '\'' +
                ", Position : '" + position +
                "}\n";
    }
}
