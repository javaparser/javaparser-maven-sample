package com.tmax.ast.service.management;

import com.github.javaparser.ast.Node;
import com.tmax.ast.dto.PackageDTO;
import com.tmax.ast.dto.Position;

import java.util.ArrayList;
import java.util.List;

public class PackageService {

    private final List<PackageDTO> packageDTOList;

    public PackageService() {
        this.packageDTOList = new ArrayList<>();
    }

    public List<PackageDTO> getPackageDTOList() {
        return this.packageDTOList;
    }

    public void packageListClear() {
        this.packageDTOList.clear();
    }

    public PackageDTO buildPackage(Long packageId, Long blockId, Node node) {
        PackageDTO packageDTO = new PackageDTO();

        String nodeValue = node.toString();
        nodeValue = nodeValue.replace(";","");
        nodeValue = nodeValue.replace("package", "");
        nodeValue = nodeValue.trim();

        packageDTO.setPackageId(packageId);
        packageDTO.setBlockId(blockId);
        packageDTO.setName(nodeValue);
        packageDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        packageDTOList.add(packageDTO);

        return packageDTO;
    }
}
