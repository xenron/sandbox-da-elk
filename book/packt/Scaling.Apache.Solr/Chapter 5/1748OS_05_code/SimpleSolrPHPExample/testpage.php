<html>
    <head>
        <title>My Company Search</title>
   </head>
    <body>
<?php
$code = file_get_contents('http://localhost:8983/solr/select?q=No&wt=php');
eval("\$result = " . $code . ";");
//print_r($result)

/*foreach ($result as $entry) {
    print_r($entry['type']);
}*/
?>   
<!--</br></br>-->
<?php
//print_r($result['responseHeader'])
?>
<!--</br></br>-->
<?php
//print_r($result['response'])
?>
<!--</br></br>-->
<?php
foreach ($result['response'] as $response) {
//print_r($response);
foreach ($response as $doc) {
//print_r($doc);
/*foreach ($doc as list ($key, $value)) {*/
foreach ($doc as $key => $value) {
    print_r($key);
	print_r('   :   ');
	print_r($value);
	print_r('<br/>');
	}
	print_r('<br/><br/>');
}
}
//print_r($result['response'])
?>

   </body>
</html>
