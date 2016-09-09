/*JavaScript functions to be used with Review Road Check Pages*/

$(document).ready(function(){	
	

	paginatorForDocTable();
	
	paginatorForOffencesObservedTable();
	
	paginatorForWitnessTable();
	
	hideDocSelections();
	
	paginatorForAssociatedDocTable();
	
	paginatorForDocsDataGrid();
	
	if($('input[id*=txtTRN]') != null)
		$('input[id*=txtTRN]').mask('999999999',{placeholder:""});
	
	$('a[id*=butEditSupportDetails]').click(function(e) {
        e.stopPropagation();
   });
	
	$('a[id*=butEditSupportDetails]').prependTo($('h3:contains("Supporting Details")'));
	
	$('a[id*=butEditOffencesDetails]').click(function(e) {
        e.stopPropagation();
   });
	
	$('a[id*=butEditOffencesDetailsForAssDocs]').prependTo($('h3:contains("Associated Documents")'));
	
	$('a[id*=butEditOffencesDetailsForOffObs]').prependTo($('h3:contains("Outcomes Observed")'));
	
	
});

function checkViewDetailsPrint()
{
	if($(txtAllSelectedSummonsSigned).attr('jq').val() != null)
	{
		var allDocsSignedByJP = $(txtAllSelectedSummonsSigned).attr('jq').val().toLowerCase().trim();
		
		if(allDocsSignedByJP == 'true')
		{
			$('button[id*=butPrintViewDetails]').click();
			
		}
		else
		{
			//Show the attach JP to document.
			PF('dlgVerifyPrint').show();
		}
	
	}
}

function checkIfDocumentsShouldBePrinted()
{
	if($(txtAllSelectedSummonsSigned).attr('jq').val() != null)
	{
		var allDocsSignedByJP = $(txtAllSelectedSummonsSigned).attr('jq').val().toLowerCase().trim();
		
		if(allDocsSignedByJP == 'true')
		{
			$('button[id*=butPrintViewDetails]').click();
			
		}
		else
		{
			//Show the attach JP to document.
			PF('dlgVerifyPrint').show();
		}
	
	}
}

function expandAllAccordTabs()
{
	var expandAllTabs = $(inputExpandAllAccordTabsWidget).attr('jq').val().toLowerCase().trim();
	
	if(expandAllTabs == 'true')
	{
		accordOffencesWidget.unselect(0);
		accordOffencesWidget.unselect(1);
		accordOffencesWidget.unselect(2);
	}
	else
	{
		accordOffencesWidget.select(0);
		accordOffencesWidget.select(1);
		accordOffencesWidget.select(2);
	
	}
}

function printFromPrepareAndPrintScreen()
{
	
	PF('dlgVerifyPrint').hide();
	
//	myWin =  openNewWindowWithName($(txtAppPathWidget).attr('jq').val() + "/GenerateDocs");
	
	myWin =  openNewWindowWithName($(txtAppPathWidget).attr('jq').val() + "/ROMS_Documents");
	
}


function showDocuments()
{
//	myWin =  openNewWindowWithName($(txtAppPathWidget).attr('jq').val() + "/GenerateDocs");
	
	myWin =  openNewWindowWithName($(txtAppPathWidget).attr('jq').val() + "/ROMS_Documents");
    
    //checkWinStillOpen(myWin);
}

function checkPrinting()
{
	if($(txtShouldYouPrint).attr('jq').val() != null)
	{
		var shouldYouPrint = $(txtShouldYouPrint).attr('jq').val().toLowerCase().trim();
		
		var isMobile = $(txtIsMobile).attr('jq').val().toLowerCase().trim();
		
		if(shouldYouPrint == 'true' || isMobile == 'false')
		{
			$('button[id*=butPrintDocuments]').trigger('click');
			
		}
	
	}
}

function hideDocSelections()
{
	$('.tableAssociatedDocsStyle').find('tr').find('#docSelections').css('visibility','hidden');
	
	//Show Upload Document Icon on Mouse Over
	$('.tableAssociatedDocsStyle').find('tr').mouseover(function(){$(this).find('#docSelections').css('visibility','');});
	
	//Hide Upload Document Icon on Mouse Leave
	$('.tableAssociatedDocsStyle').find('tr').mouseleave(function(){$(this).find('#docSelections').css('visibility','hidden');});
	
	paginatorForAssociatedDocTable();
	
}

