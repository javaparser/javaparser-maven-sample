package com.tmax.ast.dto;

public class Position {
    public int beginLine;
    public int beginColumn;

    public int endLine;
    public int endColumn;

    public Position(int beginLine, int beginColumn, int endLine, int endColumn) {
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    @Override
    public String toString() {
        return "{" +
                "begin [" +
                "line: " + beginLine + ", column: " + beginColumn +
                "]," +
                "end [" +
                "line: " + endLine + ", column: " + endColumn +
                "]" +
                "}";
    }
}
