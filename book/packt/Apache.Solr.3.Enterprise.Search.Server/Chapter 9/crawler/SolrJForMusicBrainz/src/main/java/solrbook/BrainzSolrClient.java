package solrbook;

import java.io.File;

/**
 * Simple command line application that allows you to index ARC records into
 * Solr via either the remote HTTP interface or via the pure Java based Embedded
 * Solr Server
 * 
 */
public class BrainzSolrClient {

	/**
	 * Three parameters are required for the main method. If none are passed in
	 * then the defaults are used.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String pathToArcs = null;
		String remoteOrLocal = null;
		String connectionParameter = null;

		if (args.length == 0) {
			pathToArcs = "../heritrix-2.0.2/jobs/completed-musicbrainz-only-artists-20090707185058/arcs/";
			remoteOrLocal = Indexer.EMBEDDED;
			connectionParameter = "../../../cores";
		} else if (args.length != 3) {
			throw new Exception(
					"If you pass in parameters, we expect three parameters, the first is the relative path to the arcs, the second is REMOTE or EMBEDDED, the third is either URL for REMOTE like http://localhost:8983/solr/crawler or the PATH for EMBEDDED like ../../../cores");
		} else {
			pathToArcs = args[0];
			remoteOrLocal = args[1];
			connectionParameter = args[2];
		}

		File arcDir = new File(pathToArcs).getAbsoluteFile();
		if (!arcDir.exists()) {
			throw new Exception("ARC dir:" + arcDir + " doesn't exist");
		}

		Indexer indexer = new Indexer();
		indexer.arcDir = arcDir;
		indexer.connectionType = remoteOrLocal;
		indexer.connectionParameter = connectionParameter;

		indexer.startSolr();

		indexer.deleteAll();

		indexer.index();

		indexer.shutdown();

	}

}
