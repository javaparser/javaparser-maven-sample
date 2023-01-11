package com.tmax.ast.service.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.tmax.ast.dto.ClassDTO;
import com.tmax.ast.dto.Position;

import java.util.ArrayList;
import java.util.List;

public class ClassService {

    private final List<ClassDTO> classDTOList;

    public ClassService() {
        this.classDTOList = new ArrayList<>();
    }

    public List<ClassDTO> getClassDTOList() {
        return this.classDTOList;
    }

    public void classListClear() {
        this.classDTOList.clear();
    }

    public ClassDTO buildClass(Long classId, Long blockId, Long packageId , Node node) {
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

        classDTOList.add(classDTO);

        return classDTO;
    }

    public ClassDTO buildEnum(Long classId, Long blockId, Long packageId, Node node) {
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

        classDTOList.add(classDTO);

        return classDTO;
    }
}
