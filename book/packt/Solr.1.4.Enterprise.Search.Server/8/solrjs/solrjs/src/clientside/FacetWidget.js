/**
 * A simple facet widget that shows the facet values as list. It remembers the selection
 * and shows a "value(x)" label after selection.
 *
 * @class ExtensibleResultWidget
 * @augments jQuery.solrjs.AbstractClientSideWidget
 */
jQuery.solrjs.FacetWidget = jQuery.solrjs.createClass ("AbstractClientSideWidget", { 
  
  saveSelection : true,
  
  getSolrUrl : function(start) { 
		return "&facet=true&facet.field=" + this.fieldName;
  },

  handleResult : function(data) { 
	 var values = data.facet_counts.facet_fields[this.fieldName];	 
     jQuery(this.target).html("");
		
		for (var i = 0; i < values.length; i = i + 2) {
			var items =  "[new jQuery.solrjs.QueryItem({field:'" + this.fieldName + "',value:'" +  values[i] + "'})]";
      var label = values[i] + "(" + values[i+1] + ")";     	
			jQuery('<a/>').html(label).attr("href","javascript:solrjsManager.selectItems('" + this.id + "'," + items + ")").appendTo(this.target);
			jQuery('<br/>').appendTo(this.target);
		}
	},

	handleSelect : function(data) { 
		jQuery(this.target).html(this.selectedItems[0].value);
		jQuery('<a/>').html("(x)").attr("href","javascript:solrjsManager.deselectItems('" + this.id + "')").appendTo(this.target);
	},

	handleDeselect : function(data) { 
		// do nothing
	}	   
});