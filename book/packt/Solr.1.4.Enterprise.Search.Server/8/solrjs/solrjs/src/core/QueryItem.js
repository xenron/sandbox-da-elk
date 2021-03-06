/**
 * Represents a query item (search term). It consists of a fieldName and a value. .
 * 
 * @param properties A map of fields to set. Refer to the list of non-private fields.
 * @class QueryItem
 */
jQuery.solrjs.QueryItem = jQuery.solrjs.createClass (null, /** @lends jQuery.solrjs.QueryItem.prototype */  { 
    
  /** 
   * the field name.
   * @field 
   * @public
   */
  field : "",  
   
  /** 
   * The value
   * @field 
   * @public
   */
  value : "",  
    
  /**
   * creates a lucene query syntax, eg pet:"Cats"
   */  
  toSolrQuery: function() {
		return "(" + this.field + ":\"" + this.value + "\")";
  },
  
  /**
   * Uses fieldName and value to compare items.
   */
  equals: function(obj1, obj2) {
  	if (obj1.field == obj2.field && obj1.value == obj2.value) {
  		return true;
  	}
  	return false;
  }
    
});
