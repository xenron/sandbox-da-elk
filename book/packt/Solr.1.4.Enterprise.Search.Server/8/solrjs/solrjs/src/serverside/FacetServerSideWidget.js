/**
 * Simple server side facet widget. Uses template "facets".
 * @class FacetServerSideWidget
 * @augments jQuery.solrjs.AbstractServerSideWidget
 */
jQuery.solrjs.FacetServerSideWidget = jQuery.solrjs.createClass ("AbstractServerSideWidget", /** @lends jQuery.solrjs.FacetServerSideWidget.prototype */ { 

  saveSelection : true,

  getSolrUrl : function(start) { 
    return "&facet=true&facet.field=" + this.fieldName;
  },

  getTemplateName : function() { 
  	return "facets"; 
  },  

  handleSelect : function(data) { 
	  jQuery(this.target).html(this.selectedItems[0].value);
    jQuery('<a/>').html("(x)").attr("href","javascript:solrjsManager.deselectItems('" + this.id + "')").appendTo(this.target);
  },

  handleDeselect : function(data) { 
    // do nothing, just refresh the view
  }	   

});