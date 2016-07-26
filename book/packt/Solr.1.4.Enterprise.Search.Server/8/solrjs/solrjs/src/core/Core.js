/**
 * @namespace A unique namespace inside jQuery.
 */
jQuery.solrjs = function() {};


/** 
 * A static "constructor" method that creates widget classes.
 *
 * <p> It uses manual jquery inheritance inspired by 
 * http://groups.google.com/group/jquery-dev/msg/12d01b62c2f30671' </p>
 * 
 * @param baseClass The name of the parent class. Set null to create a top level class. 
 * @param subClass The fields and methods of the new class.
 * @returns A constructor method that represents the new class. 
 * 
 * @example
 *   jQuery.solrjs.MyClass = jQuery.solrjs.createClass ("MyBaseClass", {
 *      property1: "value",
 *      function1: function() { alert("Hello World") }
 *   });
 *
 * 
 */
jQuery.solrjs.createClass = function(baseClassName, subClass) {
		
	// create new class, adding the constructor 
  var newClass = jQuery.extend(true, subClass , {	

		constructor : function(options) {
			// if a baseclass is specified, inherit methods and props, and store it in _super_			
			if (baseClassName != null) { 
	    	jQuery.extend(true, this, new jQuery.solrjs[baseClassName](options) );
			}
			// add new methods and props for this class
		  jQuery.extend(true, this , subClass);
      // add constructor arguments	    
			jQuery.extend(true, this, options); 
  	}
	});
	
	// make new class accessible
	return newClass.constructor;

};
