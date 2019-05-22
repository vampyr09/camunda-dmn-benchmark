package io.github.vampyr09.experiment.camunda.dmn.benchmark.domain;

import java.util.Objects;

/**
 * Simple domain model used to evaluate the dmn decision tables.
 */
public class Course {

    private final CourseType courseType;

    private final UserGroup userGroup;

    private final SportsType sportsType;

    /**
     * The course duration in weeks.
     */
    private final int courseDurationW;

    /**
     * The course training duration in minutes.
     */
    private final int trainingDurationMin;

    /** 
     * The amount of trainings for this course.
     */
    private final int trainingsAmount;

    public Course(final CourseType courseType, final UserGroup userGroup, final SportsType sportsType,
            final int courseDurationW, final int trainingDurationMin, final int trainingsAmount) {
        Objects.requireNonNull(courseType, "courseType must not be null.");
        Objects.requireNonNull(userGroup, "userGroup must not be null.");
        Objects.requireNonNull(sportsType, "sportsType must not be null.");

        this.courseType = courseType;
        this.userGroup = userGroup;
        this.sportsType = sportsType;
        this.courseDurationW = courseDurationW;
        this.trainingDurationMin = trainingDurationMin;
        this.trainingsAmount = trainingsAmount;
    }

    public CourseType getCourseType() {
        return this.courseType;
    }

    public UserGroup getUserGroup() {
        return this.userGroup;
    }

    public SportsType getSportsType() {
        return this.sportsType;
    }

    public int getCourseDurationW() {
        return this.courseDurationW;
    }

    public int getTrainingDurationMin() {
        return this.trainingDurationMin;
    }

    public int getTrainingsAmount() {
        return this.trainingsAmount;
    }

    @Override
    public String toString() {
        return "Course [courseType=" + this.courseType + ", userGroup=" + this.userGroup + ", sportsType="
                + this.sportsType + ", courseDurationW=" + this.courseDurationW + ", trainingDurationMin="
                + this.trainingDurationMin + ", trainingsAmount=" + this.trainingsAmount + "]";
    }

}
