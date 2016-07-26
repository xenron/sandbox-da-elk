package com.packtpub.solr.util.docs;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
/**
 * Use this sample uploader to upload your documents with custom information/metadata.
 * 
 * Author: Hrishikesh Karambelkar
 */
public class CustomDocsManagerUploader {
    
    public static String urlString = "http://localhost:8983/solr";
    public CustomDocsManagerUploader() {
        super();
    }
    
    public void indexFile(String filePath, Map<String,String> metadata) throws Exception {
        SolrServer solr = new CommonsHttpSolrServer(urlString);
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
        up.setParam("literal.id", filePath);
        for (String key : metadata.keySet()) {
            //set the literals
            up.setParam("literal." + key, metadata.get(key));
        }
        up.addFile(new File(filePath));
        up.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
        solr.request(up);

    }
    
    public static void main(String[] args) throws Exception{
        CustomDocsManagerUploader cdmu = new CustomDocsManagerUploader();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("tags", "java, design document, aarchitecture");
        parameters.put("status", "under-review");
        parameters.put("status", "under-review");
        cdmu.indexFile("D:\\hrishi\\personal\\book\\Scaling Solr\\chapter3\\code\\com\\packtpub\\solr\\util\\docs\\CustomDocsManagerUploader.java", parameters);        
    }
}
