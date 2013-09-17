//
//	jQuery Validate example script
//
//	Prepared by David Cochran
//
//	Free for your use -- No warranties, no guarantees!
//
var imagedefault = 'http://www.erepublik.com/images/default_avatars/Citizens/default_male_142x142.gif';
		
function setPlayer(player){
	if (player.registered){
		$('.citizen_avatar').css("background-image", "url("+player.image+")"); 
		if(!player.rocket){
			$('.selectpicker option:last-child').attr('disabled', 'disabled');
		}
		else{
			$('.selectpicker option:last-child').removeAttr("disabled");
		}
		$('.selectpicker').prop('disabled', false);
		$('#quant').prop('disabled', false);
		$('.selectpicker').selectpicker('refresh');
		$('.list-group a:first-child').text(player.name);
		$('.list-group a:last-child').text(player.availableFF);
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
	var player = null;
	RequestService.getCurrentStatus(value,{
		  callback:function(data) {
			  player = data;
		  },
		  async:false
		});
	setPlayer(player);
	return player.registered;
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