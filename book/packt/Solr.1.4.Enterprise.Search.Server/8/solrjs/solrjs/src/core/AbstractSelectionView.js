/**
 * Displays the current selection. Use the displaySelection method.
 * 
 * @param properties A map of fields to set. Refer to the list of non-private fields.  
 * @class AbstractSelectionView
 */
jQuery.solrjs.AbstractSelectionView = jQuery.solrjs.createClass (null, /** @lends jQuery.solrjs.AbstractSelectionView.prototype */ { 

  /** 
   * A unique identifier of this widget.
   *
   * @field 
   * @public
   */
  id : "", 
  
  /** 
   * A css classname representing the "target div" inside the html page.
   * All ui changes will be performed inside this empty div.
   * 
   * @field 
   * @private
   */
  target : "",

  displaySelection : function(selectedItems) { 
    throw "Abstract method displaySelection";    	
	}
	
});
