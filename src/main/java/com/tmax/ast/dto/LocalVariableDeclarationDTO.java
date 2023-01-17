package com.tmax.ast.dto;

public class LocalVariableDeclarationDTO extends VariableDeclarationDTO {

    @Override
    public String toString() {
        return "LocalVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", typeClassId : " + getTypeClassId() +
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
