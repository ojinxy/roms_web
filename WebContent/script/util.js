function dateFormat(obj) {
	if (!obj.value || /^\d{4}-\d{2}-\d{2}$/.test(obj.value)) return;
	obj.value = obj.value.replace(/^(\d{4})(\d{2})(\d{1,2})$/g, "$1-$2-$3");
}

/*
 * @jreid
 * This function formats the date on the screen and then validates it 
 * 2010-56-99 - invalid
 * 2010-05-23 - valid
 */
function dateCheck(obj) {	
	obj.style.border = "1px solid black";
	if (!obj.value || /^\d{4}-\d{2}-\d{2}$/.test(obj.value)){//if date is in correct format
		//if ( /^\d{4}([1-3][0-9]{3,3})-\d{2}([1-9]|1[0-2])-\d{2}([1-9]|[1-2][0-9]|3[0-1])$/.test(obj.value)) //test if valid
		if(/^([1-3][0-9]{3,3})-(0?[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1])$/.test(obj.value))
		{return;
		}else {//if invalid alert user
			obj.style.border = "2px solid red";
			//alert("Invalid Date.");
			obj.title = "Invalid Date Entered";
		}
	}
	//date not in correct format so format
	obj.value = obj.value.replace(/^(\d{4})(\d{2})(\d{1,2})$/g, "$1-$2-$3");
}


function toUpper(obj) {
	obj.value = obj.value.toUpperCase(); 
}

function toLower(obj) {
	obj.value = obj.value.toLowerCase();
}

function isLessThan15Char(aTextField) {
	   if ((aTextField.value.length < 15) ||
	   (aTextField.value==15)) {
	      return true;
	   }
	   else { return false; }
	}	

function filterFirstCharacter(input) {
	var s = input.value;
		
		var filteredValues = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   	
		
		var c = s.charAt(0);
		if(filteredValues.indexOf(c) < 0){
			input.value = s.substring(1);
		}
}


function limitValue(obj, maxLength) {
	if( obj.value.length > maxLength )
		obj.value = obj.value.substring(0, maxLength);
}		

function limitValueByMaxLength(obj, id) {
	
	maxLength = document.getElementById(id).value;
	
	if( obj.value.length > maxLength )
		obj.value = obj.value.substring(0, maxLength);
	
}

function restrictNumericFieldsById(obj, id){
	//var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-;:";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	var dataType= document.getElementById(id).value;
	var integerString = "Integer";
	i=0;

	if(dataType == integerString){
		while(!found&&i<obj.value.length)
		{
			if (!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) {
				found = true;
			}
			i++;
		}
		
		if(found){
			for(m=0;m<i-1;m++){
				newString += obj.value.charAt(m); 
			}
			obj.value = newString;
		}
	}	
	
	return !found;
}

function restrictNumericFields(obj){
	//var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-;:";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	i=0;

	while(!found&&i<obj.value.length)
	{
		if (!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) {
			found = true;
		}
		i++;
	}
	
	if(found){
		for(m=0;m<i-1;m++){
			newString += obj.value.charAt(m); 
		}
		obj.value = newString;
	}
		
	
	return !found;
}

function restrictAlphaNumericFields(obj){
	var alphaCheck = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	i=0;

	while(!found&&i<obj.value.length)
	{
		if ((!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) && (!(alphaCheck.indexOf(obj.value.charAt(i)) >= 0))) {
			found = true;
		}
		i++;
	}
	
	if(found){
		for(m=0;m<i-1;m++){
			newString += obj.value.charAt(m); 
		}
		obj.value = newString;
	}
		
	
	return !found;
}

function restrictAlphaNumericDashFields(obj){
	var alphaCheck = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	i=0;

	while(!found&&i<obj.value.length)
	{
		if ((!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) && (!(alphaCheck.indexOf(obj.value.charAt(i)) >= 0))) {
			found = true;
		}
		i++;
	}
	
	if(found){
		for(m=0;m<i-1;m++){
			newString += obj.value.charAt(m); 
		}
		obj.value = newString;
	}
		
	
	return !found;
}

