package com.tmax.ast.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.tmax.ast.dto.*;
import com.tmax.ast.service.management.*;

import java.util.*;

import static com.tmax.ast.config.GeneratorIdentifier.*;

public class ConvertService {

    private final BlockService blockService = new BlockService();
    private final PackageService packageService = new PackageService();
    private final ImportService importService = new ImportService();
    private final ClassService classService = new ClassService();

    private final VariableService variableService = new VariableService();
    private final MethodService methodService = new MethodService();

    public List<BlockDTO> getBlockDTOList() {
        return blockService.getBlockDTOList();
    }

    public List<PackageDTO> getPackageDTOList() {
        return packageService.getPackageDTOList();
    }

    public List<ImportDTO> getImportDTOList() {
        return importService.getImportDTOList();
    }

    public List<ClassDTO> getClassDTOList() {
        return classService.getClassDTOList();
    }

    public List<MemberVariableDeclarationDTO> getMemberVariableDeclarationDTOList() {
        return variableService.getMemberVariableDeclarationDTOList();
    }

    public List<LocalVariableDeclarationDTO> getLocalVariableDeclarationDTOList() {
        return variableService.getLocalVariableDeclarationDTOList();
    }

    public List<MethodDeclarationDTO> getMethodDeclarationDTOList() {
        return methodService.getMethodDeclarationDTOList();
    }

    public List<MethodCallExprDTO> getMethodCallExprDTOList() {
        return methodService.getMethodCallExprDTOList();
    }

    public void clear() {
        blockService.blockListClear();
        packageService.packageListClear();
        importService.importListClear();
        classService.classListClear();
        variableService.variableDeclarationListClear();
        methodService.methodDeclarationListClear();
        methodService.methodCallExprListClear();
    }

    public void visit(Node node) {
        BlockDTO rootBlock = visitAndBuildRoot((CompilationUnit) node);
        visitAndBuild(node, rootBlock);
    }

    private BlockDTO visitAndBuildRoot(CompilationUnit cu) {
        String nodeType = cu.getMetaModel().getTypeName();
        BlockDTO rootBlockDTO = blockService.buildBlock(blockId++, 1, null, nodeType, cu);

        PackageDTO packageDTO = null;

        // import, package, class dto 생성
        List<Node> childNodes = cu.getChildNodes();
        for(Node node : childNodes) {
            String childNodeType = node.getMetaModel().getTypeName();
            switch (childNodeType) {
                case "PackageDeclaration":
                    packageDTO = packageService.buildPackage(packageId++, rootBlockDTO.getBlockId(), node);
                    break;
                case "ModuleDeclaration":
                    // 모듈이 뭔지 잘 모르겠으나 해당 준위에 포함됨.
                    break;
                case "ImportDeclaration":
                    importService.buildImport(importId++, rootBlockDTO.getBlockId(), node);
                    break;
                case "ClassOrInterfaceDeclaration":
                    classService.buildClass(classId++, rootBlockDTO.getBlockId(), packageDTO != null ? packageDTO.getPackageId() : 0L, node);
                    break;
                case "EnumDeclaration":
                    classService.buildEnum(classId++, rootBlockDTO.getBlockId(), packageDTO != null ? packageDTO.getPackageId() : 0L, node);
                    break;
                // 커스텀 어노테이션 제작 시
                case "AnnotationDeclaration":
                    break;
                // 무슨 새로운 class type 이라는데 처음봄...
                case "RecordDeclaration":
                    break;
            }
        }
        return rootBlockDTO;
    }

