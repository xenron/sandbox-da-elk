<?php
error_reporting(E_ALL);
ini_set('display_errors', true);//displays error
require('library/Solarium/Autoloader.php'); //put the web path the autoloader.php 
Solarium_Autoloader::register();

$config = array(
    'adapteroptions' => array(
        'host' => 'localhost',
        'port' => 8983,
        'path' => '/solr/',
    )
);
$client = new Solarium_Client($config);
echo '<html><head><title>Solarium examples</title></head><body>';
$query = $client->createSelect();
$resultset = $client->select($query);// this executes the query and returns the result

echo 'NumFound: '.$resultset->getNumFound();// display the total number of documents found by solr

foreach ($resultset as $document) {
    echo '<hr/><table>';
    foreach($document AS $field => $value)
    {
        if(is_array($value)) $value = implode(', ', $value);        // this converts multivalue fields to a comma-separated string        
        echo '<tr><th>' . $field . '</th><td>' . $value . '</td></tr>';
    }
    echo '</table>';
}
echo '</body></html>';
?>