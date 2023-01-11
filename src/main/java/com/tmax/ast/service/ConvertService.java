package com.tmax.ast.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import com.tmax.ast.dto.*;

import java.util.*;

public class ConvertService {
    private final List<BlockDTO> blockDTOList;
    private final List<PackageDTO> packageDTOList;
    private final List<ImportDTO> importDTOList;
    private final List<ClassDTO> classDTOList;
    private final List<VariableDeclarationDTO> variableDeclarationDTOList;
    private final List<FunctionDeclarationDTO> functionDeclarationDTOList;
    private static Long blockId = 1L;
    private static Long packageId = 1L;
    private static Long importId = 1L;
    private static Long classId = 1L;
    private static Long variableDeclarationId = 1L;
    private static Long functionDeclarationId = 1L;
    private static Long parameterId = 1L;
    public ConvertService() {
        this.blockDTOList = new ArrayList<>();
        this.packageDTOList = new ArrayList<>();
        this.importDTOList = new ArrayList<>();
        this.classDTOList = new ArrayList<>();
        this.variableDeclarationDTOList = new ArrayList<>();
        this.functionDeclarationDTOList = new ArrayList<>();
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
    public List<VariableDeclarationDTO> getVariableDeclarationDTOList() {
        return this.variableDeclarationDTOList;
    }
    public List<FunctionDeclarationDTO> getFunctionDeclarationDTOList() {
        return this.functionDeclarationDTOList;
    }

    public void clear() {
        this.blockDTOList.clear();
        this.packageDTOList.clear();
        this.importDTOList.clear();
        this.classDTOList.clear();
        this.variableDeclarationDTOList.clear();
        this.functionDeclarationDTOList.clear();
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

    public void visitVariablesAndBuildClassId() {
        for(VariableDeclarationDTO variableDeclarationDTO : variableDeclarationDTOList) {
            // 선언한 변수가 primitive(원시) 타입이면 class id 는 0으로
            if(variableDeclarationDTO.getVariableType().isPrimitiveType()) {
                continue;
            }
            // 1. 패키지를 import 해서 사용하는 클래스 변수인지 체크
            isImportPackage(variableDeclarationDTO);

            // 2. 그게 아니라면, 같은 프로젝트 내에 존재하는 클래스 변수
            isProjectPackage(variableDeclarationDTO);
        }
    }

    private void isImportPackage(VariableDeclarationDTO variableDeclarationDTO) {
        List<ImportDTO> imported = new ArrayList<>();
        if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
            System.out.println("'" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
            return;
        }
        String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
        System.out.println("[isImportPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

        Long findBlockId = getBlockIdByVariable(variableDeclarationDTO);

        // TODO : import 패키지 중 *로 선언하는 것이 있으면 해당 라이브러리 내 모든 패키지->소스코드들을 다 까봐야 한다.

        // import 패키지의 클래스라면 import Id를 추가
        for(ImportDTO importDTO : importDTOList) {
            String[] importPkg = importDTO.getName().split("\\.");
            if(importDTO.getBlockId().equals(findBlockId) && variableTypeName.equals(importPkg[importPkg.length-1])){
                imported.add(importDTO);
            }
        }

        if(imported.size() == 1) {
            System.out.println("[isImportPackage] : import 에서 '" + imported.get(0).getName() + "'를 발견했습니다.");
             variableDeclarationDTO.setImportId(imported.get(0).getImportId());

            String[] importPkg = imported.get(0).getName().split("\\.");
            StringBuilder importPkgPath = new StringBuilder();
            for(int i = 0; i < importPkg.length-1; i++) {
                importPkgPath.append(importPkg[i]);
                if(i != importPkg.length-2) {
                    importPkgPath.append(".");
                }
            }
            String importPkgClass = importPkg[importPkg.length-1];

            for(PackageDTO packageDTO : packageDTOList) {
                if(packageDTO.getName().equals(importPkgPath.toString())) {
                    for(ClassDTO classDTO : classDTOList) {
                        if(classDTO.getName().equals(importPkgClass)) {
                            System.out.println("[isImportPackage] : '"+ imported.get(0).getName() +"'에 대한 클래스를 발견하여 '"+ variableDeclarationDTO.getName() +"'의 class id '" + classDTO.getClassId() +"'를 부여합니다");
                            variableDeclarationDTO.setClassId(classDTO.getClassId());
                            break;
                        }
                    }
                }
            }

        } else if(imported.size() > 1){
            System.out.println("[isImportPackage] : 예상되는 import 패키지가 너무 많습니다.");
            for(ImportDTO importDTO : imported) {
                System.out.print("[isImportPackage] : [Expected]: "+importDTO);
            }
        } else {
            System.out.println("[isImportPackage] : import 패키지를 찾지 못했습니다.");
        }
    }

    private void isProjectPackage(VariableDeclarationDTO variableDeclarationDTO) {
        if(variableDeclarationDTO.getClassId().equals(0L)) {
            // 선언한 변수가 속한 패키지를 찾는다
            if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
                System.out.println("'" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
                return;
            }
            String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
            System.out.println("[isProjectPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

            Long findBlockId = getBlockIdByVariable(variableDeclarationDTO);

            PackageDTO packageDTO = packageDTOList.stream()
                    .filter(pkg -> pkg.getBlockId().equals(findBlockId))
                    .findFirst()
                    .orElseGet(PackageDTO::new);

            if(packageDTO.getPackageId() == null) {
                return;
            }
            // 현재는 같은 패키지 이름을 가졌더라도 소스코드 별로 패키지가 생셩되기 때문에 list 로 담아뒀다.
            List<PackageDTO> packaged = new ArrayList<>();
            for(PackageDTO find : packageDTOList) {
                if(packageDTO.getName().equals(find.getName())) {
                    packaged.add(find);
                }
            }

            boolean isFind = false;
            // 해당 패키지에서 선언한 클래스를 찾는다
            for(PackageDTO pkg : packaged) {
                for(ClassDTO cls : classDTOList) {
                    // 클래스가 속한 패키지 중에서 그 클래스 이름이 선언한 변수와 같을 때, 클래스 id 부여
                    if(pkg.getPackageId().equals(cls.getPackageId()) && cls.getName().equals(variableTypeName)) {
                        System.out.println("[isProjectPackage] : '"+ pkg.getName() + "." + cls.getName() +"'에 대한 클래스를 발견하여 '"+ variableDeclarationDTO.getName() +"'의 class id '" + cls.getClassId() +"'를 부여합니다");
                        variableDeclarationDTO.setClassId(cls.getClassId());
                        isFind = true;
                        break;
                    }
                }
            }
            if(!isFind) {
                System.out.println("[isProjectPackage] : project 패키지를 찾지 못했습니다.");
            }
        }
    }

    // getBlockIdByVariable: 변수가 선언된 소스코드의 최상단 블락의 위치를 반환 (변수가 선언된 블락 id를 가지고, 해당 블락의 최상단 블락을 찾아 리턴)
    private Long getBlockIdByVariable(VariableDeclarationDTO variableDeclarationDTO) {
        Long findBlockId = variableDeclarationDTO.getBlockId();
        while(true) {
            Long finalFindBlockId = findBlockId;
            BlockDTO blockDTO = blockDTOList.stream()
                    .filter(blockDTO1 -> blockDTO1.getBlockId().equals(finalFindBlockId))
                    .findFirst()
                    .orElseGet(BlockDTO::new);
            if(blockDTO.getParentBlockId() == null) {
                break;
            }
            findBlockId = blockDTO.getParentBlockId();
        }
        return findBlockId;
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
                case "ModuleDeclaration":
                    // 모듈이 뭔지 잘 모르겠으나 해당 준위에 포함됨.
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
                case "EnumDeclaration":
                    classDTO = buildEnum(classId, blockDTO.getBlockId(), packageDTO.getPackageId(), node);
                    classId += 1;
                    classDTOList.add(classDTO);
                    break;
                // 커스텀 어노테이션 제작 시
                case "AnnotationDeclaration":
                    break;
                // 무슨 새로운 class type 이라는데 처음봄...
                case "RecordDeclaration":
                    break;
            }
        }
        return blockDTO;
    }

    private void visitAndBuild(Node node, BlockDTO parentBlockDTO) {
        BlockDTO blockDTO;
        String nodeType = node.getMetaModel().getTypeName();
        if (nodeType.equals("ClassOrInterfaceDeclaration") || nodeType.equals("EnumDeclaration") ||
                nodeType.equals("AnnotationDeclaration") || nodeType.equals("RecordDeclaration")) {
            blockDTO = buildBlockDTO(blockId, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
            blockId += 1;
            blockDTOList.add(blockDTO);
        }
        // 클래스 바로 아래에서 변수를 선언하는 멤버 필드
        else if(nodeType.equals("FieldDeclaration")) {
            blockDTO = parentBlockDTO;
            buildVariableDeclInMemberField(variableDeclarationId++, blockDTO.getBlockId(), node);
        }
        // 함수 내에서 선언하는 변수
        else if(nodeType.equals("VariableDeclarationExpr")) {
            blockDTO = parentBlockDTO;
            buildVariableDeclInMethod(variableDeclarationId++, blockDTO.getBlockId(), node);
        }
        // || nodeType.equals("ConstructorDeclaration")
        else if(nodeType.equals("MethodDeclaration")) {
            // 내부에 BlockStmt 가 존재하여 별도의 Block 을 생성하지는 않음
            // 현재 상위 block 에서 선언되는 것들.
            blockDTO = parentBlockDTO;
            // 함수 및 생성자 선언 시 build
            buildFunctionDecl(functionDeclarationId++, blockDTO.getBlockId(), node, nodeType);
        }
        else if(nodeType.equals("BlockStmt")) {
            blockDTO = buildBlockDTO(blockId, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
            blockId += 1;
            blockDTOList.add(blockDTO);
        }
        else {
            blockDTO = parentBlockDTO;
        }
        List<Node> childNodes = node.getChildNodes();
        for(Node childNode :  childNodes) {
            if(!childNode.getMetaModel().getTypeName().equals("SimpleName")) {
                visitAndBuild(childNode, blockDTO);
            }
        }
    }

    private void buildFunctionDecl(Long functionDeclarationId, Long blockId, Node node, String nodeType) {
        FunctionDeclarationDTO functionDeclarationDTO = new FunctionDeclarationDTO();
        ReturnMapperDTO returnMapperDTO = new ReturnMapperDTO();
        List<ParameterDTO> parameters = new ArrayList<>();
        List<Node> childNodes = node.getChildNodes();

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String functionName = "";

        Long returnId = 1L;
        Integer parameterIndex = 1;

        for(Node childNode : childNodes) {
            String childNodeTypeName = childNode.getMetaModel().getTypeName();
            if(childNodeTypeName.equals("Modifier")) {
                Modifier modifier = (Modifier) childNode;
                // 접근 제어자 분별
                if(modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PRIVATE) ) {
                    accessModifierKeyword = modifier.getKeyword().asString();
                } else {
                    modifierKeyword = modifier.getKeyword().asString();
                }
            } else if(childNodeTypeName.equals("SimpleName")) {
                SimpleName simpleName = (SimpleName) childNode;
                functionName = simpleName.asString();
            } else if(childNodeTypeName.equals("Parameter")) {
                ParameterDTO parameterDTO = new ParameterDTO();

                Parameter parameterNode = (Parameter) childNode;

                parameterDTO.setParameterId(parameterId++);
                parameterDTO.setFunctionId(functionDeclarationId);
                parameterDTO.setIndex(parameterIndex++);
                parameterDTO.setName(parameterNode.getName().asString());
                parameterDTO.setType(parameterNode.getType().asString());
                parameterDTO.setPosition(
                        new Position(
                                parameterNode.getRange().get().begin.line,
                                parameterNode.getRange().get().begin.column,
                                parameterNode.getRange().get().end.line,
                                parameterNode.getRange().get().end.column
                        )
                );

                parameters.add(parameterDTO);


            } else if(childNodeTypeName.matches("(.*)Type")) {

                returnMapperDTO.setReturnMapperId(returnId++);
                returnMapperDTO.setFunctionId(functionDeclarationId);
                returnMapperDTO.setClassId(0L);
                returnMapperDTO.setType(childNodeTypeName);
                returnMapperDTO.setPosition(
                        new Position(
                                childNode.getRange().get().begin.line,
                                childNode.getRange().get().begin.column,
                                childNode.getRange().get().end.line,
                                childNode.getRange().get().end.column
                        )
                );

            }
        }

        functionDeclarationDTO.setFunctionId(functionDeclarationId);
        functionDeclarationDTO.setBlockId(blockId);
        functionDeclarationDTO.setName(functionName);
        functionDeclarationDTO.setModifier(modifierKeyword);
        functionDeclarationDTO.setAccessModifier(accessModifierKeyword);
        // add to functionDeclarationDTO
        functionDeclarationDTO.setReturnMapper(returnMapperDTO);
        functionDeclarationDTO.setParameters(parameters);

        functionDeclarationDTO.setNode(node);
        functionDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        functionDeclarationDTOList.add(functionDeclarationDTO);
    }


    private void buildVariableDeclInMethod(Long variableId, Long blockId, Node node) {
        VariableDeclarationDTO variableDeclarationDTO = new VariableDeclarationDTO();
        VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) node;

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        Type variableType = null;
        String type = "";
        String name = "";
        Optional<Expression> initializer = Optional.empty();
        Long classId = 0L;
        Long importId = 0L;

        // 변수 제어자
        NodeList<Modifier> modifiers = variableDeclarationExpr.getModifiers();
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
        // 변수 이름, 타입
        NodeList<VariableDeclarator> variableDeclarators = variableDeclarationExpr.getVariables();
        for(VariableDeclarator variableDeclarator : variableDeclarators) {
            variableType = variableDeclarator.getType();
            // type = variableType.getChildNodes().stream().collect(Collectors.toList()).toString();
            type = variableDeclarator.getType().asString();
            name = variableDeclarator.getName().asString();
            initializer = variableDeclarator.getInitializer();
        }

        variableDeclarationDTO.setVariableId(variableId);
        variableDeclarationDTO.setBlockId(blockId);
        variableDeclarationDTO.setClassId(classId);
        variableDeclarationDTO.setImportId(importId);
        variableDeclarationDTO.setVariableType(variableType);
        variableDeclarationDTO.setType(type);
        variableDeclarationDTO.setName(name);
        variableDeclarationDTO.setModifier(modifierKeyword);
        variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
        variableDeclarationDTO.setInitializer(initializer);
        variableDeclarationDTO.setNode(node);
        variableDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        variableDeclarationDTOList.add(variableDeclarationDTO);
    }

    private void buildVariableDeclInMemberField(Long variableId, Long blockId, Node node) {
        VariableDeclarationDTO variableDeclarationDTO = new VariableDeclarationDTO();
        FieldDeclaration fieldDeclaration = (FieldDeclaration) node;

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        Type variableType = null;
        String type = "";
        String name = "";
        Optional<Expression> initializer = Optional.empty();
        Long classId = 0L;
        Long importId = 0L;

        // 변수 제어자
        NodeList<Modifier> modifiers = fieldDeclaration.getModifiers();
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
        // 변수 이름, 타입
        NodeList<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
        for(VariableDeclarator variableDeclarator : variableDeclarators) {
            variableType = variableDeclarator.getType();
            // type = variableType.getChildNodes().stream().collect(Collectors.toList()).toString();
            type = variableDeclarator.getType().asString();
            name = variableDeclarator.getName().asString();
            initializer = variableDeclarator.getInitializer();
        }

        variableDeclarationDTO.setVariableId(variableId);
        variableDeclarationDTO.setBlockId(blockId);
        variableDeclarationDTO.setClassId(classId);
        variableDeclarationDTO.setImportId(importId);
        variableDeclarationDTO.setVariableType(variableType);
        variableDeclarationDTO.setType(type);
        variableDeclarationDTO.setName(name);
        variableDeclarationDTO.setModifier(modifierKeyword);
        variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
        variableDeclarationDTO.setInitializer(initializer);
        variableDeclarationDTO.setNode(node);
        variableDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        variableDeclarationDTOList.add(variableDeclarationDTO);
    }

    public BlockDTO buildBlockDTO(Long blockId, Integer depth, Long ParentBlockId, String blockType, Node node) {
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
        packageDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

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
        importDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        return importDTO;
    }

    private ClassDTO buildClassDTO(Long classId, Long blockId, Long packageId ,Node node) {
        ClassDTO classDTO = new ClassDTO();

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) node;
        boolean isImplemented = false;
        String implementClass = "";
        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String className = classOrInterfaceDeclaration.getNameAsString();
        String classType;

        NodeList<Modifier> modifiers = classOrInterfaceDeclaration.getModifiers();
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
        // 인터페이스 구현체 여부
        if(!classOrInterfaceDeclaration.getImplementedTypes().isEmpty()) {
            isImplemented = true;
            implementClass = classOrInterfaceDeclaration.getImplementedTypes().get(0).asString();
        }
        // 인터페이스 클래스 여부
        if(classOrInterfaceDeclaration.isInterface()) {
            classType = "interface";
        } else {
            classType = "class";
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
        classDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );


        return classDTO;
    }

    private ClassDTO buildEnum(Long classId, Long blockId, Long packageId, Node node) {
        ClassDTO classDTO = new ClassDTO();

        EnumDeclaration enumDeclaration = (EnumDeclaration) node;
        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String className = enumDeclaration.getNameAsString();

        NodeList<Modifier> modifiers = enumDeclaration.getModifiers();
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

        // NodeList<EnumConstantDeclaration> enumConstantDeclarations = enumDeclaration.getEntries();

        classDTO.setClassId(classId);
        classDTO.setBlockId(blockId);
        classDTO.setPackageId(packageId);
        classDTO.setName(className);
        classDTO.setModifier(modifierKeyword);
        classDTO.setAccessModifier(accessModifierKeyword);
        // enumConstantDeclarations 를 class dto 에 넣을 만한 멤버 변수가 필요함.
        classDTO.setType("enum");
        classDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        return classDTO;
    }
}
