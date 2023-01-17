package com.tmax.ast.dto;

public class MemberVariableDeclarationDTO extends VariableDeclarationDTO {

    private Long belongedClassId; // 변수가 포함되어 있는 클래스

    public Long getBelongedClassId() {
        return belongedClassId;
    }

    public void setBelongedClassId(Long belongedClassId) {
        this.belongedClassId = belongedClassId;
    }
    @Override
    public String toString() {
        return "MemberVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", typeClassId : " + getTypeClassId() +
                ", belongedClassId : " + getBelongedClassId() +
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
