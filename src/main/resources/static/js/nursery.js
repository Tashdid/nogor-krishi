function loadChildDemographicData(selectElement, targetElementId){
	if($(selectElement).val()!=0){
	$.ajax({
		type: 'GET',
		url: "/get-demographic-data-by-parent/"+$(selectElement).val(),
		
		success: function(data) {
			$("#"+targetElementId).empty();
			var _html='';
			for(var i=0;i<data.length;i++){
				_html+='<option value="'+data[i].id+'" >'+data[i].name+'</option>';
			}
			$("#"+targetElementId).html(_html);

			if(targetElementId=='district'){
				$("#city").empty();
			}
			if(data.length && data[0].type==1){
				loadChildDemographicData($("#"+targetElementId),'city');
			}
		},
		error: function(data) {
			alert("error");
		}
	
	});
}
}