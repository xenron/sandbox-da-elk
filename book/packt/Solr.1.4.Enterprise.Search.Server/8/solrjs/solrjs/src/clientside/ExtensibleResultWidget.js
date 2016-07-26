/**
 * A simple base class for result widgets (list of documents, including paging).
 * Implementations should override the renderResult(docs, pageSize, offset, numFound)
 * funtion to render the result.
 *
 * @class ExtensibleResultWidget
 * @augments jQuery.solrjs.AbstractClientSideWidget
 */
jQuery.solrjs.ExtensibleResultWidget = jQuery.solrjs.createClass ("AbstractClientSideWidget", { 
  
  isResult : true,
  
  getSolrUrl : function(start) { 
		return ""; // no special params need
	},

  handleResult : function(data) { 
    jQuery(this.target).empty();
    this.renderResult(data.response.docs, parseInt(data.responseHeader.params.rows), data.responseHeader.params.start, data.response.numFound);
	},

  renderResult : function(docs, pageSize, offset, numFound) { 
		throw "Abstract method renderDataItem";
  }
});