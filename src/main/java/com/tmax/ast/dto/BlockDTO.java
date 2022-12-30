package com.tmax.ast.dto;

import com.github.javaparser.ast.Node;

public class BlockDTO {
    private Long blockId;
    private Long parentBlockId;
    private Integer depth;
    private String blockType;
    private Node blockNode;

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getParentBlockId() {
        return parentBlockId;
    }

    public void setParentBlockId(Long parentBlockId) {
        this.parentBlockId = parentBlockId;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public Node getBlockNode() {
        return blockNode;
    }

    public void setBlockNode(Node blockNode) {
        this.blockNode = blockNode;
    }

    @Override
    public String toString() {
        return "BlockDTO : {" +
                "blockId : " + blockId +
                ", parentBlockId : " + parentBlockId +
                ", depth : " + depth +
                ", blockType : '" + blockType + '\'' +
                "}";
//                ", \nblockNode : \n" + blockNode +
//                "}\n";
    }
}
