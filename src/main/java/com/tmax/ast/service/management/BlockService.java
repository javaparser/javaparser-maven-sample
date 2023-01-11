package com.tmax.ast.service.management;

import com.github.javaparser.ast.Node;
import com.tmax.ast.dto.*;

import java.util.ArrayList;
import java.util.List;


public class BlockService {

    private final List<BlockDTO> blockDTOList;

    public BlockService() {
        this.blockDTOList = new ArrayList<>();
    }

    public List<BlockDTO> getBlockDTOList() {
        return this.blockDTOList;
    }

    public void blockListClear() {
        this.blockDTOList.clear();
    }

    public BlockDTO buildBlock(Long blockId, Integer depth, Long ParentBlockId, String blockType, Node node) {
        BlockDTO blockDTO = new BlockDTO();

        blockDTO.setBlockId(blockId);
        blockDTO.setDepth(depth);
        blockDTO.setParentBlockId(ParentBlockId);
        blockDTO.setBlockType(blockType);
        blockDTO.setBlockNode(node);
        blockDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        blockDTOList.add(blockDTO);

        return blockDTO;
    }
}
