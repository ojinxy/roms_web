				


function teamLeadChanged()
{
	var str = "";
	
	$.each($('label[id*=teamLeadList_label]'), 
			function(key,value){
				
			
				str = ($(value).text());
			});

	//alert(str);
	$('input[id*="jpTabTeamLead"]').val(str);
}

function handleStaffTransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=taStaffpickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
			
			
			
			
			$('input[id*="listOfSelectedStaffID"]').val(newString);
			
			$('button[id*=buttHandleTransfer]').click();
			
		}
		

	function handleStaffTransferAux(e)
	{
		var str = "";
		
		$.each(($('table[id*=taStaffpickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
				function(key,value){
					
				
					str = str+ ($(value).attr('data-item-value')) + ",";
				});
		
		var newString = str.substr(0, str.length-1); 
		
		
		$('input[id*="listOfSelectedStaffID"]').val(newString);
		//alert(newString);
	}
		
		function handleVehTransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=vehpickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
		
			
			
			$('input[id*="listOfSelectedVehID"]').val(newString);
			
			
		}
		
		
	
		function handleJPTransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=jppickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
			//console.log(str);
		
			//alert("newString" +newString+" "+ newString.length+"XXX");
			if(newString.length>0){
				//alert("inside call buttHandleJPTransfer");
			$('input[id*="listOfSelectedJPID"]').val(newString);
			$('button[id*=buttHandleJPTransfer]').click();
			}else{
				$('input[id*="listOfSelectedJPID"]').val('');
				
			}
			
			
		}
		
		
		function handleITATransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=itapickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
			
					
			$('input[id*="listOfSelectedITAID"]').val(newString);
			
			
		}
		
		
		function handlePoliceTransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=policepickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
		
			
			
			$('input[id*="listOfSelectedPoliceID"]').val(newString);
			
			
		}
		
		
		
		function handleStrategyTransfer(e)
		{
			var str = "";
			
			$.each(($('table[id*=strategyPickList]').find($('ul[class*=ui-picklist-target]'))).find('li[class*=ui-picklist-item]'), 
					function(key,value){
						
					
						str = str+ ($(value).attr('data-item-value')) + ",";
					});
			
			var newString = str.substr(0, str.length-1); 
		
			
			
			$('input[id*="listOfSelectedStrategyID"]').val(newString);
			
			$('button[id*=buttHandleStratTransfer]').click();
			
		}
		
		
		function validate()
		{
			var val = $(closeTeamPopup).attr('jq').val().toLowerCase().trim();
			
			return val;
		}
		
		
		function checkIfAddDialogShouldClose()
		{
			var shouldYouCloseDialog = $(closeTeamPopup).attr('jq').val().toLowerCase().trim();
			
			if(shouldYouCloseDialog == 'y')
			{
				 PF('teamDialog').hide();
			}
			
			theWidget.enable();
		}
		
		
		
//		$('#resourceDetails\\:addOneTeam').click(function()
//			{
		function ensureAllValuesReturned()
		{
			
				handleStaffTransferAux();
				handleVehTransfer();
				handleJPTransfer();
				handlePoliceTransfer();
				handleITATransfer();
		}
//		);
		
		
		function handleStrategyTransferAux()
		{
			
			handleStrategyTransfer();
			
		}
		