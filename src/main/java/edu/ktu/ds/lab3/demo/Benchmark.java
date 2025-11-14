package edu.ktu.ds.lab3.demo;

import edu.ktu.ds.lab3.utils.HashManager;
import edu.ktu.ds.lab3.utils.HashMap;
import edu.ktu.ds.lab3.utils.HashMapOa;
import edu.ktu.ds.lab3.utils.HashMapOa.OpenAddressingType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import edu.ktu.ds.lab3.utils.Map;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
public class Benchmark {

    public enum MapImplementation {
        SEPARATE_CHAINING,
        CUSTOM_LINEAR_OA,
        CUSTOM_QUADRATIC_OA,
        CUSTOM_DOUBLE_OA
    }

    @Param({"10000", "20000", "40000", "80000"})
    public int elementCount;

    @Param({"SEPARATE_CHAINING", "CUSTOM_LINEAR_OA", "CUSTOM_QUADRATIC_OA", "CUSTOM_DOUBLE_OA"})
    public MapImplementation mapImplementation;

    List<String> ids;

    @Setup(Level.Trial)
    public void generateIdsAndCars() {
        ids = generateIds(elementCount);
    }

    static List<String> generateIds(int count) {
        return new ArrayList<>(CarsGenerator.generateShuffleIds(count));
    }

    @org.openjdk.jmh.annotations.Benchmark
    // FIX: The return type is now the common java.util.Map interface
    public Map<String, String> putPerformance() {
        // FIX: The map variable uses the common java.util.Map interface
        Map<String, String> map;

        final int INITIAL_CAPACITY = (int) (elementCount / 0.75f) + 1;
        final float LOAD_FACTOR = 0.75f;

        switch (mapImplementation) {
            case SEPARATE_CHAINING:
                map = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR, HashManager.HashType.DIVISION);
                break;

            case CUSTOM_LINEAR_OA:
                // Custom map creation
                map = new HashMapOa<String, String>(
                        INITIAL_CAPACITY,
                        LOAD_FACTOR,
                        HashManager.HashType.DIVISION,
                        OpenAddressingType.LINEAR
                );
                break;

            case CUSTOM_QUADRATIC_OA:
                map = new HashMapOa<String, String>(
                        INITIAL_CAPACITY,
                        LOAD_FACTOR,
                        HashManager.HashType.DIVISION,
                        OpenAddressingType.QUADRATIC
                );
                break;

            case CUSTOM_DOUBLE_OA:
                map = new HashMapOa<String, String>(
                        INITIAL_CAPACITY,
                        LOAD_FACTOR,
                        HashManager.HashType.DIVISION,
                        OpenAddressingType.DOUBLE_HASHING
                );
                break;

            default:
                throw new IllegalStateException("Unknown Map Implementation: " + mapImplementation);
        }

        putMappings(ids, map);
        return map;
    }

    /**
     * Helper method to insert all mappings into the given map.
     * FIX: Accepts the standard java.util.Map interface, which both standard
     * and (presumably) custom maps implement or are cast to internally.
     */
    public static void putMappings(List<String> ids, Map<String, String> map) {
        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), ids.get(i));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Benchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}