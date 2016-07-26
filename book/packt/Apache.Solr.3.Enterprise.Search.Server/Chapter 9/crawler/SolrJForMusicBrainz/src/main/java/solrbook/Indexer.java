package solrbook;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.StreamingUpdateSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveReaderFactory;
import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.xml.sax.SAXException;


public class Indexer {

	String connectionType; // how do we connect to Solr?
	boolean printOutUrls = true; // Print to System.out each URL being indexed
	boolean indexIntoSolr = true; // Actually perform indexing?
	boolean indexAsSolrDocument = true; // true to index as a SolrDocument or false to index as POJO
	String connectionParameter;
	File arcDir;

	private CoreContainer container; // used only by EmbeddedSolrServer
	public SolrServer solr = null;
	
	public static String EMBEDDED = "EMBEDDED";
	public static String REMOTE = "REMOTE";
	public static String STREAMING = "STREAMING";

	public void startSolr() throws Exception {
		if (connectionType.equalsIgnoreCase(REMOTE)) {
			solr = startRemoteSolr();
		} else if (connectionType.equalsIgnoreCase(EMBEDDED)) {
			solr = startEmbeddedSolr();
		}
		else if (connectionType.equalsIgnoreCase(STREAMING)){
			solr = startStreamingSolr();
		} else {
			throw new Exception("The second parameter:" + connectionType
					+ " needs to be either REMOTE or EMBEDDED");
		}

	}

