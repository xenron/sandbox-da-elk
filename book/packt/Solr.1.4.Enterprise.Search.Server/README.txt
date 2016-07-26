Welcome to the Solr 1.4 Enterprise Search book example code.  

All the examples are broken up by chapter number, ie /8 contains examples for Chapter 8: Integration.  The bundled /solr directory contains a version of Solr 1.4 trunk that has been tested with the sample code.

Most of the code here and most of the sample searches in the book use MusicBrainz.org data.  MB data for artists, releases, and tracks has been packaged up into CSV and solr-xml formats.  These three large data files hosted externally and are downloaded into "downloads" directory.  The ant build.xml file along side this read-me is a script which will download, decompress, and index them into a a multi-core Solr setup.

To index the MB data files, first open a command prompt here to the directory where this README file is. From this directory, run:
>> ant index
This of course presumes you have Apache Ant which you will quickly discover is the case or not by typing just "ant".  The whole process might take over an hour.  Each file in the downloads directory is indexed into a Solr instance which is run so that it can index the needed data.  You'll need a couple GB of space or so to run this though you should have plenty more free space around for a healthy running system of course.  If you only want to do some of the data or just re-index it after having made modifications to solr's configuration then take a look at the build.xml script.

You probably won't run into memory issues running this script but if you do then you can allocate more memory to Ant by setting the ANT_OPTS system property. On *nix systems you would do:

>> export ANT_OPTS=-Xmx256m

Once the ant script is finished, you will want to run Solr....

Start Solr by running:

>> java -Xms512M -Xmx1024M -Dfile.encoding=UTF8 -Dsolr.solr.home=cores -Djetty.home=solr -Djetty.logs=solr/logs -jar solr/start.jar 
   
Browse to http://localhost:8983/solr and you should see the various cores listed. 
   
   
-Xms specifies the minimum amount of memory to assign to the JVM on startup
-Xmx specifies the maximum amount of memory the JVM can use.

-Dfile.encoding specifies that we want UTF8 encoding, which is what the MusicBrainz data is encoded in as read in from data files.
-Dsolr.solr.home specifies the directory Solr should look in for configuration values, in this case it is a multicore Solr instance.
-DJetty.home specifies to Jetty where it should look for configuration values and WAR files
-Djetty.log specifies where HTTP server logs should be stored.

-jar solr/start.jar specifies the startup Jar that starts Jetty and Solr.

To learn more about optimizing memory allocations, refer to the last chapter, Chapter 9: Scalability.  

Starting up Solr with Java Replication: 
-Dslave=disabled starts the master Solr.
-Dmaster=disabled starts a slave Solr.   

Starting up Solr with JMX Support:
-Dcom.sun.management.jmxremote specifies that we want JMX support.
-Dcom.sun.management.jmxremote.port=3000 specifies the port to connect to.
-Dcom.sun.management.jmxremote.ssl=false controls if SSL is used.
-Dcom.sun.management.jmxremote.authenticate=false turns off authentication to JMX.
