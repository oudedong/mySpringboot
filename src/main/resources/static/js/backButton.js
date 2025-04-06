backButton = document.querySelector("#back");

if(backButton != null){
    backButton.onclick = function(){
        window.history.back();
    }
}