function loadDemographicData(selectElement){
	
	var cValue = $(selectElement).val()
	switch(cValue){
		case "0":
		$("#bivag").hide();
		$("#zela").hide();
		break;
		case "1":
		$("#bivag").show();
		$("#zela").hide();
		break;
		case "2":
		$("#bivag").show();
		$("#zela").show();
		break;
	}
}

function loadChildDemographicData(selectElement, targetElementId, dependentElement, isPreselectionNeeded){
	if($(selectElement).val()!=0){
	$.ajax({
		type: 'GET',
		url: "/get-demographic-data-by-parent/"+$(selectElement).val(),
		
		success: function(data) {
			$("#"+targetElementId).empty();
			var _html='';
			if(data && data.length && !isPreselectionNeeded){
				_html='<option value=0> Please Select</option>';
			}
			for(var i=0;i<data.length;i++){
				_html+='<option value="'+data[i].id+'" >'+data[i].name+'</option>';
			}
			$("#"+targetElementId).html(_html);

			if(dependentElement!=null){
				$("#"+dependentElement).empty();
			}
			if(data.length && data[0].type==1){
				loadChildDemographicData($("#"+targetElementId),dependentElement, null, isPreselectionNeeded);
			}
		},
		error: function(data) {
			alert("error");
		}
	
	});
}
}