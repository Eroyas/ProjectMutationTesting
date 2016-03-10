package fr.unice.polytech.mojos;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Created by Eroyas on 08/03/16.
 */
public class JUnitRunListener extends RunListener {
    private int numberOfTestClass;
    private int testExecuted;
    private int testFailed;
    private int testStarted;
    private long begin;

    public JUnitRunListener(int numberOfTestClass) {
        this.setBegin(System.currentTimeMillis());
        this.numberOfTestClass = numberOfTestClass;
    }

    @Override
    public void testStarted(Description description) throws Exception {
        // for each test, increase number of executed test
        this.testExecuted += 1;
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        // for each failed test, increase number of failed test
        failure.getDescription().getMethodName();
        this.testFailed += 1;
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        // for each test started, increase number of started test
        this.testStarted += 1;
    }

    @Override
    public void testRunFinished(Result result) throws Exception {

    }

    @Override
    public void testFinished(Description description) throws Exception {

    }

    @Override
    public void testAssumptionFailure(Failure failure) {

    }

    @Override
    public void testIgnored(Description description) throws Exception {

    }

    /**
     * @return the numberOfTestClass
     */
    public int getNumberOfTestClass() {
        return numberOfTestClass;
    }

    /**
     * @param numberOfTestClass the numberOfTestClass to set
     */
    public void setNumberOfTestClass(int numberOfTestClass) {
        this.numberOfTestClass = numberOfTestClass;
    }

    /**
     * @return the testExecuted
     */
    public int getTestExecuted() {
        return testExecuted;
    }

    /**
     * @param testExecuted the testExecuted to set
     */
    public void setTestExecuted(int testExecuted) {
        this.testExecuted = testExecuted;
    }

    /**
     * @return the testFailed
     */
    public int getTestFailed() {
        return testFailed;
    }

    /**
     * @param testFailed the testFailed to set
     */
    public void setTestFailed(int testFailed) {
        this.testFailed = testFailed;
    }

    /**
     * @return the testExecuted
     */
    public int getTestStarted() {
        return testStarted;
    }

    /**
     * @param testStarted the testStarted to set
     */
    public void setTestStarted(int testStarted) {
        this.testStarted = testStarted;
    }

    /**
     * @return the begin
     */
    public long getBegin() {
        return begin;
    }

    /**
     * @param begin the begin to set
     */
    public void setBegin(long begin) {
        this.begin = begin;
    }

}