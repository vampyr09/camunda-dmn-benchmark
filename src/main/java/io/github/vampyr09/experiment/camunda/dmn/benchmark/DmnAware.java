package io.github.vampyr09.experiment.camunda.dmn.benchmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.engine.variable.Variables;

import io.github.vampyr09.experiment.camunda.dmn.benchmark.domain.Course;

/**
 * Is responsible to setup the {@link #dmnEngine} and to hold the {@link DmnDecision decision tables}.
 */
public abstract class DmnAware {

    private DmnEngine dmnEngine;

    /** decisions mapped by its decision key. */
    private final Map<String, DmnDecision> dmnDecisions = new HashMap<>();
    
    public void buildEngine() {
        this.dmnEngine = new DefaultDmnEngineConfiguration().buildEngine();
    }

    public DmnEngine getDmnEngine() {
        return this.dmnEngine;
    }

    public Map<String, DmnDecision> getDmnDecisions() {
        return this.dmnDecisions;
    }

    /**
     * Parse the dmn file given by its fileName and with the <b>contract</b> that decision table key (id)
     * and filename are equal.
     * 
     * The parsed decisions are hold in the {@link #dmnDecisions} mapped by its decision table key. 
     * 
     * @param dmnEngine the dmn engine to parse the decision
     * @param fileName the file name and decision name
     * @throws FileNotFoundException if the file with the given file name is not found
     */
    void parseDecision(final DmnEngine dmnEngine, final String fileName) throws FileNotFoundException {
        Objects.requireNonNull(dmnEngine, "dmnEngine must not be null.");
        Objects.requireNonNull(fileName, "fileName must not be null.");
        
        try (InputStream dmnFile = getClass().getClassLoader().getResourceAsStream(fileName + ".dmn")) {
            if (dmnFile == null) {
                throw new FileNotFoundException("DMN file '" + fileName + ".dmn' not found");
            }

            this.dmnDecisions.put(fileName, dmnEngine.parseDecision(fileName, dmnFile));
        } catch (IOException e) {
            throw new RuntimeException("Error handling dmn file: " + fileName, e);
        }
    }
    
    /**
     * Evaluate the parsed decision table for the given course.
     * 
     * @param decisionKey the key for the parsed decision table
     * @param course the course
     * @return the evaluated {@link DmnDecisionResult}
     */
    DmnDecisionResult evaluateDecisionWithCourse(final String decisionKey, final Course course) {
        Objects.requireNonNull(decisionKey, "decisionKey must not be null.");
        Objects.requireNonNull(course, "course must not be null.");
        Objects.requireNonNull(getDmnEngine(), "dmnEngine must not be null. Maybe you forgot to call 'buildEngine()'?");

        Optional<DmnDecision> decision = Optional.ofNullable(getDmnDecisions().get(decisionKey));
        if (decision.isPresent()) {
            return getDmnEngine().evaluateDecision(decision.get(),
                    Variables.createVariables().putValue("course", course));
        } else {
            throw new RuntimeException("No dmn decision parsed for decision key: '" + decisionKey + "'.");
        }
    }

}
