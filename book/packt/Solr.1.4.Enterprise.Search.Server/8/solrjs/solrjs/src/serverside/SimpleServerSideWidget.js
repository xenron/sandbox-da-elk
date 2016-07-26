/**
 * Simple server side widget, only gets a template.
 * 
 * @class SimpleServerSideWidget
 * @augments jQuery.solrjs.AbstractServerSideWidget
 */
jQuery.solrjs.SimpleServerSideWidget = jQuery.solrjs.createClass ("AbstractServerSideWidget", /** @lends jQuery.solrjs.SimpleServerSideWidget.prototype */{ 

  /** 
   * The name of the velocity template (without ".vm") to use.
   * @field 
   * @public
   */
   templateName : "",
   
  getTemplateName : function() { 
    return this.templateName; 
  }

});