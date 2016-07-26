import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;

public final class EmbeddedSolrServerTest {
    public EmbeddedSolrServerTest() {
        super();
    }

    public void callme() throws Exception {
        CoreContainer coreContainer = new CoreContainer("D:/temp/solr");
        //coreContainer.load();
        for (String coreName : coreContainer.getCoreNames()) {
            System.out.println(coreName);
        }
        EmbeddedSolrServer server = new EmbeddedSolrServer(coreContainer, "collection1");
        SolrQuery solrQuery = new SolrQuery("*.*");
        QueryResponse response = server.query(solrQuery);
        SolrDocumentList dList = response.getResults();
        for (int i = 0; i < dList.getNumFound(); i++) {
            for (Map.Entry mE : dList.get(i).entrySet()) {
                System.out.println(mE.getKey() + ":" + mE.getValue());
            }
            System.out.println("******************");

        }
    }

    public static void main(String[] args) throws Exception {
        EmbeddedSolrServerTest class1 = new EmbeddedSolrServerTest();
        class1.callme();
    }
}
