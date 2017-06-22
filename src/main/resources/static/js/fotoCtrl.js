function readFile() {
		  
		  if (this.files && this.files[0]) {
		    
		    var FR= new FileReader();
		    
		    FR.addEventListener("load", function(e) {
		      document.getElementById("img").src= e.target.result;
		    }); 
		    
		    FR.readAsDataURL( this.files[0] );
		  }
		  
};

document.getElementById("inp").addEventListener("change", readFile);
$('#img').click(function(e) {
    $(this).find('#inp:hidden').click();
    $('input:file')[0].click()
});