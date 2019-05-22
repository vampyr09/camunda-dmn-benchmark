# Camunda DMN benchmark

This project is running performance benchmarks for the [Camunda BPM](https://docs.camunda.org/manual/7.10/) `DMN Engine`.

It is running the `org.camunda.bpm.dmn:camunda-engine-dmn` in version `7.10.0`. 

## Intention

The intention of this project is to highlight that the `DmnEngine` takes a very long time to parse big decision tables when running with Java 8.0_212 compared to Java 11.0.3.
It is used to ask the [Camunda BPM forum]() for the possible reasons to explain this performance differences.

## Running the benchmark

You can build the `benchmark-deploy.jar` with 
```
mvn clean install
```

and run the benchmark afterwards with

```
java -jar target\benchmark-deploy.jar
```

## Benchmark data

There are multiple `.dmn` files located in the classpath (`src/main/resources`). They are named by the amount of rules they contain from 300 to 6000. So the file `Decision_900.dmn` contains 900 rules in one decision table. The decision table key needs to be equal to its file name (without the .dmn postfix).

The benchmark is configured to parse 300, 600, 1200, 2400 and 4800 rules and prints the time it need to parse the decision table. It runs with 5 warmup cycles and 20 iterations, each one parsing the file 1 time.

## Result

### With Java 11.0.3

| Benchmark      | Rules |          ms/op | iterations |
|----------------|-------|---------------:|-----------:|
| parseDecisions | 300   | 66,4 +/- 19,3  | 20         |
| parseDecisions | 600   | 88,7 +/- 13,9  | 20         |
| parseDecisions | 1200  | 154,0 +/- 17,7 | 20         |
| parseDecisions | 2400  | 375,0 +/- 46,9 | 20         |
| parseDecisions | 4800  | 757,3 +/- 85,4 | 20         |

The 4800 rule decision table took **800ms** to be parsed with Java 11.

### With Java 8.0_212 

| Benchmark      | Rules |          ms/op    | iterations  |
|----------------|-------|------------------:|------------:|
| parseDecisions | 300   | 139,1 +/- 8,8     | 20          |
| parseDecisions | 600   | 448,1 +/- 15,9    | 20          |
| parseDecisions | 1200  | 1824,7 +/- 281,5  | 20          |
| parseDecisions | 2400  | 11629,6 +/- 2801  | 20          |
| parseDecisions | 4800  | 68730 +/- ....... | 5 canceled |

The 4800 rule decision table took **68s** to be parsed with Java 8.

## Not-Causes

The Garbage Collector is not the reason. With the ParallelGC and Java 11 it is still fast: <https://docs.gigaspaces.com/xap/14.0/rn/java11-guidelines.html>. With G1GC and Java 8 it is still slow.

## Cause

To be found.