function restrictAlphaNumericDashFieldsWithSpace(obj){
	//alert();
	var alphaCheck = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ- ";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	i=0;

	while(!found&&i<obj.value.length)
	{
		if ((!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) && (!(alphaCheck.indexOf(obj.value.charAt(i)) >= 0))) {
			found = true;
		}
		i++;
	}
	
	if(found){
		for(m=0;m<i-1;m++){
			newString += obj.value.charAt(m); 
		}
		obj.value = newString;
	}
		
	
	return !found;
}

function checkPhoneNumber(obj){
	//var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-;:";
	var numericCheck = "0123456789";
	var i,m;	
	var found=false;
	var newString ="";
	i=0;
	var check=true;

	while(!found&&i<obj.value.length)
	{
		if(i==3){
			if(obj.value.charAt(i)=="-"){
				check=false;
			}
		}	
		if(check){
			if (!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) {
				found = true;
			}
		}
		check = true;
		i++;
	}

	if(found){
		for(m=0;m<i-1;m++){
			
			newString += obj.value.charAt(m);
		}
		obj.value = newString;
	}

	
	return !found;
}

function phoneMask(obj, event){
		if(obj.value!=null){
			//phone format: ###-####
			checkPhoneNumber(obj); 
			if(obj.value.indexOf('-')==-1 && obj.value.length >=3&&event.keyCode!=8){ //event.keyCode!=8 : ensures that backspace wasn't pressed
				obj.value=obj.value.substring(0,3)+"-"+obj.value.substring(4,obj.value.length);
			}else if(obj.value.indexOf('-')>3 && obj.value.length >3){
				obj.value=obj.value.replace(/-/,"");
				obj.value=obj.value.substring(0,3)+"-"+obj.value.substring(4,obj.value.length);
			}
		return true;
		}
		return false;
}

