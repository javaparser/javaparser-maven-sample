package com.tmax.ast.service.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.tmax.ast.dto.*;

import java.util.ArrayList;
import java.util.List;

import static com.tmax.ast.config.GeneratorIdentifier.*;

public class MethodService {

    private final List<MethodDeclarationDTO> methodDeclarationDTOList;
    private final List<MethodCallExprDTO> methodCallExprDTOList;

    public MethodService() {
        this.methodDeclarationDTOList = new ArrayList<>();
        this.methodCallExprDTOList = new ArrayList<>();
    }

    public List<MethodDeclarationDTO> getMethodDeclarationDTOList() {
        return this.methodDeclarationDTOList;
    }
    public List<MethodCallExprDTO> getMethodCallExprDTOList() {
        return this.methodCallExprDTOList;
    }

    public void methodDeclarationListClear() {
        this.methodDeclarationDTOList.clear();
    }
    public void methodCallExprListClear() {
        this.methodCallExprDTOList.clear();
    }

    public void buildMethodDeclaration(Long methodDeclarationId, Long blockId, Node node, String nodeType) {
        MethodDeclarationDTO methodDeclarationDTO = new MethodDeclarationDTO();
        ReturnMapperDTO returnMapperDTO = new ReturnMapperDTO();
        List<ParameterDTO> parameters = new ArrayList<>();
        List<Node> childNodes = node.getChildNodes();

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String methodName = "";

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
                methodName = simpleName.asString();
            } else if(childNodeTypeName.equals("Parameter")) {
                ParameterDTO parameterDTO = new ParameterDTO();

                Parameter parameterNode = (Parameter) childNode;

                parameterDTO.setParameterId(parameterId++);
                parameterDTO.setMethodDeclId(methodDeclarationId);
                parameterDTO.setIndex(parameterIndex++);
                parameterDTO.setName(parameterNode.getName().asString());
                parameterDTO.setType(parameterNode.getType().asString());
                parameterDTO.setNode(parameterNode);
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
                String returnValueTypeName = childNode.toString();

                returnMapperDTO.setReturnMapperId(returnMapperId++);
                returnMapperDTO.setMethodDeclId(methodDeclarationId);
                returnMapperDTO.setClassId(0L);
                returnMapperDTO.setType(returnValueTypeName);
                returnMapperDTO.setNode(childNode);
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

        methodDeclarationDTO.setMethodDeclId(methodDeclarationId);
        methodDeclarationDTO.setBlockId(blockId);
        methodDeclarationDTO.setName(methodName);
        methodDeclarationDTO.setModifier(modifierKeyword);
        methodDeclarationDTO.setAccessModifier(accessModifierKeyword);
        // add to methodDeclarationDTO
        methodDeclarationDTO.setReturnMapper(returnMapperDTO);
        methodDeclarationDTO.setParameters(parameters);

        methodDeclarationDTO.setNode(node);
        methodDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        methodDeclarationDTOList.add(methodDeclarationDTO);
    }

    public void buildMethodCallExpr(Long methodCallExprId, Long blockId, Node node, String nodeType) {
        MethodCallExprDTO methodCallExprDTO = new MethodCallExprDTO();
        MethodCallExpr methodCallExpr = (MethodCallExpr) node;
        List<Node> childNodes = node.getChildNodes();

        String methodName = "";
        int argumentIndex = 1;

        for(Node childNode : childNodes) {
            String childNodeTypeName = childNode.getMetaModel().getTypeName();
            // scope node에 대한 처리도 해줘야함
            if(childNodeTypeName.equals("SimpleName")) {
                SimpleName simpleName = (SimpleName) childNode;
                methodName = simpleName.asString();
            }
        }

        List<ArgumentDTO> argumentDTOList = new ArrayList<>();
        NodeList<Expression> arguments = methodCallExpr.getArguments();
        for (Expression arg : arguments) {
            ArgumentDTO argumentDTO = new ArgumentDTO();
            argumentDTO.setIndex(argumentIndex++);
            argumentDTO.setName(arg.toString());
            argumentDTO.setArgumentId(argumentId++);
            argumentDTO.setMethodCallExprId(methodCallExprId);
            // 임시로 NodeType으로 저장
            argumentDTO.setType(nodeType);
            argumentDTO.setPosition(
                    new Position(
                            arg.getRange().get().begin.line,
                            arg.getRange().get().begin.column,
                            arg.getRange().get().end.line,
                            arg.getRange().get().end.column
                    )
            );
            argumentDTOList.add(argumentDTO);
        }
        methodCallExprDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column
                )
        );

        methodCallExprDTO.setMethodCallExprId(methodCallExprId);
        methodCallExprDTO.setBlockId(blockId);
        methodCallExprDTO.setName(methodCallExpr.getNameAsString());
        methodCallExprDTO.setArguments(argumentDTOList);


        methodCallExprDTOList.add(methodCallExprDTO);

    }
}
