package pl.edu.agh.codecomp;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import pl.edu.agh.codecomp.algorithm.BoyerMoore;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        BoyerMoore bm = new BoyerMoore();
        List<Integer> result = bm.match("abcabcabcabcabc", "cabc");
//        for(int i = 0; i < result.size(); i++)
//        	System.out.println(result.get(i));
        assertEquals(true, !result.isEmpty());
    }
}
