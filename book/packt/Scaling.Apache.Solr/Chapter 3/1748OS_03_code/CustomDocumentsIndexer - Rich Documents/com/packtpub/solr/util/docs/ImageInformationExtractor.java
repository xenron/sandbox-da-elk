package com.packtpub.solr.util.docs;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;

public class ImageInformationExtractor {
    public ImageInformationExtractor() {
        super();
    }
    String urlString = "http://localhost:8983/search";

    private void indexFilesSolrCell(String filePath) throws IOException, SolrServerException {
        SolrServer solr = new CommonsHttpSolrServer(urlString);
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");

        boolean isImage = false;

        File tempFile = File.createTempFile("fileName", ".tmp");
        //we use tessaract, and it can be called from comand line. with source, and target file name in which the 
        //text is expected
        String commandLine = "tesseract \"" + filePath + "\" \"" + tempFile.getAbsolutePath() + "\"";
        System.out.println(commandLine);
        Process p = Runtime.getRuntime().exec(commandLine);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File fNew = new File(tempFile.getAbsolutePath() + ".txt");
        up.addFile(fNew);
        //since its a scanned document

        up.setParam("uprefix", "attr_");
        up.setParam("fmap.content", "attr_content");
        up.setParam("literal.id", filePath);
        up.setParam("literal.URL", filePath);

        up.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);

        solr.request(up);
        if (tempFile != null)
            tempFile.deleteOnExit();

    }

    public static void main(String[] args) throws IOException, SolrServerException {
        String filesPath = "C:\\scanned1.jpg";
        ImageInformationExtractor mgr = new ImageInformationExtractor();


        StringTokenizer tokens = new StringTokenizer(filesPath, ",");
        while (tokens.hasMoreTokens()) {
            mgr.indexFilesSolrCell(tokens.nextToken());
        }

    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

}
