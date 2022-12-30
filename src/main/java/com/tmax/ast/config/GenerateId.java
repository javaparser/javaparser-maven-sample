package com.tmax.ast.config;

public class GenerateId {
    private static Long autoPackageId = 1L;
    private static Long autoBlockId = 1L;
    private static Long parentBlockId = null;

    public static Long getPackageId() {
        return autoPackageId++;
    }
    public static Long getBlockId() {
        Long temp;
        temp = autoBlockId;
        parentBlockId = temp;
        return autoBlockId++;
    }
    public static Long getParentBlockId() {
        return parentBlockId;
    }

    public static void incrementPackageId() {
        autoPackageId++;
    }
    public static void incrementBlockId() {
        autoBlockId++;
    }
}