function dateValid(objName) {//alert("date valid called");
	var strDate;
	var strDateArray;
	var strDay;
	var strMonth;
	var strYear;
	var intday;
	var intMonth;
	var intYear;
	var booFound = false;
	var datefield = objName;
	var strSeparatorArray = new Array("-"," ","/",".");
	var intElementNr;
	var i;
	// var err = 0;
	var strMonthArray = new Array(12);
	strMonthArray[0] = "Jan";
	strMonthArray[1] = "Feb";
	strMonthArray[2] = "Mar";
	strMonthArray[3] = "Apr";
	strMonthArray[4] = "May";
	strMonthArray[5] = "Jun";
	strMonthArray[6] = "Jul";
	strMonthArray[7] = "Aug";
	strMonthArray[8] = "Sep";
	strMonthArray[9] = "Oct";
	strMonthArray[10] = "Nov";
	strMonthArray[11] = "Dec";
	//strDate = datefield.value;
	strDate = objName;
	if (strDate.length < 1) {
	return true;
	}
	for (intElementNr = 0; intElementNr < strSeparatorArray.length; intElementNr++) {
	if (strDate.indexOf(strSeparatorArray[intElementNr]) != -1) {
	strDateArray = strDate.split(strSeparatorArray[intElementNr]);
	if (strDateArray.length != 3) {
	err = 1;
	return false;
	}
	else {
	strDay = strDateArray[0];
	strMonth = strDateArray[1];
	strYear = strDateArray[2];
	}
	booFound = true;
	   }
	}
	if (booFound == false) {
	if (strDate.length>5) {
	strDay = strDate.substr(0, 2);
	strMonth = strDate.substr(2, 2);
	strYear = strDate.substr(4);
	   }
	}
	//Adjustment for short years entered
	if (strYear.length == 2) {
	strYear = '20' + strYear;
	}
	strTemp = strDay;
	strDay = strMonth;
	strMonth = strTemp;
	intday = parseInt(strDay, 10);
	if (isNaN(intday)) {
	err = 2;
	return false;
	}
	intMonth = parseInt(strMonth, 10);
	if (isNaN(intMonth)) {
	for (i = 0;i<12;i++) {
	if (strMonth.toUpperCase() == strMonthArray[i].toUpperCase()) {
	intMonth = i+1;
	strMonth = strMonthArray[i];
	i = 12;
	   }
	}
	if (isNaN(intMonth)) {
	err = 3;
	return false;
	   }
	}
	intYear = parseInt(strYear, 10);
	if (isNaN(intYear)) {
	err = 4;
	return false;
	}
	if (intMonth>12 || intMonth<1) {
	err = 5;
	return false;
	}
	if ((intMonth == 1 || intMonth == 3 || intMonth == 5 || intMonth == 7 || intMonth == 8 || intMonth == 10 || intMonth == 12) && (intday > 31 || intday < 1)) {
	err = 6;
	return false;
	}
	if ((intMonth == 4 || intMonth == 6 || intMonth == 9 || intMonth == 11) && (intday > 30 || intday < 1)) {
	err = 7;
	return false;
	}
	if (intMonth == 2) {
	if (intday < 1) {
	err = 8;
	return false;
	}
	if (LeapYear(intYear) == true) {
	if (intday > 29) {
	err = 9;
	return false;
	   }
	}
	else {
	if (intday > 28) {
	err = 10;
	return false;
	      }
	   }
	}
	return true;
	}
	function LeapYear(intYear) {
	if (intYear % 100 == 0) {
	if (intYear % 400 == 0) { return true; }
	}
	else {
	if ((intYear % 4) == 0) { return true; }
	}
	return false;
	}
	//  End -->
	/**
	 * DHTML email validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
	 */

	function echeck(str) {

			var at="@";
			var dot=".";
			var lat=str.indexOf(at);
			var lstr=str.length;
			var ldot=str.indexOf(dot);
			if (str.indexOf(at)==-1){
			 
			   return false;
			}

			if (str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr){
			   
			   return false;
			}

			if (str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr){
			    
			    return false;
			}

			 if (str.indexOf(at,(lat+1))!=-1){
			    
			    return false;
			 }

			 if (str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot){
			    
			    return false;
			 }

			 if (str.indexOf(dot,(lat+2))==-1){
			   
			    return false;
			 }
			
			 if (str.indexOf(" ")!=-1){
			   
			    return false;
			 }

	 		 return true					
	}

	function validateEmail(obj){
		
		if(echeck(obj.value)==false){
			obj.style.border = "2px solid red";
			obj.title = "Invalid Email Format Entered";
		}
		else{
			obj.style.border = null;	
			obj.title = null;
		}
	}

	
	function restrictAmountFields(obj){
		//var alphaCheck = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-;:";
		
		var numericCheck = "0123456789.";
		var i,m;
		var found=false;
		var newString ="";
		i=0;
		while(!found&&i<obj.value.length)
		{
			if (!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) {
				found = true;
			}
			i++;
		}
		if(found){
			for(m=0;m<i-1;m++){
				newString += obj.value.charAt(m);
			}
			obj.value = newString;
		}
		return !found;
	}

	function restrictCurrencyFields(obj){
		var numericCheck = "0123456789.,";
		var i,m;
		var found=false;
		var newString ="";
		i=0;
		while(!found&&i<obj.value.length)
		{
			if (!(numericCheck.indexOf(obj.value.charAt(i)) >= 0)) {
				found = true;
			}
			i++;
		}
		if(found){ 
			if(i==1){
				obj.value = "";
			}
			else{
				for(m=0;m<i-1;m++){
					newString += obj.value.charAt(m);
				}
				obj.value = newString;
			}
		}
		
		return !found;
	}
	
	function getComponentId(fullId){
		var stringArray = fullId.split(':');
		var arrayLength = stringArray.length;
		
		var id =  stringArray[arrayLength-1];
		
		return id; 
	}

	//rennis
	function removeSpecialChars(obj) {
		obj.value = obj.value.replace(/[^a-zA-Z 0-9]+/g, '');
	}

	//rennis
	function removeSQLOffenders(obj) {
		
		obj.value = obj.value.replace(';', '');
		obj.value = obj.value.replace("'", '');
		
	}
	
	/*function openNewWindow(url)
	{
		var leftPos = 10;	
		var topPos = 10;
		
		window.open(url,'_blank','location=0,resizable=yes,toolbar=no,scrollbars=1,width=960,height=640,top='+topPos+',left='+leftPos+'');
	}*/
	
	function openNewWindow(url)
	{
		var leftPos = 10;	
		var topPos = 10;
		
		var generator = window.open(null,'_blank','location=0,resizable=yes,toolbar=no,scrollbars=1,width=960,height=640,top='+topPos+',left='+leftPos+'', true);
		 
		generator.document.write('<div style="background: white; width: 220px; height: 40px; position: absolute; top: 50%; left: 50%; ">	<p><img id="bar" src="../../layouts/images/ajaxloader.gif" style="position: absolute; bottom: 10;" /> </p>	</div>');
		
		generator.location.replace('../../view/pdf/?buffered=true');
	}
	

	function openNewWindowWithName(url)
	{
		var leftPos = 10;	
		var topPos = 10;
		
		my_win = window.open(url,'_blank','location=0,resizable=yes,toolbar=no,scrollbars=1,width=960,height=640,top='+topPos+',left='+leftPos+'');
		
		return my_win;
	}
	
	
	function checkWinStillOpen(myWindow)
	{
	if (!myWindow)
	  {
	     // document.getElementById("documentManagerView:errorMessages").innerHTML="'myWindow' has never been opened!";
	  }
	else
	  {
	  if (myWindow.closed)
	    { 
		  //document.getElementById("documentManagerView:errorMessages").innerHTML="'myWindow' has been closed!";
		  document.getElementById("editCaseNum").click();
	    }
	  else
	    {
		  //document.getElementById("documentManagerView:errorMessages").innerHTML="'myWindow' has not been closed!";
		  document.getElementById("editCaseNum").click();
	    }
	  }	
	}
	
	function validateDates(){		
		var n = 0;
		var valid = true;
		var datePickers = $('.datepicker');
		$.each(datePickers, function (i){
			if(!DateFormat(this,this.value,null,true)){			
				valid = false;
				n++;
			}
		});		
		return valid;
	}

	function validateDates(datePickerClass){		
		var n = 0;
		var valid = true;
		var datePickers = $(datePickerClass);
		if(datePickers!=null){
			$.each(datePickers, function (i){
				
					if(this.value!=null){
						if(!DateFormat(this,this.value,null,true)){						
							valid = false;
							n++;					
						}
					}				
			});
		}
		return valid;
	}
	
	function DateFormat(vDateName, vDateValue, e, dateCheck) {
		// Check browser version
		var isNav4 = false, isNav5 = false, isIE4 = false;
		var strSeperator = "-";
		// If you are using any Java validation on the back side you will want to use the / because
		// Java date validations do not recognize the dash as a valid date separator.
		var vDateType = 3; // Global value for type of date format
//		                1 = mm/dd/yyyy
//		                2 = yyyy/dd/mm  (Unable to do date check at this time)
//		                3 = dd/mm/yyyy
		var vYearType = 4; //Set to 2 or 4 for number of digits in the year for Netscape
		var vYearLength = 2; // Set to 4 if you want to force the user to enter 4 digits for the year before validating.
		var err = 0; // Set the error code to a default of zero
		if(navigator.appName == "Netscape") {
		if (navigator.appVersion < "5") {
		isNav4 = true;
		isNav5 = false;
		}
		else
		if (navigator.appVersion > "4") {
		isNav4 = false;
		isNav5 = true;
		   }
		}
		else {
		isIE4 = true;
		}
		

		
	vDateType = 2;


	// vDateName = object name
	// vDateValue = value in the field being checked
	// e = event
	// dateCheck
	// True  = Verify that the vDateValue is a valid date
	// False = Format values being entered into vDateValue only
	// vDateType
	// 1 = mm/dd/yyyy
	// 2 = yyyy/mm/dd
	// 3 = dd/mm/yyyy
	//Enter a tilde sign for the first number and you can check the variable information.
	if (vDateValue == "~") {
	alert("AppVersion = "+navigator.appVersion+" \nNav. 4 Version = "+isNav4+" \nNav. 5 Version = "+isNav5+" \nIE Version = "+isIE4+" \nYear Type = "+vYearType+" \nDate Type = "+vDateType+" \nSeparator = "+strSeperator);
	vDateName.value = "";
	vDateName.focus();
	return true;
	}
	var whichCode;
	if(e==null)
		whichCode = undefined;
	else
		whichCode = (window.Event) ? e.which : e.keyCode;
	// Check to see if a seperator is already present.
	// bypass the date if a seperator is present and the length greater than 8

	//if(vDateValue.length==10 ){
		if(dateCheck){
			
			if(vDateType==2){//type 2 check
				if(vDateName.value != "" && vDateName.value !=undefined){
				mYear = vDateName.value.substr(0,4);
				mMonth = vDateName.value.substr(5,2);
				mDay = vDateName.value.substr(8,2);
				}
				else{
					vDateName.style.border = "1px solid #8ba0bd";;
					vDateName.title = "";
					dijit.hideTooltip(vDateName);
					
					return true;
					//return false;	
				}
					//	vDateType = 1;
			vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
			if(vDateName.value != "" && vDateName.value!=null && vDateName.value!=undefined){
			if (!dateValid(vDateValueCheck)) {
				//alert("not valid");
				//vDateName.value = "";
				vDateName.focus();
				vDateName.select();
				//vDateName.style.backgroundColor = "yellow";
				//vDateName.className = "invalidField";
				vDateName.style.border = "2px solid red";
				vDateName.title = "Invalid Date Entered";
				//vDateName.style.backgroundImage = "none";
				//var dojoe = dojo.byId(vDateName.id);
				//dijit.showTooltip("Invalid Date Entered", dojoe.domNode);
				alert("* The date entered is not valid.", vDateName);
				
				return false;
				}
			else {
				//vDateName.style.border = "none";
				//$(vDateName).removeClass("invalidField");
				vDateName.style.border = "1px solid #8ba0bd";;
				vDateName.title = "";
				dijit.hideTooltip(vDateName);
				
				return true;
				}
			}
			else {
				vDateName.style.border = "1px solid #8ba0bd";;
				vDateName.title = "";
				dijit.hideTooltip(vDateName);
				
				return true;
			
				}
			}
		}
		//return false;
	//}

	if(whichCode==undefined){
		return false;
	}	

	if (vDateValue.length > 8 && isNav4) {
	if ((vDateValue.indexOf("-") >= 1) || (vDateValue.indexOf("/") >= 1))
	return true;
	}
	//Eliminate all the ASCII codes that are not valid

	var mDay ;
	var mMonth ;
	var mYear ;
	var vDateValueCheck;
	var alphaCheck = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/-";
	if (alphaCheck.indexOf(vDateValue) >= 1) {
	if (isNav4) {
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	}
	else {
	vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
	return false;
	   }
	}
	if (whichCode == 8) //Ignore the Netscape value for backspace. IE has no value
	return false;
	else {
	//Create numeric string values for 0123456789/
	//The codes provided include both keyboard and keypad values
	var otherCheck = '91,92,93,112,113,114,115,116,117,118,119,120,121,122,123,144,145';
	if (otherCheck.indexOf(whichCode) != -1 || whichCode<=46) {
		return false;
	}
	if (vDateValue.length == 5 && whichCode==109){
		return false;
	}
	if (vDateValue.length == 8 && whichCode==109){
		return false;
	}
	var strCheck = '47,48,49,50,51,52,53,54,55,56,57,58,59,95,96,97,98,99,100,101,102,103,104,105';
	if (strCheck.indexOf(whichCode) != -1) {
	if (isNav4) {
	if (((vDateValue.length < 6 && dateCheck) || (vDateValue.length == 7 && dateCheck)) && (vDateValue.length >=1)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	}
	if (vDateValue.length == 6 && dateCheck) {
	mDay = vDateName.value.substr(2,2);
	mMonth = vDateName.value.substr(0,2);
	mYear = vDateName.value.substr(4,4);
	//Turn a two digit year into a 4 digit year
	if (mYear.length == 2 && vYearType == 4) {
	var mToday = new Date();
	//If the year is greater than 30 years from now use 19, otherwise use 20
	var checkYear = mToday.getFullYear() + 30;
	var mCheckYear = '20' + mYear;
	if (mCheckYear >= checkYear)
	mYear = '19' + mYear;
	else
	mYear = '20' + mYear;
	}
	vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
	if (!dateValid(vDateValueCheck)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	}
	return true;
	}
	else {
	// Reformat the date for validation and set date type to a 1
	if (vDateValue.length >= 8  && dateCheck) {
	if (vDateType == 1) // mmddyyyy
	{
	mDay = vDateName.value.substr(2,2);
	mMonth = vDateName.value.substr(0,2);
	mYear = vDateName.value.substr(4,4);
	vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
	}
	if (vDateType == 2) // yyyymmdd
	{ alert("right here type 2");
	mYear = vDateName.value.substr(0,4);
	mMonth = vDateName.value.substr(4,2);
	mDay = vDateName.value.substr(6,2);
	vDateName.value = mYear+strSeperator+mMonth+strSeperator+mDay;
	}
	if (vDateType == 3) // ddmmyyyy
	{
	mMonth = vDateName.value.substr(2,2);
	mDay = vDateName.value.substr(0,2);
	mYear = vDateName.value.substr(4,4);
	vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;
	}
	//Create a temporary variable for storing the DateType and change
	//the DateType to a 1 for validation.
	var vDateTypeTemp = vDateType;
	vDateType = 1;
	vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
	alert("right here");
	if (!dateValid(vDateValueCheck)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateType = vDateTypeTemp;
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	}
	vDateType = vDateTypeTemp;
	return true;
	}
	else {
	if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	         }
	      }
	   }
	}
	else {// alert("non nav check");
	// Non isNav Check
	if (((vDateValue.length < 8 && dateCheck) || (vDateValue.length == 9 && dateCheck)) && (vDateValue.length >=1)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	return true;
	}
	// Reformat date to format that can be validated. mm/dd/yyyy
	if (vDateValue.length >= 8 && dateCheck) {
	// Additional date formats can be entered here and parsed out to
	// a valid date format that the validation routine will recognize.
	if (vDateType == 1) // mm/dd/yyyy
	{
	mMonth = vDateName.value.substr(0,2);
	mDay = vDateName.value.substr(3,2);
	mYear = vDateName.value.substr(6,4);
	}
	if (vDateType == 2) // yyyy/mm/dd
	{
	mYear = vDateName.value.substr(0,4);
	mMonth = vDateName.value.substr(5,2);
	mDay = vDateName.value.substr(8,2);
	}
	if (vDateType == 3) // dd/mm/yyyy
	{
	mDay = vDateName.value.substr(0,2);
	mMonth = vDateName.value.substr(3,2);
	mYear = vDateName.value.substr(6,4);
	}
	if (vYearLength == 4) {
	if (mYear.length < 4) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.value = "";
	vDateName.focus();
	return true;
	   }
	}
	// Create temp. variable for storing the current vDateType
	var vDateTypeTemp1 = vDateType;
	// Change vDateType to a 1 for standard date format for validation
	//Type will be changed back when validation is completed.
	vDateType = 1;
	// Store reformatted date to new variable for validation.
	vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
	if (mYear.length == 2 && vYearType == 4 && dateCheck) {
	//Turn a two digit year into a 4 digit year
	var mToday1 = new Date();
	//If the year is greater than 30 years from now use 19, otherwise use 20
	var checkYear1 = mToday1.getFullYear() + 30;
	var mCheckYear1 = '20' + mYear;
	if (mCheckYear1 >= checkYear1)
	mYear = '19' + mYear;
	else
	mYear = '20' + mYear;
	vDateValueCheck = mMonth+strSeperator+mDay+strSeperator+mYear;
	// Store the new value back to the field.  This function will
	// not work with date type of 2 since the year is entered first.
	if (vDateTypeTemp1 == 1) // mm/dd/yyyy
	vDateName.value = mMonth+strSeperator+mDay+strSeperator+mYear;
	if (vDateTypeTemp1 == 3) // dd/mm/yyyy
	vDateName.value = mDay+strSeperator+mMonth+strSeperator+mYear;
	}
	alert("before date check");
	if (!dateValid(vDateValueCheck)) {
	alert("Invalid Date\nPlease Re-Enter");
	vDateType = vDateTypeTemp1;
	vDateName.value = "";
	vDateName.focus();
	return true;
	}
	vDateType = vDateTypeTemp1;
	return true;
	}
	else {
	if (vDateType == 1) {
	if (vDateValue.length == 2) {
	vDateName.value = vDateValue+strSeperator;
	}
	if (vDateValue.length == 5) {
	vDateName.value = vDateValue+strSeperator;
	   }
	}
	if (vDateType == 2) {
	if (vDateValue.length == 4) {
		vDateName.value = vDateValue+strSeperator;
	}

	if (vDateValue.length == 5 && whichCode!=109) {

		var last5Digit = vDateName.value.substr(vDateValue.length-1, (vDateValue.length-1));
		if(vDateName.value.substr(vDateValue.length-1, (vDateValue.length-1))!='-'){
			vDateValue = vDateValue.substr(0, (vDateValue.length-1));
			vDateName.value = vDateValue+strSeperator+last5Digit;
		}
	}

	if (vDateValue.length == 7) {
		vDateName.value = vDateValue+strSeperator;
	}

	if (vDateValue.length == 8 && whichCode!=109) {
		var last7Digit = vDateName.value.substr(vDateValue.length-1, (vDateValue.length-1));
		if(vDateName.value.substr(vDateValue.length-1, (vDateValue.length-1))!='-'){
			vDateValue = vDateValue.substr(0, (vDateValue.length-1));
			vDateName.value = vDateValue+strSeperator+last7Digit;
		}
	}

	}
	if (vDateType == 3) {
	if (vDateValue.length == 2) {
	vDateName.value = vDateValue+strSeperator;
	}
	if (vDateValue.length == 5) {
	vDateName.value = vDateValue+strSeperator;
	   }
	}
	return true;
	   }
	}
	if (vDateValue.length == 10&& dateCheck) {
	if (!dateValid(vDateName)) {
	// Un-comment the next line of code for debugging the dateValid() function error messages
	//alert(err);
	alert("Invalid Date\nPlease Re-Enter");
	vDateName.focus();
	vDateName.select();
	   }
	}
	return false;
	}
	else {
	// If the value is not in the string return the string minus the last
	// key entered.
	if (isNav4) {
	vDateName.value = "";
	vDateName.focus();
	vDateName.select();
	return false;
	}
	else
	{
	vDateName.value = vDateName.value.substr(0, (vDateValue.length-1));
	return false;
	         }
	      }
	   }
	}
	
	
	
	
	function disableMultipleSubmit(id)
	{
		document.getElementById(id).disabled=true;
		
	}
	
	
	function getRandomizer(bottom, top) {
		return Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
		
	};

	function getRandNumbers(size)
	{
		var numbers="";
		var min_number = 0;
		var max_number = 9
		try
		{
		
			for( i=0 ; i < size; i++)
			{
				var rand = getRandomizer(min_number, max_number);
				numbers += rand.toString() ;
			
			}
		
		}catch(e){
			alert(e);
		}
		
		return numbers;
	
	};

	/*Disable the button and continue to process the post back request.
	 * The button will be reset to its original state when the post back is 
	 * complete.
	 * Written By: OAnguin
	 * Date: 29 April 2016
	 * Modified By: OAnguin
	 * Modified: 29 April 2016*/
	function disableButton(button){
		$(button).addClass('ui-state-disabled');
		button.addEventListener('click', clickStopper, false);
	}
	
	function clickStopper(e)
	{
	   e.preventDefault();
	}
	
	(function($) {
	
		 $.fn.Default_RO_Name = function( options ) {
		 
			//default settings
			var settings = $.extend({
				regionCode	:"kro", 
				category	:"Regional"
			}, options);
			
			var size_of_numbers = 4;
			return this.each( function() {
				try{
					var default_name = settings.regionCode.toUpperCase().trim()+"-"+settings.category.toUpperCase().trim()+"-"+getRandNumbers(size_of_numbers);
					$(this).text(default_name).val(default_name);
				}catch(e)
				{
					alert(e);
				}
			});

		};
		 
		
	}(jQuery));

	
	
	/*
	function trim(str, chars) {
		return ltrim(rtrim(str, chars), chars);
	}
	 
	function ltrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
	}
	 
	function rtrim(str, chars) {
		chars = chars || "\\s";
		return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
	}
	
*/