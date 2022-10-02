package com.github.hebelala.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author hebelala
 */
public class AtomicUpdaterTest {

    @Benchmark
    public void testGetByDirect(MyFields myFields) {
        myFields.getByDirect();
    }

    @Benchmark
    public void testGetByUpdater(MyFields myFields) {
        myFields.getByUpdater();
    }

    @Benchmark
    public void testSetByDirect(MyFields myFields) {
        myFields.setByDirect(1);
    }

    @Benchmark
    public void testSetByUpdater(MyFields myFields) {
        myFields.setByUpdater(1);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AtomicUpdaterTest.class.getSimpleName())
                .threads(4)
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Benchmark)
    public static class MyFields {

        volatile int field = 0;
        volatile int directField = 0;
        AtomicIntegerFieldUpdater<MyFields> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(MyFields.class, "field");

        public int getByDirect() {
            return directField;
        }

        public int getByUpdater() {
            return fieldUpdater.get(this);
        }

        public void setByDirect(int value) {
            directField = value;
        }

        public void setByUpdater(int value) {
            fieldUpdater.set(this, value);
        }

    }

}
