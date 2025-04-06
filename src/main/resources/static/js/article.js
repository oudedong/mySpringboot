const baseUrl = window.location.origin;
const currentUrl = window.location.href;
const currentId = currentUrl.split('/').filter(Boolean).pop();

const deleteButton = document.querySelector("#delete");
const modifyButton = document.querySelector("#modify");

console.log(currentId);
if(deleteButton != null){
    deleteButton.addEventListener('click',()=>{
        fetch(baseUrl+"/api/article/"+currentId, {
            method: "delete",
        }).then(response=>{
            if(response.status == "200"){
                alert("삭제 성공!");
                window.location.href = baseUrl+"/main/articles/0";
            }
            else{
                alert("삭제 실패...");
            }
        });
    });
}