    private void visitAndBuild(Node node, BlockDTO parentBlockDTO) {
        BlockDTO blockDTO;
        String nodeType = node.getMetaModel().getTypeName();
        if (nodeType.equals("ClassOrInterfaceDeclaration") || nodeType.equals("EnumDeclaration") ||
                nodeType.equals("AnnotationDeclaration") || nodeType.equals("RecordDeclaration")) {
            blockDTO = blockService.buildBlock(blockId++, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
        }
        // 클래스 바로 아래에서 변수를 선언하는 멤버 필드
        else if(nodeType.equals("FieldDeclaration")) {
            blockDTO = parentBlockDTO;
            ClassDTO belongedClassDTO = classService.getClassDTOList().get(classService.getClassDTOList().size()-1);
            variableService.buildVariableDeclInMemberField(variableDeclarationId++, blockDTO.getBlockId(), belongedClassDTO.getClassId(), node);
        }
        // 함수 내에서 선언하는 변수
        else if(nodeType.equals("VariableDeclarationExpr")) {
            blockDTO = parentBlockDTO;
            ClassDTO belongedClassDTO = classService.getClassDTOList().get(classService.getClassDTOList().size()-1);
            variableService.buildVariableDeclInMethod(variableDeclarationId++, blockDTO.getBlockId(), belongedClassDTO.getClassId(), node);
        }
        //
        else if(nodeType.equals("MethodDeclaration") || nodeType.equals("ConstructorDeclaration")) {
            // 내부에 BlockStmt 가 존재하여 별도의 Block 을 생성하지는 않음
            // 현재 상위 block 에서 선언되는 것들.
            blockDTO = parentBlockDTO;
            // 함수 및 생성자 선언 시 build
            ClassDTO belongedClassDTO = classService.getClassDTOList().get(classService.getClassDTOList().size()-1);
            methodService.buildMethodDeclaration(methodDeclarationId++, blockDTO.getBlockId(), belongedClassDTO.getClassId(), node, nodeType);
        }
        else if(nodeType.equals("MethodCallExpr")) {
            blockDTO = parentBlockDTO;
            methodService.buildMethodCallExpr(methodCallExprId++, blockDTO.getBlockId(), node, nodeType);
        }
        else if(nodeType.equals("BlockStmt")) {
            blockDTO = blockService.buildBlock(blockId++, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
        }
        else {
            blockDTO = parentBlockDTO;
        }
        List<Node> childNodes = node.getChildNodes();
        for(Node childNode :  childNodes) {
            if(!childNode.getMetaModel().getTypeName().equals("SimpleName") ||
            !childNode.getMetaModel().getTypeName().equals("Modifier")) {
                visitAndBuild(childNode, blockDTO);
            }
        }
    }

    public void visitVariablesAndBuildClassId() {
        // TODO : 선언한 객체 변수가 다른(하위) 클래스 객체를 initialize 했을 때, initializer 타입에 맞는 클래스의 id를 부여할 수 있도록 수정
        // 클래스 member 변수
        for(MemberVariableDeclarationDTO memberVariableDeclarationDTO : getMemberVariableDeclarationDTOList()) {
            // 선언한 변수가 primitive(원시) 타입이면 class id 는 0으로
            if(memberVariableDeclarationDTO.getVariableType().isPrimitiveType()) {
                continue;
            }
            // 1. 패키지를 import 해서 사용하는 클래스 변수인지 체크
            boolean isImport = checkVariableIfImportPackage(memberVariableDeclarationDTO);

            // 2. 그게 아니라면, 같은 프로젝트 내에 존재하는 클래스 변수
            if(!isImport) {
                boolean isProject = checkVariableIfProjectPackage(memberVariableDeclarationDTO);

                if(!isProject) {
                    System.out.println("[visitVariablesAndBuildClassId] : 변수 '" + memberVariableDeclarationDTO.getName() + "'에 대한 클래스를 발견하지 못했습니다.");
                }
            }
            System.out.println();
            // TODO : 외부 패키지를 import 한 경우, 패키지 클래스를 기반으로 선언한 class id를 찾아 매핑하는 작업
        }
        // method 내 변수
        for(LocalVariableDeclarationDTO localVariableDeclarationDTO : getLocalVariableDeclarationDTOList()) {
            // 선언한 변수가 primitive(원시) 타입이면 class id 는 0으로
            if(localVariableDeclarationDTO.getVariableType().isPrimitiveType()) {
                continue;
            }
            // 1. 패키지를 import 해서 사용하는 클래스 변수인지 체크
            boolean isImport = checkVariableIfImportPackage(localVariableDeclarationDTO);

            // 2. 그게 아니라면, 같은 프로젝트 내에 존재하는 클래스 변수
            if(!isImport) {
                boolean isProject = checkVariableIfProjectPackage(localVariableDeclarationDTO);

                if(!isProject) {
                    System.out.println("[visitVariablesAndBuildClassId] : 변수 '" + localVariableDeclarationDTO.getName() + "'에 대한 클래스를 발견하지 못했습니다.");
                }
            }
            System.out.println();
            // TODO : 외부 패키지를 import 한 경우, 패키지 클래스를 기반으로 선언한 class id를 찾아 매핑하는 작업
        }
    }

    public void visitMethodsAndBuildClassId() {
        for(MethodDeclarationDTO methodDeclarationDTO : getMethodDeclarationDTOList()) {
            // return 타입에 대한 클래스 id 부여
            ReturnMapperDTO returnMapperDTO = methodDeclarationDTO.getReturnMapper();

            if(returnMapperDTO.getReturnMapperId() == null) {
                continue;
            }
            else if(returnMapperDTO.getNode().getMetaModel().getTypeName().equals("VoidType") ||
                    returnMapperDTO.getNode().getMetaModel().getTypeName().equals("PrimitiveType")) {
                continue;
            }

            boolean isImport = checkReturnIfImportPackage(returnMapperDTO, methodDeclarationDTO.getBlockId());

            if(!isImport) {
                boolean isProject = checkReturnIfProjectPackage(returnMapperDTO, methodDeclarationDTO.getBlockId());

                if(!isProject) {
                    System.out.println("[visitMethodsAndBuildClassId] : 리턴 타입 '" + returnMapperDTO.getType() + "'에 대한 클래스를 발견하지 못했습니다.");
                }
            }
            System.out.println();
            // TODO : 외부 패키지를 import 한 경우, 패키지 클래스를 기반으로 선언한 class id를 찾아 매핑하는 작업

            // 파라미터 타입에 대한 클래스 id 부여
            List<ParameterDTO> parameterDTOList = methodDeclarationDTO.getParameters();

            for(ParameterDTO parameterDTO : parameterDTOList) {
                if(parameterDTO.getParameterId() == null) {
                    continue;
                }

                Parameter parameter = (Parameter) parameterDTO.getNode();
                if(parameter.getType().isPrimitiveType()) {
                    continue;
                }

                if(!checkParameterIfImportPackage(parameterDTO, methodDeclarationDTO.getBlockId())) {
                    if(!checkParameterIfProjectPackage(parameterDTO, methodDeclarationDTO.getBlockId())) {
                        System.out.println("[visitMethodsAndBuildClassId] : 파라미터 타입 '" + parameterDTO.getType() + "'에 대한 클래스를 발견하지 못했습니다.");
                    }
                }
                System.out.println();
            }

        }
    }

    private boolean checkParameterIfImportPackage(ParameterDTO parameterDTO, Long blockId) {
        System.out.println("[checkParameterIfImportPackage] : Find Class about '" + parameterDTO.getType() + "' parameter");
        Long rootBlockId = findRootBlockIdByCurrentBlockId(blockId);

        String parameterTypeName = parameterDTO.getType().replace(">", "").split("<")[0];

        List<ImportDTO> imported = findImportsByRootBlockIdAndTypeName(rootBlockId, parameterTypeName);

        if(imported.size() == 1) {
            System.out.println("[checkParameterIfImportPackage] : import 에서 '" + imported.get(0).getName() + "'를 발견했습니다.");
            Map<String, String> importMapper = parseImportClassNameOrImportPackageName(imported.get(0));
            if(importMapper.isEmpty()){
                return false;
            }

            parameterDTO.setTypeClassId(findAndBuildClassInImportPackage(importMapper.get("packageName"), importMapper.get("className"), parameterDTO.getType()));
            return true;
        }
        else if(imported.size() > 1){
            System.out.println("[checkParameterIfImportPackage] : 예상되는 import 패키지가 너무 많습니다.");
            for(ImportDTO importDTO : imported) {
                System.out.print("[checkParameterIfImportPackage] : [Expected]: "+ importDTO);
            }
            return false;
        }
        else {
            System.out.println("[checkParameterIfImportPackage] : import 패키지를 찾지 못했습니다.");
            return false;
        }
    }

    private boolean checkParameterIfProjectPackage(ParameterDTO parameterDTO, Long blockId) {
        if(!parameterDTO.getTypeClassId().equals(0L)) {
            System.out.println("[checkParameterIfProjectPackage] : '" + parameterDTO.getType() + "'는 이미 클래스 id를 부여했습니다.");
            return false;
        }
        String parameterTypeName = parameterDTO.getType().replace(">", "").split("<")[0];
        System.out.println("[checkParameterIfProjectPackage] : Find Class about '" + parameterDTO.getType() + "' parameter");

        Long rootBlockId = findRootBlockIdByCurrentBlockId(blockId);

        PackageDTO packageDTO = packageService.getPackageDTOList().stream()
                .filter(pkg -> pkg.getBlockId().equals(rootBlockId))
                .findFirst()
                .orElseGet(PackageDTO::new);

        if(packageDTO.getPackageId() == null) {
            System.out.println("[checkParameterIfProjectPackage] : ERROR - 패키지의 id는 NULL 될 수 없습니다.");
            return false;
        }
        // 현재는 같은 패키지 이름을 가졌더라도 소스코드 별로 패키지가 생셩되기 때문에 list 로 담아뒀다.
        List<PackageDTO> packaged = new ArrayList<>();
        for(PackageDTO find : packageService.getPackageDTOList()) {
            if(packageDTO.getName().equals(find.getName())) {
                packaged.add(find);
            }
        }

        // 해당 패키지에서 선언한 클래스를 찾는다
        for(PackageDTO pkg : packaged) {
            for(ClassDTO cls : classService.getClassDTOList()) {
                // 클래스가 속한 패키지 중에서 그 클래스 이름이 선언한 변수와 같을 때, 클래스 id 부여
                if(pkg.getPackageId().equals(cls.getPackageId()) && cls.getName().equals(parameterTypeName)) {
                    System.out.println("[checkParameterIfProjectPackage] : '"+ pkg.getName() + "." + cls.getName() +"'에 대한 클래스를 발견하여 '"+ parameterDTO.getType() +"'의 class id '" + cls.getClassId() +"'를 부여합니다");
                    parameterDTO.setTypeClassId(cls.getClassId());
                    return true;
                }
            }
        }
        System.out.println("[checkParameterIfProjectPackage] : 프로젝트 내에서 패키지를 찾지 못했습니다.");
        return false;
    }

    private boolean checkReturnIfImportPackage(ReturnMapperDTO returnMapperDTO, Long blockId) {
        System.out.println("[checkReturnIfImportPackage] : Find Class about '" + returnMapperDTO.getType() + "' return mapper");
        Long rootBlockId = findRootBlockIdByCurrentBlockId(blockId);

        String returnTypeName = returnMapperDTO.getType().replace(">", "").split("<")[0];

        List<ImportDTO> imported = findImportsByRootBlockIdAndTypeName(rootBlockId, returnTypeName);

        if(imported.size() == 1) {
            System.out.println("[checkReturnIfImportPackage] : import 에서 '" + imported.get(0).getName() + "'를 발견했습니다.");
            Map<String, String> importMapper = parseImportClassNameOrImportPackageName(imported.get(0));
            if(importMapper.isEmpty()){
                return false;
            }

            returnMapperDTO.setTypeClassId(findAndBuildClassInImportPackage(importMapper.get("packageName"), importMapper.get("className"), returnMapperDTO.getType()));
            return true;
        }
        else if(imported.size() > 1){
            System.out.println("[checkReturnIfImportPackage] : 예상되는 import 패키지가 너무 많습니다.");
            for(ImportDTO importDTO : imported) {
                System.out.print("[checkReturnIfImportPackage] : [Expected]: "+ importDTO);
            }
            return false;
        }
        else {
            System.out.println("[checkReturnIfImportPackage] : import 패키지를 찾지 못했습니다.");
            return false;
        }
    }

    private boolean checkReturnIfProjectPackage(ReturnMapperDTO returnMapperDTO, Long blockId) {
        if(!returnMapperDTO.getTypeClassId().equals(0L)) {
            System.out.println("[checkReturnIfProjectPackage] : '" + returnMapperDTO.getType() + "'는 이미 클래스 id를 부여했습니다.");
            return false;
        }
        String returnTypeName = returnMapperDTO.getType().replace(">", "").split("<")[0];
        System.out.println("[checkReturnIfProjectPackage] : Find Class about '" + returnMapperDTO.getType() + "' return mapper");

        Long rootBlockId = findRootBlockIdByCurrentBlockId(blockId);

        PackageDTO packageDTO = packageService.getPackageDTOList().stream()
                .filter(pkg -> pkg.getBlockId().equals(rootBlockId))
                .findFirst()
                .orElseGet(PackageDTO::new);

        if(packageDTO.getPackageId() == null) {
            System.out.println("[checkReturnIfProjectPackage] : ERROR - 패키지의 id는 NULL 될 수 없습니다.");
            return false;
        }
        // 현재는 같은 패키지 이름을 가졌더라도 소스코드 별로 패키지가 생셩되기 때문에 list 로 담아뒀다.
        List<PackageDTO> packaged = new ArrayList<>();
        for(PackageDTO find : packageService.getPackageDTOList()) {
            if(packageDTO.getName().equals(find.getName())) {
                packaged.add(find);
            }
        }

        // 해당 패키지에서 선언한 클래스를 찾는다
        for(PackageDTO pkg : packaged) {
            for(ClassDTO cls : classService.getClassDTOList()) {
                // 클래스가 속한 패키지 중에서 그 클래스 이름이 선언한 변수와 같을 때, 클래스 id 부여
                if(pkg.getPackageId().equals(cls.getPackageId()) && cls.getName().equals(returnTypeName)) {
                    System.out.println("[checkReturnIfProjectPackage] : '"+ pkg.getName() + "." + cls.getName() +"'에 대한 클래스를 발견하여 '"+ returnMapperDTO.getType() +"'의 class id '" + cls.getClassId() +"'를 부여합니다");
                    returnMapperDTO.setTypeClassId(cls.getClassId());
                    return true;
                }
            }
        }
        System.out.println("[checkReturnIfProjectPackage] : 프로젝트 내에서 패키지를 찾지 못했습니다.");
        return false;
    }

    private boolean checkVariableIfImportPackage(VariableDeclarationDTO variableDeclarationDTO) {

        if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
            System.out.println("[checkVariableIfImportPackage] : '" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
            return false;
        }
        String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
        System.out.println("[checkVariableIfImportPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

        Long rootBlockId = findRootBlockIdByCurrentBlockId(variableDeclarationDTO.getBlockId());

        // TODO : import 패키지 중 *로 선언하는 것이 있으면 해당 라이브러리 내 모든 패키지->소스코드들을 다 까봐야 한다.
        List<ImportDTO> imported = findImportsByRootBlockIdAndTypeName(rootBlockId, variableTypeName);

        if(imported.size() == 1) {
            System.out.println("[checkVariableIfImportPackage] : import 에서 '" + imported.get(0).getName() + "'를 발견했습니다.");
            variableDeclarationDTO.setImportId(imported.get(0).getImportId());

            Map<String, String> importMapper = parseImportClassNameOrImportPackageName(imported.get(0));
            if(importMapper.isEmpty()){
                return false;
            }

            variableDeclarationDTO.setTypeClassId(findAndBuildClassInImportPackage(importMapper.get("packageName"), importMapper.get("className"), variableDeclarationDTO.getName()));
            return true;

        }
        else if(imported.size() > 1){
            System.out.println("[checkVariableIfImportPackage] : 예상되는 import 패키지가 너무 많습니다.");
            for(ImportDTO importDTO : imported) {
                System.out.print("[checkVariableIfImportPackage] : Expected - "+ importDTO);
            }
            return false;
        }
        else {
            System.out.println("[checkVariableIfImportPackage] : import 패키지를 찾지 못했습니다.");
            return false;
        }
    }

    private boolean checkVariableIfProjectPackage(VariableDeclarationDTO variableDeclarationDTO) {
        if(!variableDeclarationDTO.getTypeClassId().equals(0L)) {
            System.out.println("[checkVariableIfProjectPackage] : '" + variableDeclarationDTO.getVariableType().asString() + "'는 이미 클래스 id를 부여했습니다.");
            return false;
        }
        // 선언한 변수가 속한 패키지를 찾는다
        if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
            System.out.println("[checkVariableIfProjectPackage] : '" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
            return false;
        }
        String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
        System.out.println("[checkVariableIfProjectPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

        Long rootBlockId = findRootBlockIdByCurrentBlockId(variableDeclarationDTO.getBlockId());

        PackageDTO packageDTO = packageService.getPackageDTOList().stream()
                .filter(pkg -> pkg.getBlockId().equals(rootBlockId))
                .findFirst()
                .orElseGet(PackageDTO::new);

        if(packageDTO.getPackageId() == null) {
            System.out.println("[checkVariableIfProjectPackage] : ERROR - 패키지의 id는 NULL 될 수 없습니다.");
            return false;
        }
        // 현재는 같은 패키지 이름을 가졌더라도 소스코드 별로 패키지가 생셩되기 때문에 list 로 담아뒀다.
        List<PackageDTO> packaged = new ArrayList<>();
        for(PackageDTO find : packageService.getPackageDTOList()) {
            if(packageDTO.getName().equals(find.getName())) {
                packaged.add(find);
            }
        }

        // 해당 패키지에서 선언한 클래스를 찾는다
        for(PackageDTO pkg : packaged) {
            for(ClassDTO cls : classService.getClassDTOList()) {
                // 클래스가 속한 패키지 중에서 그 클래스 이름이 선언한 변수와 같을 때, 클래스 id 부여
                if(pkg.getPackageId().equals(cls.getPackageId()) && cls.getName().equals(variableTypeName)) {
                    System.out.println("[checkVariableIfProjectPackage] : '"+ pkg.getName() + "." + cls.getName() +"'에 대한 클래스를 발견하여 '"+ variableDeclarationDTO.getName() +"'의 class id '" + cls.getClassId() +"'를 부여합니다");
                    variableDeclarationDTO.setTypeClassId(cls.getClassId());
                    return true;
                }
            }
        }
        System.out.println("[checkVariableIfProjectPackage] : 프로젝트 내에서 패키지를 찾지 못했습니다.");
        return false;
    }

    // findRootBlockIdByCurrentBlockId: 소스코드의 최상단 블락의 위치를 반환 (변수가 선언된 블락 id를 가지고, 해당 블락의 최상단 블락을 찾아 리턴)
    private Long findRootBlockIdByCurrentBlockId(Long currentBlockId) {
        Long findBlockId = currentBlockId;
        while(true) {
            Long finalFindBlockId = findBlockId;
            BlockDTO blockDTO = blockService.getBlockDTOList().stream()
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

    // findImportsByRootBlockIdAndTypeName: 해당 소스파일에서 임포트로 선언한 패키지+클래스를 비교하여 같은 타입(혹은 클래스) 명을 가진 임포트 리스트를 반환
    private List<ImportDTO> findImportsByRootBlockIdAndTypeName(Long rootBlockId, String typeName) {
        List<ImportDTO> imports = new ArrayList<>();

        // import 패키지의 클래스라면 import Id를 추가
        for(ImportDTO importDTO : importService.getImportDTOList()) {
            String[] importPkg = importDTO.getName().split("\\.");
            if(importDTO.getBlockId().equals(rootBlockId) && importPkg[importPkg.length-1].equals(typeName)){
                imports.add(importDTO);
            }
        }

        return imports;
    }

    // parseImportClassNameOrImportPackageName: import 에서 패키지와 클래스 이름을 분리하여 map<string, string> 타입으로 반환
    private Map<String, String> parseImportClassNameOrImportPackageName(ImportDTO importDTO) {
        Map<String, String> importMapper = new HashMap<>();

        String[] importPkg = importDTO.getName().split("\\.");
        StringBuilder importPkgPath = new StringBuilder();
        for(int i = 0; i < importPkg.length-1; i++) {
            importPkgPath.append(importPkg[i]);
            if(i != importPkg.length-2) {
                importPkgPath.append(".");
            }
        }
        importMapper.put("className", importPkg[importPkg.length-1]);
        importMapper.put("packageName", importPkgPath.toString());

        return importMapper;
    }

    // findClassInImportPackage: 프로젝트에 존재하는 다른 패키지를 import 해서 사용중일 때, 패키지에서 선언한 클래스가 import package.class;와 동일하면 class id 반환
    private Long findAndBuildClassInImportPackage(String importPackageName, String importClassName, String targetName) {
        String importName = importPackageName + "." + importClassName;
        for(PackageDTO packageDTO : packageService.getPackageDTOList()) {
            if(packageDTO.getName().equals(importPackageName)) {
                for(ClassDTO classDTO : classService.getClassDTOList()) {
                    if(classDTO.getPackageId().equals(packageDTO.getPackageId()) && classDTO.getName().equals(importClassName)) {
                        System.out.println("[findAndBuildClassInImportPackage] : '"+ importName +"'에 대한 클래스를 발견하여 '"
                                + targetName +"'의 class id '" + classDTO.getClassId() +"'를 부여합니다");
                        return classDTO.getClassId();
                    }
                }
            }
        }
        System.out.println("[findAndBuildClassInImportPackage] : '"+ importName + "'에 대한 클래스를 발견하지 못해 class id 부여에 실패했습니다.");
        return 0L;
    }
}
