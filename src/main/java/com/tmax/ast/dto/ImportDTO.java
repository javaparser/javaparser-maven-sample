package com.tmax.ast.dto;

public class ImportDTO{
    private Long importId;
    private Long blockId;
    private String name;
    private Position position;

    public Long getImportId() {
        return importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
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
        return "ImportDTO :{" +
                "importId : " + importId +
                ", blockId : " + blockId +
                ", name : '" + name + '\'' +
                ", Position : '" + position +
                "}\n";
    }
}
