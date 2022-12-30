package com.tmax.ast.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.tmax.ast.dto.BlockDTO;
import com.tmax.ast.dto.ClassDTO;
import com.tmax.ast.dto.ImportDTO;
import com.tmax.ast.dto.PackageDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertService {
    private final List<BlockDTO> blockDTOList;
    private final List<PackageDTO> packageDTOList;
    private final List<ImportDTO> importDTOList;
    private final List<ClassDTO> classDTOList;
    private static Long blockId = 1L;
    private static Long packageId = 1L;
    private static Long importId = 1L;
    private static Long classId = 1L;
    public ConvertService() {
        this.blockDTOList = new ArrayList<>();
        this.packageDTOList = new ArrayList<>();
        this.importDTOList = new ArrayList<>();
        this.classDTOList = new ArrayList<>();
    }
    public List<BlockDTO> getBlockDTOList() {
        return this.blockDTOList;
    }
    public List<PackageDTO> getPackageDTOList() {
        return this.packageDTOList;
    }
    public List<ImportDTO> getImportDTOList() {
        return this.importDTOList;
    }
    public List<ClassDTO> getClassDTOList() {
        return this.classDTOList;
    }

    public void visit(Node node) {
        BlockDTO cuBlockDTO = visitAndBuildRootBlock((CompilationUnit) node);
        visitAndBuild(node, cuBlockDTO);

//        List<PropertyMetaModel> attributes = nodeMetaModel.getAllPropertyMetaModels().stream()
//                .filter(PropertyMetaModel::isAttribute)
//                .filter(PropertyMetaModel::isSingular)
//                .collect(Collectors.toList());
//
//        if(!attributes.isEmpty()) {
//            System.out.println("\n["+nodeType + "]");
//            for(PropertyMetaModel attribute : attributes) {
//                System.out.println(attribute.getName()+"/"+attribute.getType().getSimpleName()+"/"+attribute.getValue(node));
//            }
//        }
    }

    private BlockDTO visitAndBuildRootBlock(CompilationUnit cu) {
        String nodeType = cu.getMetaModel().getTypeName();
        BlockDTO blockDTO;
        PackageDTO packageDTO = null;
        ImportDTO importDTO;
        ClassDTO classDTO;
        // 전체 소스코드 블락 처리
        blockDTO = buildBlockDTO(blockId, 1, null, nodeType, cu);
        blockId += 1;
        blockDTOList.add(blockDTO);
        // import, package, class dto 생성
        List<Node> childNodes = cu.getChildNodes();
        for(Node node : childNodes) {
            String childNodeType = node.getMetaModel().getTypeName();
            switch (childNodeType) {
                case "PackageDeclaration":
                    packageDTO = buildPackageDTO(packageId, blockDTO.getBlockId(), node);
                    packageId += 1;
                    packageDTOList.add(packageDTO);
                    break;
                case "ImportDeclaration":
                    importDTO = buildImportDTO(importId, blockDTO.getBlockId(), node);
                    importId += 1;
                    importDTOList.add(importDTO);
                    break;
                case "ClassOrInterfaceDeclaration":
                    if(packageDTO != null) {
                        classDTO = buildClassDTO(classId, blockDTO.getBlockId(), packageDTO.getPackageId(), node);
                        classId += 1;
                        classDTOList.add(classDTO);
                    }
                    break;
            }
        }
        return blockDTO;
    }

    private void visitAndBuild(Node node, BlockDTO parentBlockDTO) {
        BlockDTO blockDTO;
        String nodeType = node.getMetaModel().getTypeName();
        if(nodeType.equals("ClassOrInterfaceDeclaration")) {
            blockDTO = buildBlockDTO(blockId, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
            blockId += 1;
            blockDTOList.add(blockDTO);
        } else if(nodeType.equals("BlockStmt")) {
            blockDTO = buildBlockDTO(blockId, parentBlockDTO.getDepth()+1, parentBlockDTO.getBlockId(), nodeType, node);
            blockId += 1;
            blockDTOList.add(blockDTO);
        } else {
            blockDTO = parentBlockDTO;
        }
        List<Node> childNodes = node.getChildNodes();
        for(Node childNode :  childNodes) {
            visitAndBuild(childNode, blockDTO);
        }
    }

    public BlockDTO buildBlockDTO(Long blockId, Integer depth, Long ParentBlockId, String blockType, Node blockNode) {
        BlockDTO blockDTO = new BlockDTO();

        blockDTO.setBlockId(blockId);
        blockDTO.setDepth(depth);
        blockDTO.setParentBlockId(ParentBlockId);
        blockDTO.setBlockType(blockType);
        blockDTO.setBlockNode(blockNode);

        return blockDTO;
    }

    private PackageDTO buildPackageDTO(Long packageId, Long blockId, Node node) {
        PackageDTO packageDTO = new PackageDTO();

        String nodeValue = node.toString();
        nodeValue = nodeValue.replace(";","");
        nodeValue = nodeValue.replace("package", "");
        nodeValue = nodeValue.trim();

        packageDTO.setPackageId(packageId);
        packageDTO.setBlockId(blockId);
        packageDTO.setName(nodeValue);

        return packageDTO;
    }

    private ImportDTO buildImportDTO(Long importId, Long blockId, Node node) {
        ImportDTO importDTO = new ImportDTO();

        String nodeValue = node.toString();
        nodeValue = nodeValue.replace(";","");
        nodeValue = nodeValue.replace("import", "");
        nodeValue = nodeValue.trim();

        importDTO.setImportId(importId);
        importDTO.setBlockId(blockId);
        importDTO.setName(nodeValue);

        return importDTO;
    }

    private ClassDTO buildClassDTO(Long classId, Long blockId, Long packageId ,Node node) {
        ClassDTO classDTO = new ClassDTO();

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) node;
        String classType = "class";
        boolean isImplemented = false;
        String implementClass = "";
        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String className = classOrInterfaceDeclaration.getNameAsString();

        NodeList<Modifier> modifiers = classOrInterfaceDeclaration.getModifiers();
        if(!classOrInterfaceDeclaration.getImplementedTypes().isEmpty()) {
            isImplemented = true;
            implementClass = classOrInterfaceDeclaration.getImplementedTypes().get(0).asString();
        }
        for(Modifier modifier : modifiers) {
            // 접근 제어자 분별
            if(modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PRIVATE) ) {
                accessModifierKeyword = modifier.getKeyword().asString();
            } else {
                modifierKeyword = modifier.getKeyword().asString();
            }

        }
        if(classOrInterfaceDeclaration.isInterface()) {
            classType = "interface";
        }

        classDTO.setClassId(classId);
        classDTO.setBlockId(blockId);
        classDTO.setPackageId(packageId);
        classDTO.setName(className);
        classDTO.setModifier(modifierKeyword);
        classDTO.setAccessModifier(accessModifierKeyword);
        classDTO.setType(classType);
        classDTO.setIsImplemented(isImplemented);
        classDTO.setImplementClass(implementClass);

        return classDTO;
    }
}