function hideUploadButtons()
{
	$('button[class*=ui-fileupload-upload]').css('visibility','hidden');
	
	$('button[class*=ui-fileupload-cancel]').css('visibility','hidden');
}


function uploadDoc()
{
	if($(txtShouldYouUpload).attr('jq').val() != null)
	{
		var shouldYouUpload = $(txtShouldYouUpload).attr('jq').val().toLowerCase().trim();
		
		if(shouldYouUpload == 'true')
		{
			$('button[class*=ui-fileupload-upload]').click();
			
			
		}
	
	}
	
}

function verifyJP_old()
{
	if($(txtShouldYouPrint).attr('jq').val() != null)
	{
		var shouldYouPrint = $(txtShouldYouPrint).attr('jq').val().toLowerCase().trim();
		
		if(shouldYouPrint == 'true')
		{
			
			
			$('button[id*=butPrintDocuments]').click();
			
		}
	
	}
	
}

function verifyJP()
{
	if($(txtShouldYouPrint).attr('jq').val() != null)
	{
		var shouldYouPrint = $(txtShouldYouPrint).attr('jq').val().toLowerCase().trim();
		
		if(shouldYouPrint == 'true')
		{
			 showDocuments();
			 
			$('button[id*=butFinPrint]').click();
			
		}
	
	}
	
}

function startCompleteRoadCheck()
{
	//butBack.disable();
	//$('button[id*=butBack]').attr('disabled', 'disabled');
	$('button[id*=butBack]').css('disabled','true');
	
	//butCompleteRoadCheck.disable();
	//$('button[id*=butCompleteRoadCheck]').attr('disabled', 'disabled');
	$('button[id*=butCompleteRoadCheck]').css('disabled','true');
		
}

function errorCompleteRoadCheck()
{
	butBack.enable();
	
	
	butCompleteRoadCheck.enable();
	
}

function completeRoadCheck()
{
	var shouldGoToPrepareAndPrint =  $('input[id*=shouldGoToPrepareAndPrint]').val().toLowerCase().trim();
	
	if(shouldGoToPrepareAndPrint == "true")
	{
		$('button[id*=butPrepareAndPrint]').click();
	}
	else
	{
		errorCompleteRoadCheck();
	}
}

function tableScrollLook()
{
	if( $(".tblroadCheckClass") != null)
	{
	  $(".tblroadCheckClass").delegate('tr','mouseover mouseleave', function(e) {
	        if (e.type == 'mouseover') {
	          $(this).addClass("tblRoadOpHover");
	          
	        }
	        else {
	          $(this).removeClass("tblRoadOpHover");
	          
	        }
	    });
	  
	  butSearch.enable();
	}
	    
	    
	    
	  
}


function paginatorForDocTable()
{
	if($('#roadCheckReview\\:accordOffences\\:tableDocuments') != null)
		$('#roadCheckReview\\:accordOffences\\:tableDocuments').forceDatatablePagin();
	
}

function paginatorForAssociatedDocTable()
{
	if($('#roadCheckReview\\:accordOffences\\:tableAssociatedDocs') != null)
		$('#roadCheckReview\\:accordOffences\\:tableAssociatedDocs').forceDatatablePagin();
	
}


function paginatorForOffencesObservedTable()
{
	if($('#roadCheckReview\\:accordOffences\\:tableOffenceOutcomes') != null)
		$('#roadCheckReview\\:accordOffences\\:tableOffenceOutcomes').forceDatatablePagin();
	
}

function paginatorForWitnessTable()
{
	if($('#roadCheckReview\\:accordOffences\\:tableWitnesses') != null)
		$('#roadCheckReview\\:accordOffences\\:tableWitnesses').forceDatatablePagin();
	
}


function paginatorForDocsDataGrid()
{
	if($('#formPrepareAndPrint\\:dataGridAssociatedDocs') != null)
		$('#formPrepareAndPrint\\:dataGridAssociatedDocs').forceDataGridPagin();
	
}
