//
//	jQuery Validate example script
//
//	Prepared by David Cochran
//
//	Free for your use -- No warranties, no guarantees!
//
var imagedefault = 'http://www.erepublik.com/images/default_avatars/Citizens/default_male_142x142.gif';
		
function setPlayer(dto){
	console.log(JSON.stringify(dto)); //FIXME remove
	if (dto.player){
		$('.citizen_avatar').css("background-image", "url("+dto.player.avatar+")"); 
		if(!dto.rocketAllowed){
			$('.selectpicker option:last-child').attr('disabled', 'disabled');
		}
		else{
			$('.selectpicker option:last-child').removeAttr("disabled");
		}
		$('.selectpicker').prop('disabled', false);
		$('#quant').prop('disabled', false);
		$('.selectpicker').selectpicker('refresh');
		$('.list-group a:first-child').text(dto.player.name);
		$('.list-group a:last-child').text(dto.availableFF);
	}
	else{
		$('.citizen_avatar').css("background-image", "url("+imagedefault+")"); 
		$('.selectpicker').prop('disabled', true);
		$('#quant').prop('disabled', true);
		$('.selectpicker').selectpicker('refresh');
		$('#nick').text("");
	}
	
}

function check(value, element){
	var dto = null;
	RequestService.getCurrentStatus(value,{
		  callback:function(data) {
			  dto = data;
		  },
		  async:false
		});
	setPlayer(dto);
	return dto.player;
}


$(document).ready(function(){

	$('.selectpicker').selectpicker();
		$('.citizen_avatar').css("background-image", "url("+imagedefault+")");  
		
	$.validator.addMethod(
	        "registered",check );
	
	$('#contact-form').validate({
	    rules: {
	      id: {
	        minlength: 2,
	        required: true,
	        registered: true
	      },
	      quant:{
	    	  number:true,
	    	  required:true
	      },
	      type:{
	    	  required:true
	      }
	    },
	    errorPlacement: function(error, element) {},
	    highlight: function(element) {
	    	$(element).removeClass('valid').addClass('error');
	    	$(element).closest('.input-group').removeClass('has-success').addClass('has-error');
		},
		unhighlight: function(element) {
			$(element).removeClass('error').addClass('valid');
			$(element).closest('.input-group').removeClass('has-error').addClass('has-success');
			}
	  });

}); // end document.ready