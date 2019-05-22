package io.github.vampyr09.experiment.camunda.dmn.benchmark;

import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import io.github.vampyr09.experiment.camunda.dmn.benchmark.domain.Course;
import io.github.vampyr09.experiment.camunda.dmn.benchmark.domain.CourseType;
import io.github.vampyr09.experiment.camunda.dmn.benchmark.domain.SportsType;
import io.github.vampyr09.experiment.camunda.dmn.benchmark.domain.UserGroup;

/** tests the deployment time of dmn files. */
@Warmup(iterations = 5, batchSize = 1)
@Measurement(iterations = 20, batchSize = 1)
@BenchmarkMode({ Mode.SingleShotTime })
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Threads(1)
@State(Scope.Thread)
public class DmnDeployBenchmark extends DmnAware {

    @Param({ "Decision_300", "Decision_600", "Decision_1200", "Decision_2400", "Decision_4800" })
    public String parseDecisions;

    @Setup
    @Override
    public void buildEngine() {
        super.buildEngine();
    }

    @Benchmark
    public void parseDecisions() throws FileNotFoundException, InterruptedException {
        parseDecision(getDmnEngine(), this.parseDecisions);
        //        showHeapMemoryUsage(this.parseDecisions);
    }

    public Course randomCourse() {
        Random random = new Random();

        return new Course(CourseType.Course,
                UserGroup.values()[random.nextInt(5)],
                SportsType.values()[random.nextInt(2)],
                random.nextInt(100),
                random.nextInt(300),
                random.nextInt(25));
    }

    public static void main(final String[] args) throws RunnerException {
        new Runner(new OptionsBuilder().include(DmnDeployBenchmark.class.getSimpleName()).build()).run();
    }

    /**
     * Very unsafe, hacked and crude version to show the heap memory usage for the decision table parsing by showing 
     * the heap memory at the beginning, then pushing multiple hints to the garbage collector and hope that it did run.
     * <p>
     * It can be possible that the garbage collector already run before calling this method or that it did not run
     * even after the 4 hints. But this benchmarks is running for multiple interations so the chances are good that it
     * does something useful to interpret. :)
     *
     * @param parseDecision the decision to evaluate between the heap memory usages.
     * @throws InterruptedException if interrupted
     */
    private void showHeapMemoryUsage(final String parseDecision) throws InterruptedException {
        System.out.println(
                "heap: " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024) + " MB");

        /* hint multiple times to the garbage collector and hope it runs. */
        System.gc();
        Thread.sleep(50);
        System.gc();
        Thread.sleep(50);
        System.gc();
        Thread.sleep(50);
        System.gc();
        Thread.sleep(50);

        System.out.println(
                "heap: " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024) + " MB");

        evaluateDecisionWithCourse(parseDecision, randomCourse()).getFirstResult();

        System.out.println(
                "heap: " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024) + " MB");
    }

}
