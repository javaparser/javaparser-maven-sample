package com.tmax.ast;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.*;
import com.tmax.ast.service.ConvertService;
import com.tmax.ast.service.OutputService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

/**
 * Some code that uses JavaParser.
 */
public class ProjectParser {
    private static final OutputService outputService = new OutputService();
    public static void main(String[] args) throws IOException {
        ConvertService convertService = new ConvertService();
        // 파싱할 프로젝트 루트 경로
        Path root = Paths.get(("/Users/namgonkim/workspace/java-baseball"));

        // 파싱 전략 설정
        // 여기서 어케하면 jar에 있는 심볼이나 디렉토리 내에 있는 심볼을 매핑시킬 수 있다는데 좀더 조사가 필요함.
        // https://github.com/javaparser/javasymbolsolver
        SymbolSolverCollectionStrategy symbolSolverCollectionStrategy = new SymbolSolverCollectionStrategy();
        ProjectRoot projectRoot = symbolSolverCollectionStrategy.collect(root);
        // 전체 프로젝트 ast로 조회
        List<SourceRoot> sourceRootList = projectRoot.getSourceRoots();

        for(SourceRoot sourceRoot : sourceRootList) {
            List<ParseResult<CompilationUnit>> parseResults = sourceRoot.tryToParse();
            for(ParseResult<CompilationUnit> parseResult : parseResults) {
                Optional<CompilationUnit> optionalCompilationUnit = parseResult.getResult();
                if(optionalCompilationUnit.isPresent()) {
                    CompilationUnit cu = optionalCompilationUnit.get();

                    // cu를 활용
                    String fileName = cu.getStorage().get().getFileName();

//                    // 전체 다해보기
//                    System.out.println("File: [" + cu.getStorage().get().getPath() + "]");
//                    convertService.visit(cu);

                    // 부분만 해보기
                    if(fileName.equals("Game.java") || fileName.equals("Judgement.java") || fileName.equals("Computer.java")) {
//                    if( fileName.equals("RandomNumberGenerator.java") ||
//                            fileName.equals("NumberGenerator.java") || fileName.equals("FixedNumberGenerator.java")) {
                        System.out.println("File: [" + cu.getStorage().get().getPath() + "]");
                        convertService.visit(cu);
                    }

                    //outputService.dotPrinter(fileName, cu);
                }
            }
            //saveSourceCodesInOutputDir(sourceRoot);

        }
        convertService.visitVariablesAndBuildClassId();
        convertService.visitMethodsAndBuildClassId();

        // 출력
        System.out.println(convertService.getBlockDTOList());
        System.out.println(convertService.getPackageDTOList());
        System.out.println(convertService.getImportDTOList());
        System.out.println(convertService.getClassDTOList());

        System.out.println(convertService.getMemberVariableDeclarationDTOList());
        System.out.println(convertService.getStmtVariableDeclarationDTOList());

        System.out.println(convertService.getMethodDeclarationDTOList());
        System.out.println(convertService.getMethodCallExprDTOList());

        convertService.clear();
    }

    private static void saveSourceCodesInOutputDir(SourceRoot sourceRoot) {
        // 소스코드 형태로 cu를 재조립해서 output 디렉토리에 저장
        // This saves all the files we just read to an output directory.
        sourceRoot.saveAll(
                // The path of the Maven module/project which contains the LogicPositivizer class.
                CodeGenerationUtils.mavenModuleRoot(ProjectParser.class)
                        // appended with a path to "output"
                        .resolve(Paths.get("output")));
    }
}
