package com.linkedin.learning.samples.util;


import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
public class ScramblerBenchMarker {
    @Param("MyNameIsWhat")
    String sourceText;

    List<String> listOfStrings;

    @Setup(Level.Invocation)
    public void initListOfStrings(){
        listOfStrings = Arrays.asList("MyNameIsWhat","MyNameIsWho","MyNameIs","MeShady","Tango","Alpha","Yankee","Oscar");
    }

    final static int maxAnagrams = 25;

    public static String scrambleWithRandom(String valueToScramble) {
        char[] options = valueToScramble.toCharArray();
        int[] positions = new int[options.length];
        Random randomizer = new Random();
        StringBuilder sb = new StringBuilder();
        int next = 0;
        for (int i = 0; i < options.length; i++) {
            do {
                next = randomizer.nextInt(valueToScramble.length());
                if (positions[next] == 0) {
                    sb.append(options[next]);
                    positions[next]++;
                    break;
                }
            }while(positions[next] > 0);
        }
        return sb.toString();
    }

    public static String scrambleWithThreadLocalRandom(String valueToScramble) {
        char[] options = valueToScramble.toCharArray();
        int[] positions = new int[options.length];
        Random randomizer = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        int next = 0;
        for (int i = 0; i < options.length; i++) {
            do {
                next = randomizer.nextInt(valueToScramble.length());
                if (positions[next] == 0) {
                    sb.append(options[next]);
                    positions[next]++;
                    break;
                }
            }while(positions[next] > 0);
        }
        return sb.toString();
    }





    public static String scrambleRandom(String stringToScramble){
        return scrambleWithRandom(stringToScramble);
    }


    public static synchronized String scrambleRandomWithSynchronizer(String stringToScramble){
        return scrambleWithRandom(stringToScramble);
    }


    public static String scrambleThreadLocalRandom(String stringToScramble){
        return scrambleWithThreadLocalRandom(stringToScramble);
    }


    public static synchronized String scrambleThreadLocalRandomWithSynchronizer(String stringToScramble){
        return scrambleWithThreadLocalRandom(stringToScramble);
    }



    public static List<String> serialScrambleWithRandom(List<String> stringsToScramble){
        return stringsToScramble.stream()
                .map(ScramblerBenchMarker::scrambleWithRandom)
                .collect(Collectors.toList());
    }


    public static List<String> serialScrambleWithThreadLocalRandom(List<String> stringsToScramble){
        return stringsToScramble.stream()
                .map(ScramblerBenchMarker::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    public static List<String> parallelScrambleWithThreadLocalRandom(List<String> stringsToScramble){
        return stringsToScramble.parallelStream()
                .map(ScramblerBenchMarker::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput,Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(2)
    @Threads(5)
    @Warmup(iterations = 3,time = 3, timeUnit = TimeUnit.SECONDS)
    public static List<String> parallelScrambleWithRandom(ScramblerBenchMarker stringsToScramble){
        return stringsToScramble.listOfStrings.parallelStream()
                .map(ScramblerBenchMarker::scrambleWithRandom)
                .collect(Collectors.toList());
    }

    public static void main(String[] params) throws IOException {
        Main.main(params);
    }

}
