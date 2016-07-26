/**
 * Baseclass for client side widgets. 
 *
 * <p> The json response writer is used, the widget gets the result object passed
 *     to the handleResult() method
 * </p>
 * 
 * @param properties A map of fields to set. Refer to the list of non-private fields. 
 * @class AbstractClientSideWidget
 * @augments jQuery.solrjs.AbstractWidget
 */
jQuery.solrjs.AbstractClientSideWidget = jQuery.solrjs.createClass ("AbstractWidget", /** @lends jQuery.solrjs.AbstractClientSideWidget.prototype */ { 
  
  /**
   * Adds the JSON specific request parameters to the url and creates a JSON call
   * using a dynamic script tag.
   * @param url The solr query request
   */
  executeHttpRequest : function(url) { 
  	url += "&wt=json&json.wrf=?&jsoncallback=?";  
    var me = this;
  	jQuery.getJSON(url,
  	  function(data){
  		me.handleResult(data);    
  	  }
  	);
  }, 
  
  /**
   * An abstract hook for child implementations. It is a callback that
   * is execute after the solr response data arrived.
   * @param data The solr response inside a javascript object.
   */
  handleResult : function(data) { 
	 throw "Abstract method handleResult"; 
  }

});
