java -Xms512M -Xmx1024M -Dcom.sun.management.jmxremote -Dfile.encoding=UTF8 -Djava.awt.headless=true -Dsolr.solr.home=cores -Dsolr.master.enable=true -Dsolr.clustering.enabled=true -Djetty.home=solr -Djetty.logs=solr/logs -jar solr/start.jar