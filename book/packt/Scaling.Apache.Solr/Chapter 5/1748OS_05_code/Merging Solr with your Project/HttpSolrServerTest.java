
import com.packtpub.solr.util.docs.EmbeddedSolrServerTest;

import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;

public class HttpSolrServerTest {
    public HttpSolrServerTest() {
        super();
    }

    public void callme() throws Exception {
        String urlString = "http://localhost:7001/myApplication/search";
        SolrServer solr = new HttpSolrServer(urlString);
        
        SolrQuery solrQuery = new SolrQuery("*.*");
        QueryResponse response = solr.query(solrQuery);
        SolrDocumentList dList = response.getResults();
        for (int i = 0; i < dList.getNumFound(); i++) {
            for (Map.Entry mE : dList.get(i).entrySet()) {
                System.out.println(mE.getKey() + ":" + mE.getValue());
            }
            System.out.println("******************");

        }
    }

    public static void main(String[] args) throws Exception {
        HttpSolrServerTest class1 = new HttpSolrServerTest();
        class1.callme();
    }

}