	public void index() throws Exception {

		performSearch("*:*");

		long start = System.currentTimeMillis();
		File arcFiles[] = arcDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".arc");
            }
        });
        if (arcFiles == null)
            throw new Exception("No .arc files found in "+arcDir);
		int hits = 1;
		for (File arcFile : arcFiles) {
			System.out.println("Reading " + arcFile.getName());
			ArchiveReader r = ArchiveReaderFactory.get(arcFile);
			r.setDigest(true);

			for (ArchiveRecord rec : r) {
				if (rec != null) {
					ArchiveRecordHeader meta = rec.getHeader();
					if (meta.getMimetype().trim().startsWith("text/html")) {

						if (printOutUrls) {
							System.out.println(meta.getUrl());
						}
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						rec.dump(baos);
						if (indexIntoSolr) {
							if (indexAsSolrDocument) {
								indexAsSolrDocument(meta, baos);
							} else {
								indexAsPOJO(meta, baos);
							}
						}
						hits++;
					}
				}
				rec.close();
				if (indexIntoSolr && hits % 100 == 0) {
					// Uncomment this to see the impact that frequent commits has.
					//System.out.println("Commit after adding " + hits);
					//solr.commit();
				}
			}
		}
		System.out.println("Index time was " + (System.currentTimeMillis() - start) + " ms for "
				+ hits + " documents");
		solr.commit();
		solr.optimize();
		
		// do a search that returns results as beans and displays count of hits.
		if (!indexAsSolrDocument){
			performBeanSearch("*:*");
		}


		System.out.println("Execution time was " + (System.currentTimeMillis() - start) + " ms for "
				+ hits + " documents");
	}

	/**
	 * Demonstrate the typical use case of reading from a record, store as a
	 * SolrDocument.  
	 * 
	 * Note: in any real situation you would want to only extract the
	 * HTML content from the baos parameter.  Currently it contains the full HTTP request,
	 * headers and all!  Also, you should use baos.toString("ISO-8859-1") or whatever is
	 * the correct character encoding.
	 * 
	 * @param meta
	 * @param baos
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 * @throws IOException
	 */
	private void indexAsSolrDocument(ArchiveRecordHeader meta,
			ByteArrayOutputStream baos) throws MalformedURLException,
			SolrServerException, IOException {
		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("url", meta.getUrl(), 1.0f);
		doc.addField("mimeType", meta.getMimetype(), 1.0f);
		doc.addField("docText", baos.toString()); // should parse out HTML body and specify character encoding!
		URL url = new URL(meta.getUrl());
		doc.addField("host", url.getHost());
		doc.addField("path", url.getPath());
		solr.add(doc);
	}

	/**
	 * Demonstrate the use case of storing a POJO into Solr, using a record as
	 * the data source to facilitate this example.
	 * 
	 * Note: in any real situation you would want to only extract the
	 * HTML content from the baos parameter.  Currently it contains the full HTTP request,
	 * headers and all!  Also, you should use baos.toString("ISO-8859-1") or whatever is
	 * the correct character encoding.
	 * 
	 * @param meta
	 * @param baos
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 * @throws IOException
	 */
	private void indexAsPOJO(ArchiveRecordHeader meta,
			ByteArrayOutputStream baos) throws MalformedURLException,
			SolrServerException, IOException {
		RecordItem item = new RecordItem();

		item.setId(meta.getUrl());
		item.setMimeType(meta.getMimetype());
		item.setHtml(baos.toString());
		URL url = new URL(meta.getUrl());
		item.setHost(url.getHost());
		item.setPath(url.getPath());
		solr.addBean(item);
	}

	/**
	 * Starts a connection to a local embedded Solr that connects directly,
	 * without using HTTP as a transport layer.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws SolrServerException
	 */
	public SolrServer startEmbeddedSolr() throws IOException,
			ParserConfigurationException, SAXException, SolrServerException {

		File root = new File(connectionParameter);
		container = new CoreContainer();

		SolrConfig config = new SolrConfig(root + "/crawler", "solrconfig.xml",
				null);
		CoreDescriptor descriptor = new CoreDescriptor(container, "crawler",
				root + "/solr");
		SolrCore core = new SolrCore("crawler",
				root + "/../cores_data/crawler", config, null, descriptor);

		container.register(core, false);
		EmbeddedSolrServer solr = new EmbeddedSolrServer(container, "crawler");
		return solr;
	}

	/**
	 * Starts a connection to a remote Solr using HTTP as the transport
	 * mechanism.
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws SolrServerException
	 */
	public SolrServer startRemoteSolr() throws MalformedURLException,
			SolrServerException {
		CommonsHttpSolrServer solr = new CommonsHttpSolrServer(
				connectionParameter);
		solr.setRequestWriter(new BinaryRequestWriter());
		return solr;
	}

	/**
	 * Starts a connection to a remote Solr server that streams documents, using 3 threads and 20 documents in the queue.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws SolrServerException
	 */
	public SolrServer startStreamingSolr() throws IOException,
			ParserConfigurationException, SAXException, SolrServerException {

		StreamingUpdateSolrServer solr = new StreamingUpdateSolrServer(
				connectionParameter, 20, 3);
		solr.setRequestWriter(new BinaryRequestWriter());
		return solr;
	}

	/**
	 * Perform a very simple search
	 * 
	 * @param query
	 * @throws SolrServerException
	 */
	public void performSearch(String query) throws SolrServerException {
		SolrQuery solrQuery = new SolrQuery(query);
		QueryResponse response = solr.query(solrQuery);
		System.out.println("Perform Search for '*:*': response = " + response);
	}
	
	public List<RecordItem> performBeanSearch(String query) throws SolrServerException {
		SolrQuery solrQuery = new SolrQuery(query);
		QueryResponse response = solr.query(solrQuery);
		List<RecordItem> beans = response.getBeans(RecordItem.class);
		System.out.println("Perform Search for '" + query + "': found " + beans.size() + " beans.");
		return beans;
	}

	public void deleteAll() throws SolrServerException, IOException {
		solr.deleteByQuery("*:*"); // delete everything!
		solr.commit();

	}

	public void shutdown() {
		if (container != null) {
			container.shutdown();
		}
	}

	public String getRemoteOrLocal() {
		return connectionType;
	}

	public void setRemoteOrLocal(String remoteOrLocal) {
		this.connectionType = remoteOrLocal;
	}

	public boolean isPrintOutUrls() {
		return printOutUrls;
	}

	public void setPrintOutUrls(boolean printOutUrls) {
		this.printOutUrls = printOutUrls;
	}

	public boolean isIndexIntoSolr() {
		return indexIntoSolr;
	}

	public void setIndexIntoSolr(boolean indexIntoSolr) {
		this.indexIntoSolr = indexIntoSolr;
	}

	public File getArcDir() {
		return arcDir;
	}

	public void setArcDir(File arcDir) {
		this.arcDir = arcDir;
	}

	public String getConnectionParameter() {
		return connectionParameter;
	}

	public void setConnectionParameter(String connectionParameter) {
		this.connectionParameter = connectionParameter;
	}

}
