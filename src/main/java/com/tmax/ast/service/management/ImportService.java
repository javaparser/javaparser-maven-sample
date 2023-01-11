package com.tmax.ast.service.management;

import com.github.javaparser.ast.Node;
import com.tmax.ast.dto.ImportDTO;
import com.tmax.ast.dto.Position;

import java.util.ArrayList;
import java.util.List;

public class ImportService {

    private final List<ImportDTO> importDTOList;

    public ImportService() {
        this.importDTOList = new ArrayList<>();
    }

    public List<ImportDTO> getImportDTOList() {
        return this.importDTOList;
    }

    public void importListClear() {
        this.importDTOList.clear();
    }

    public void buildImport(Long importId, Long blockId, Node node) {
        ImportDTO importDTO = new ImportDTO();

        String nodeValue = node.toString();
        nodeValue = nodeValue.replace(";","");
        nodeValue = nodeValue.replace("import", "");
        nodeValue = nodeValue.trim();

        importDTO.setImportId(importId);
        importDTO.setBlockId(blockId);
        importDTO.setName(nodeValue);
        importDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        importDTOList.add(importDTO);

    }
}
