/**
 * A facet widget that renders the values as a tagcloud.
 *
 * @class TagcloudWidget
 * @augments jQuery.solrjs.AbstractClientSideWidget
 */
jQuery.solrjs.TagcloudWidget = jQuery.solrjs.createClass ("AbstractClientSideWidget", { 

  /** 
   * Maximum count of items in the tagcloud. 
   *
   * @field 
   * @public
   */
  size : 20,
  
  /** 
   * The facet field name.
   *
   * @field 
   * @public
   */
  fieldName : "",  
  
  getSolrUrl : function(start) { 
		return "&facet=true&facet.mincount=1&facet.field=" + this.fieldName + "&facet.limit=" + this.size;
  },

  handleResult : function(data) { 
	 var values = data.facet_counts.facet_fields[this.fieldName];	 
     jQuery(this.target).empty();
     
     if (values.length == 0) {
       jQuery("<div/>").html("no items found in current selection").appendTo(this.target);
     }
		
		 var maxCount = 0;
		 var objectedItems = [];
		 for (var i = 0; i < values.length; i = i + 2) {
		    var c = parseInt(values[i+1]);
		    if (c > maxCount) {
		      maxCount = c;
		    }
		    objectedItems.push({label:values[i], count:values[i+1]});
		 }
		 
		 objectedItems.sort(function(a,b) {
		   if (a.label < b.label) {
		    return -1;
		   }
		   return 1;  
		 });
		 
		 for (var i = 0; i < objectedItems.length; i++) {
       var label = objectedItems[i].label;
			 var items =  "[new jQuery.solrjs.QueryItem({field:'" + this.fieldName + "',value:'" +  label + "'})]";
       var percent =  (objectedItems[i].count / maxCount);
       var tagvalue = parseInt(percent * 10);     	
			 jQuery("<a/>").html(label).addClass("solrjs_tagcloud_item").addClass("solrjs_tagcloud_size_" + tagvalue).attr("href","javascript:solrjsManager.selectItems('" + this.id + "'," + items + ")").appendTo(this.target);
		 }
		 
		 jQuery("<div/>").addClass("solrjs_tagcloud_clearer").appendTo(this.target);
		 
	}
});