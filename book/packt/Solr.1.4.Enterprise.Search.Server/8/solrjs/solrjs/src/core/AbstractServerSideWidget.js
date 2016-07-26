/**
 * Baseclass for server side widgets.
 * 
 * <p> The velocity response writer is used, the widget only specifies the
 *     template name in the getTemplateName() method. </p>
 *
 * @param properties A map of fields to set. Refer to the list of non-private fields. 
 * @class AbstractServerSideWidget
 * @augments jQuery.solrjs.AbstractWidget
 */
jQuery.solrjs.AbstractServerSideWidget = jQuery.solrjs.createClass ("AbstractWidget", /** @lends jQuery.solrjs.AbstractServerSideWidget.prototype */  { 
  
  /**
   * Adds the velocity specific request parameters to the url and creates a JSON call
   * using a dynamic script tag. The html response from the velocity template gets wrapped inside a 
   * javascript object to make cross site requests possible.
   * 
   * @param url The solr query request
   */
  executeHttpRequest : function(url) { 
    url += "&wt=velocity&vl.response=QueryResponse&vl.json=?&jsoncallback=?&vl.template=" + this.getTemplateName();
    url += "&solrjs.widgetid=" + this.id;   
    var me = this;
    jQuery.getJSON(url,
      function(data){
        me.handleResult(data.result);
      }
    );
  },
  
  getTemplateName : function() { 
   throw("Abstract method");
  },
  
  /**
   * The default behaviour is that the result of the template is simply "copied" to the target div.
   * 
   * @param result The result of the velocity template wrapped inside a javascript object.
   */
  handleResult : function(result) { 
    jQuery(this.target).html(result);
  },

});

