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
			// if(data.length && data[0].type==1){
			// 	loadChildDemographicData($("#"+targetElementId),dependentElement, null, isPreselectionNeeded);
			// }
		},
		error: function(data) {
			Swal.fire({
				icon: 'error',
				title: 'Oops...',
				text: 'কিছু সমস্যা হয়েছে, কিছুক্ষণ পর আবার চেষ্টা করুন'
			})
		}
	
	});
}
}