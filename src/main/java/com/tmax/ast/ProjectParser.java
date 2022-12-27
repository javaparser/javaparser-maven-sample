package com.tmax.ast;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Some code that uses JavaParser.
 */
public class ProjectParser {
    public static void main(String[] args) throws IOException {
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
                    System.out.println("File: [" + cu.getStorage().get().getPath() + "]");
                    System.out.println(cu);
                }
            }

            // 소스코드 형태로 cu를 재조립해서 output 디렉토리에 저장
            // This saves all the files we just read to an output directory.
            sourceRoot.saveAll(
                    // The path of the Maven module/project which contains the LogicPositivizer class.
                    CodeGenerationUtils.mavenModuleRoot(ProjectParser.class)
                            // appended with a path to "output"
                            .resolve(Paths.get("output")));
        }

    }
}
