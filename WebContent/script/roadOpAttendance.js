$(document).ready(function()
{	
	
	//allPaginatorTemplates();
});


function findDuration()
{
	var spanDurText = $("span[id*='spanDurationText']");
	
	spanDurText.empty();
	
	var actualStartDate = calActualStartWidgetId.getDate();
	
	var actualEndDate = calActualEndWidgetId.getDate();
	
	if(actualStartDate != null && actualEndDate != null && actualStartDate < actualEndDate )
	{
				var totalHours = (actualEndDate - actualStartDate) / 3.6e6 | 0;
				
				var days = Math.floor(totalHours / 24);
				
				var hours =  totalHours % 24;
				
				spanDurText.append("(" + days + ")" +  " days " + "(" + hours + ")" + " hours");		
			
	}	
	
	
	
}

function checkAttendedState(item)
{
	
	
	var parentSelectOne = $(item).parent().parent().parent().children()[0];
	
	var childOne = $(parentSelectOne).children()[0];
	
	var childTwo = $(parentSelectOne).children()[1];
	
	var selectedItem = $(parentSelectOne).find("div[class*='ui-state-active']")[0];
	
	if(selectedItem.textContent.toLowerCase().indexOf('no') != -1)
	{
		$(selectedItem).children().css("background-color","red");
		$(selectedItem).children().css("color","white");
	}
	else
	{
		$(selectedItem).children().css("background-color","green");
		$(selectedItem).children().css("color","white");
	}
	
	if(selectedItem.textContent.toLowerCase().indexOf(childOne.textContent.toLowerCase()) != -1)
	{
		$(childTwo).children().css("background-color","");
		$(childTwo).children().css("color","black");
	}
	else
	{
		$(childOne).children().css("background-color","");
		$(childOne).children().css("color","black");
	}
	
	$("div[class*='ui-state-active']").find(':contains("NO")').parent().children().css("border-radius","inherit");
	$("div[class*='ui-state-active']").find(':contains("YES")').parent().children().css("border-radius","inherit");
}

function setAttendedStateColors()
{
	
	$("div[class*='ui-state-active']").find(':contains("NO")').parent().children().css("background-color","red");
	$("div[class*='ui-state-active']").find(':contains("NO")').parent().children().css("color","white");
	$("div[class*='ui-state-active']").find(':contains("YES")').parent().children().css("background-color","green");
	$("div[class*='ui-state-active']").find(':contains("YES")').parent().children().css("color","white");
					
	$("div[class*='ui-state-active']").find(':contains("NO")').parent().children().css("border-radius","inherit");
	$("div[class*='ui-state-active']").find(':contains("YES")').parent().children().css("border-radius","inherit");
	
	allPaginatorTemplates();
	
}

function closePopUpUsingStatus()
{
		
		if(txtIsMobile.jq[0].defaultValue == 'true')
		{
			mobileCalendar();
		}
		
	
		if(txtCloseStatus.jq[0].defaultValue == 'close')
		{
			$('button[id*=butRoadOpStartedSuccessfully]').trigger('click');
		}
}


function paginatorTeamTable()
{
	if($('div[id$=teamResults]') != null)
		$('div[id$=teamResults]').forceDatatablePagin();
	
	
}


function paginatorTeamTAStaff()
{
	if($('div[id$=teamTAStaff]') != null)
		$('div[id$=teamTAStaff]').forceDatatablePagin();
}

function paginatorTeamITAExaminer()
{
	if($('div[id$=teamITAExaminer]') != null)
		$('div[id$=teamITAExaminer]').forceDatatablePagin();
}

function paginatorTeamPolice()
{
	if($('div[id$=teamPolice]') != null)
		$('div[id$=teamPolice]').forceDatatablePagin();
}

function paginatorTeamJP()
{
	if($('div[id$=teamJP]') != null)
		$('div[id$=teamJP]').forceDatatablePagin();
}

function paginatorTeamVehicle()
{
	if($('div[id$=teamVehicle]') != null)
		$('div[id$=teamVehicle]').forceDatatablePagin();
}

function allPaginatorTemplates()
{
	$('.ui-paginator-prev').each(function(){
		if($(this).text().indexOf('Prev') < 0)
			{
			$(this).css('cssText','color:white !important;display:inline-flex;border-style:none;background:none;');
				$(this).append('Prev');
			}
	});
	
	$('.ui-paginator-next').each(function(){
		if($(this).text().indexOf('Next') < 0)
			{
				$(this).css('cssText','color:white !important;display:inline-flex;border-style:none;background:none;');
				$(this).append('Next');
			}
	});
	
	$('.ui-paginator-first').each(function(){
		if($(this).text().indexOf('First') < 0)
			{
				$(this).css('cssText','color:white !important;display:inline-flex;border-style:none;background:none;');
				$(this).append('First');
			}
	});
	
	
	$('.ui-paginator-last').each(function(){
		if($(this).text().indexOf('Last') < 0)
			{
				$(this).css('cssText','color:white !important;display:inline-flex;border-style:none;background:none;');
				$(this).append('Last');
			}
	});
	
}


function mobileCalendar()
{
	 $('input[id*=calActual]').scroller({
	      preset: 'datetime',
	      theme: 'android-ics light',
	      display: 'bottom',
	      mode: 'mixed',
         dateFormat: 'yy-mm-dd',
			  timeFormat: 'h:ii a'
	    }); 
}