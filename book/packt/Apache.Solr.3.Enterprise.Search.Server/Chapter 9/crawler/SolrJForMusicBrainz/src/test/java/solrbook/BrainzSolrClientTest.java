package solrbook;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple BrainzSolrClient.
 */
public class BrainzSolrClientTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BrainzSolrClientTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BrainzSolrClientTest.class );
    }


    public void testHeritrix2() throws Exception
    {
        String[] params = {"../heritrix-2.0.2/jobs/completed-musicbrainz-only-artists-20090707185058/arcs/","REMOTE","http://localhost:8983/solr/crawler"}; 
        
        BrainzSolrClient.main(params);
    }

/**
     * Rigorous Test :-)
     * @throws Exception 
     */
    public void offtestHeritrix3() throws Exception
    {

        String[] params = {"../heritrix-3.0.0/jobs/test2/warcs","REMOTE","http://localhost:8983/solr/crawler"}; 
        
        BrainzSolrClient.main(params);
    }
}
