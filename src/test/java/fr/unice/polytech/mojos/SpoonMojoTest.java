package fr.unice.polytech.mojos;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Unit test for simple App.
 */
public class SpoonMojoTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SpoonMojoTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SpoonMojoTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws MojoFailureException, MojoExecutionException {
    }
}
