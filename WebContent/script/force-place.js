/**
*	Force alignment of primefaces paginator components and rename links.
*	@Author: Patrick Reid
*	@Modified: Ricardo Thompson
*/


$.fn.extend({
	forceDatatablePagin: function() {
		
		//Variables 
		var dtControlsParent;
		var dtControls;
		var dtSwitchParent;
		var dtSwitch;
		var current;
		var count = 0;
		var x = 0;
		var thisVar = $(this);
		
		
		//Find all paginator components 
		dtControlsParent = $(this).find('.ui-paginator.ui-paginator-bottom');
		dtControls = dtControlsParent.find('span');
		dtSwitchParent = $(this).find('.ui-datatable-header.ui-widget-header.ui-corner-top');
		dtSwitch = dtControlsParent.find('.ui-paginator-rpp-options.ui-corner-left');
		
		//Add temp holder for Text with results
		current = dtControlsParent.find('.ui-paginator-current').text();
		dtSwitchParent.prepend('<span id="tempId">'+current+'</span>');
		
		//$('#page-content').bind('mouseenter',function(){
		setTimeout(function() {
			//alert("interval/clear");
			
		thisVar.each(function(){
			//alert("mod bind added and unbind, add thisVar4 remove temp, multiple table added in court");
			//align paginator
			dtControls.css('text-align', 'center');
			dtControlsParent.css('text-align', 'right');
			dtSwitch.css('float', 'right').css('position', 'relative').css('right', '-38px');
			
			
			//Appends the Data Size control to header right of datatable 
			dtSwitchParent.append(dtSwitch);
			dtSwitchParent.find('select:not(:first)').remove();
			
			
			//CSS for Data size control
			dtSwitchParent.find('.ui-commandlink.ui-widget').css('margin', '0');
			dtControlsParent.find('.ui-state-default .ui-icon').css('text-indent', '0').css('width', 'auto').css('background-image', 'none');
			
			//Remove Temp holder
			dtSwitchParent.find('#tempId').remove();
			dtControlsParent.find('.ui-paginator-current').detach().prependTo(dtSwitchParent);//append the String with results
			
			
			//Styling of paginator controls
			//add the appropriate text to the paginator
			dtControlsParent.find('.ui-paginator-first').insertAfter(dtControlsParent.find('.ui-paginator-next'));
			dtControlsParent.find('.ui-paginator-first .ui-icon').html('First');
			dtControlsParent.find('.ui-paginator-first').css('background', 'transparent').css('border', 'none').css('border-radius', '0px').css('color', '#fefefe');
			dtControlsParent.find('.ui-paginator-last').css('border-left', '1px solid #dfeffc').css('border-right', 'none').css('border-top', 'none').css('border-bottom', 'none').css('background', 'transparent').css('border-radius', '0px').css('color', '#fefefe');
			dtControlsParent.find('.ui-paginator-last .ui-icon').html('Last');
			dtControlsParent.find('.ui-paginator-next').css('border-left', '1px solid #dfeffc').css('border-right', 'none').css('border-top', 'none').css('border-bottom', 'none').css('margin-right', '20px').css('background', 'transparent').css('border-radius', '0px').css('color', '#fefefe');;
			dtControlsParent.find('.ui-paginator-next .ui-icon').html('Next &raquo;');
			dtControlsParent.find('.ui-paginator-pages').hide();
			dtControlsParent.find('.ui-paginator-prev .ui-icon').html('&laquo; Prev');
			dtControlsParent.find('.ui-paginator-prev').css('background', 'transparent').css('border', 'none').css('border-radius', '0px').css('color', '#fefefe');
			
			
		
			//Only occurs once
			//Appends the word item infront of size control
			$('<span class="it3m5iq" style="float: right; position: relative; right: 55px;">Items:</span>').insertBefore(dtSwitch);
					//$('.it3m5iq:contains(Items:):not(:first)').remove();
				
				
		});
			//$('#page-content').unbind();//unbind event	
		},100);
	}
});

//Data Grid Method below

$.fn.extend({
	forceDataGridPagin: function() {
		
		var thisVar = $(this);
		
		//alert('datagrid found!*');
		var dtControlsParent;
		var dtControls;
		var dtSwitchParent;
		var dtSwitch;
		
		dtControlsParent = thisVar.find('.ui-paginator.ui-paginator-bottom');
		dtControls = dtControlsParent.find('span');
		dtSwitchParent = thisVar.find('.ui-datagrid-header.ui-widget-header.ui-corner-top');
		dtSwitch = dtControlsParent.find('.ui-paginator-rpp-options.ui-corner-left');
		//alert(dtSwitch.text());
		
		dtControls.css('text-align', 'center');
		dtControlsParent.css('text-align', 'right');
		
		//Add temp holder for Text with results
		var current = dtControlsParent.find('.ui-paginator-current').text();
		dtSwitchParent.prepend('<span id="tempId">'+current+'</span>');
		
		//
		setTimeout(function(){
		thisVar.each(function(){
			//x++;
			//alert(x);
			//alert("after x");
			
			
			
			dtControls.css('text-align', 'center');
			dtControlsParent.css('text-align', 'right');
			dtSwitch.css('float', 'right').css('position', 'relative').css('right', '-38px');
			
			//dtSwitch.css('display','none');
			//dtSwitch.detach().appendTo(dtSwitchParent);
			
			dtSwitchParent.append(dtSwitch);
			//dtSwitchParent.find('select:not(:first)').remove();
			
			dtSwitchParent.find('.ui-commandlink.ui-widget').css('margin', '0');
			dtControlsParent.find('.ui-state-default').css('background', 'transparent').css('border', 'none').css('border-radius', '0px').css('color', '#fefefe');
			dtControlsParent.find('.ui-state-default .ui-icon').css('text-indent', '0').css('width', 'auto').css('background-image', 'none');
			
			dtSwitchParent.find('#tempId').remove();
			dtControlsParent.find('.ui-paginator-current').detach().prependTo(dtSwitchParent).css('float', 'left');
			
			dtControlsParent.find('.ui-paginator-first').insertAfter(dtControlsParent.find('.ui-paginator-next'));
			dtControlsParent.find('.ui-paginator-first .ui-icon').html('First');
			dtControlsParent.find('.ui-paginator-last').css('border-left', '1px solid #dfeffc');
			dtControlsParent.find('.ui-paginator-last .ui-icon').html('Last');
			dtControlsParent.find('.ui-paginator-next').css('border-left', '1px solid #dfeffc').css('margin-right', '20px');
			dtControlsParent.find('.ui-paginator-next .ui-icon').html('Next &raquo;');
			dtControlsParent.find('.ui-paginator-pages').hide();
			dtControlsParent.find('.ui-paginator-prev .ui-icon').html('&laquo; Prev');
			
			$('<span class="it3m5iq" style="float: right; position: relative; right: 50px;">Items:</span>').insertBefore(dtSwitch);
			
		});
		$('#page-content').unbind();//unbind event	
		},100);
	}
});

