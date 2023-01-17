package com.tmax.ast.dto;

public class MemberVariableDeclarationDTO extends VariableDeclarationDTO {

    @Override
    public String toString() {
        return "MemberVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", classId : " + getClassId() +
                ", importId : " + getImportId() +
                ", name : '" + getName() + '\'' +
                "', nodeType: '" + getNode().getMetaModel().getTypeName() +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", type : '" + getType() + '\'' +
//                ", variableType : " + variableType.getClass().getSimpleName() +
                ", initializer : " + getInitializer() +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
