package com.yourorganization.maven_sample;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
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
public class LogicPositivizer {
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

                }
            }

            // 소스코드를 저장
            // This saves all the files we just read to an output directory.
            sourceRoot.saveAll(
                    // The path of the Maven module/project which contains the LogicPositivizer class.
                    CodeGenerationUtils.mavenModuleRoot(LogicPositivizer.class)
                            // appended with a path to "output"
                            .resolve(Paths.get("output")));
        }

    }
}
