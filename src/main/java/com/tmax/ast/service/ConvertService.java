package com.tmax.ast.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
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

    public List<VariableDeclarationDTO> getVariableDeclarationDTOList() {
        return variableService.getVariableDeclarationDTOList();
    }

    public List<MethodDeclarationDTO> getMethodDeclarationDTOList() {
        return methodService.getMethodDeclarationDTOList();
    }

    public void clear() {
        blockService.blockListClear();
        packageService.packageListClear();
        importService.importListClear();
        classService.classListClear();
        variableService.variableDeclarationListClear();
        methodService.methodDeclarationListClear();
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
                    classService.buildClass(classId++, rootBlockDTO.getBlockId(), packageDTO.getPackageId(), node);
                    break;
                case "EnumDeclaration":
                    classService.buildEnum(classId++, rootBlockDTO.getBlockId(), packageDTO.getPackageId(), node);
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
            variableService.buildVariableDeclInMemberField(variableDeclarationId++, blockDTO.getBlockId(), node);
        }
        // 함수 내에서 선언하는 변수
        else if(nodeType.equals("VariableDeclarationExpr")) {
            blockDTO = parentBlockDTO;
            variableService.buildVariableDeclInMethod(variableDeclarationId++, blockDTO.getBlockId(), node);
        }
        // || nodeType.equals("ConstructorDeclaration")
        else if(nodeType.equals("MethodDeclaration")) {
            // 내부에 BlockStmt 가 존재하여 별도의 Block 을 생성하지는 않음
            // 현재 상위 block 에서 선언되는 것들.
            blockDTO = parentBlockDTO;
            // 함수 및 생성자 선언 시 build
            methodService.buildMethodDeclaration(methodDeclarationId++, blockDTO.getBlockId(), node, nodeType);
        }
        else if(nodeType.equals("BlockStmt")) {
            blockDTO = blockService.buildBlock(blockId++, parentBlockDTO.getDepth() + 1, parentBlockDTO.getBlockId(), nodeType, node);
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

    public void visitVariablesAndBuildClassId() {
        for(VariableDeclarationDTO variableDeclarationDTO : getVariableDeclarationDTOList()) {
            // 선언한 변수가 primitive(원시) 타입이면 class id 는 0으로
            if(variableDeclarationDTO.getVariableType().isPrimitiveType()) {
                continue;
            }
            // 1. 패키지를 import 해서 사용하는 클래스 변수인지 체크
            boolean isImport = checkVariableIfImportPackage(variableDeclarationDTO);

            // 2. 그게 아니라면, 같은 프로젝트 내에 존재하는 클래스 변수
            if(!isImport) {
                boolean isProject = checkVariableIfProjectPackage(variableDeclarationDTO);

                if(!isProject) {
                    System.out.println("'" + variableDeclarationDTO.getName() + "' 변수에 대한 클래스를 발견하지 못했습니다.");
                }
            }
        }
    }

    private boolean checkVariableIfImportPackage(VariableDeclarationDTO variableDeclarationDTO) {
        List<ImportDTO> imported = new ArrayList<>();
        if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
            System.out.println("'" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
            return false;
        }
        String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
        System.out.println("[isImportPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

        Long findBlockId = getBlockIdByVariable(variableDeclarationDTO);

        // TODO : import 패키지 중 *로 선언하는 것이 있으면 해당 라이브러리 내 모든 패키지->소스코드들을 다 까봐야 한다.

        // import 패키지의 클래스라면 import Id를 추가
        for(ImportDTO importDTO : importService.getImportDTOList()) {
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

            for(PackageDTO packageDTO : packageService.getPackageDTOList()) {
                if(packageDTO.getName().equals(importPkgPath.toString())) {
                    for(ClassDTO classDTO : classService.getClassDTOList()) {
                        if(classDTO.getName().equals(importPkgClass)) {
                            System.out.println("[isImportPackage] : '"+ imported.get(0).getName() +"'에 대한 클래스를 발견하여 '"+ variableDeclarationDTO.getName() +"'의 class id '" + classDTO.getClassId() +"'를 부여합니다");
                            variableDeclarationDTO.setClassId(classDTO.getClassId());
                            return true;
                        }
                    }
                }
            }

        } else if(imported.size() > 1){
            System.out.println("[isImportPackage] : 예상되는 import 패키지가 너무 많습니다.");
            for(ImportDTO importDTO : imported) {
                System.out.print("[isImportPackage] : [Expected]: "+importDTO);
            }
            return false;
        } else {
            System.out.println("[isImportPackage] : import 패키지를 찾지 못했습니다.");
            return false;
        }
        return false;
    }

    private boolean checkVariableIfProjectPackage(VariableDeclarationDTO variableDeclarationDTO) {
        if(variableDeclarationDTO.getClassId().equals(0L)) {
            // 선언한 변수가 속한 패키지를 찾는다
            if(variableDeclarationDTO.getVariableType().getChildNodes().size() == 0) {
                System.out.println("'" + variableDeclarationDTO.getVariableType().asString() + "'는 하위 노드가 존재하지 않습니다.");
                return false;
            }
            String variableTypeName = variableDeclarationDTO.getVariableType().getChildNodes().get(0).toString();
            System.out.println("[isProjectPackage] : Find Class about '" + variableDeclarationDTO.getType() + " " + variableDeclarationDTO.getName() + "' variable");

            Long findBlockId = getBlockIdByVariable(variableDeclarationDTO);

            PackageDTO packageDTO = packageService.getPackageDTOList().stream()
                    .filter(pkg -> pkg.getBlockId().equals(findBlockId))
                    .findFirst()
                    .orElseGet(PackageDTO::new);

            if(packageDTO.getPackageId() == null) {
                System.out.println("[오류] : 패키지의 id는 null이 될 수 없습니다.");
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
                        System.out.println("[isProjectPackage] : '"+ pkg.getName() + "." + cls.getName() +"'에 대한 클래스를 발견하여 '"+ variableDeclarationDTO.getName() +"'의 class id '" + cls.getClassId() +"'를 부여합니다");
                        variableDeclarationDTO.setClassId(cls.getClassId());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // getBlockIdByVariable: 변수가 선언된 소스코드의 최상단 블락의 위치를 반환 (변수가 선언된 블락 id를 가지고, 해당 블락의 최상단 블락을 찾아 리턴)
    private Long getBlockIdByVariable(VariableDeclarationDTO variableDeclarationDTO) {
        Long findBlockId = variableDeclarationDTO.getBlockId();
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

}
