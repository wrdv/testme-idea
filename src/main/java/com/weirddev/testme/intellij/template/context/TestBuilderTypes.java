package com.weirddev.testme.intellij.template.context;

import java.util.*;

public class TestBuilderTypes {
    private static final Map<String,String> LEGACY_JAVA_COLLECTION_TYPES = new HashMap<>();
    private static final Map<String,String> JAVA_9_COLLECTION_TYPES = new HashMap<>();

    private static final Map<String,String> LEGACY_JAVA_REPLACEMENT_TYPES = new HashMap<>();
    private static final Map<String,String> LEGACY_JAVA_REPLACEMENT_TYPES_FOR_RETURN = new HashMap<>();

    public static final Set<String> JAVA_FUTURE_TYPES = Set.of("java.util.concurrent.Future", "java.util.concurrent.CompletableFuture", "java.util.concurrent.RunnableFuture",
             "java.util.concurrent.RunnableScheduledFuture", "java.util.concurrent.ScheduledThreadPoolExecutor.ScheduledFutureTask", "java.util.concurrent.FutureTask",
            "java.util.concurrent.ExecutorCompletionService.QueueingFuture", "java.util.concurrent.ForkJoinTask.AdaptedRunnable", "java.util.concurrent.ForkJoinTask.AdaptedCallable", "java.util.concurrent.ForkJoinTask",
            "java.util.concurrent.ForkJoinTask.AdaptedRunnableAction", "java.util.concurrent.CountedCompleter", "java.util.concurrent.RecursiveTask", "java.util.concurrent.ForkJoinTask.RunnableExecuteAction",
            "java.util.concurrent.CompletableFuture.AsyncSupply", "java.util.concurrent.RecursiveAction", "java.util.concurrent.CompletableFuture.Completion", "java.util.concurrent.ScheduledFuture");

    static {
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Collection", "java.util.Arrays.<TYPES>asList(<VAL>)");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Deque", "new java.util.LinkedList<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.List", "java.util.Arrays.<TYPES>asList(<VAL>)");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Map", "new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}}");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.NavigableMap", "new java.util.TreeMap<TYPES>(new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}})");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.NavigableSet", "new java.util.TreeSet<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Queue", "new java.util.LinkedList<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.RandomAccess", "new java.util.Vector(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Set", "new java.util.HashSet<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.SortedSet", "new java.util.TreeSet<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.LinkedList", "new java.util.LinkedList<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.ArrayList", "new java.util.ArrayList<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.HashMap", "new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}}");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.TreeMap", "new java.util.TreeMap<TYPES>(new java.util.HashMap<TYPES>(){{put(<VAL>,<VAL>);}})");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Vector", "new java.util.Vector(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.HashSet", "new java.util.HashSet<TYPES>(java.util.Arrays.asList(<VAL>))");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.Stack", "new java.util.Stack<TYPES>(){{push(<VAL>);}}");
        LEGACY_JAVA_COLLECTION_TYPES.put("java.util.TreeSet", "new java.util.TreeSet<TYPES>(java.util.Arrays.asList(<VAL>))");

        LEGACY_JAVA_REPLACEMENT_TYPES.putAll(LEGACY_JAVA_COLLECTION_TYPES);
        LEGACY_JAVA_REPLACEMENT_TYPES_FOR_RETURN.putAll(LEGACY_JAVA_COLLECTION_TYPES);
        for (String javaFutureType : JAVA_FUTURE_TYPES) {
            LEGACY_JAVA_REPLACEMENT_TYPES.put(javaFutureType, "java.util.concurrent.CompletableFuture.completedFuture(<VAL>)");
            LEGACY_JAVA_REPLACEMENT_TYPES_FOR_RETURN.put(javaFutureType, "<VAL>");  //todo consider handling futures separately and avoid duplicating all replacement types
        }

    }
    public static Map<String, String> getLegacyJavaReplacementTypes(){
        return LEGACY_JAVA_REPLACEMENT_TYPES;
    }
    public static Map<String, String> getLegacyJavaReplacementTypesForReturn(){
        return LEGACY_JAVA_REPLACEMENT_TYPES_FOR_RETURN;
    }
}
