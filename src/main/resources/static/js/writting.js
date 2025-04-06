const button = document.getElementById("submit");
const baseUrl = window.location.origin;
const articleId = document.querySelector("#articleId").value;
let articleTitle = document.querySelector("input[name='title']");
let articleBody = document.querySelector("textarea[name='body']");

if(articleId){

    fetch(baseUrl+"/api/article/"+articleId,{
        method: "GET",
    })
    .then((response)=>response.json())
    .then((response)=>{
        articleTitle.value = response.title;
        articleBody.value = response.body;
        console.log(response.title);
        console.log(response.body);
    });
}
if(button != null){
    button.addEventListener('click',()=>{
        
        const title = articleTitle.value;
        const content = articleBody.value;
        
        let responseBody = {
            title: title,
            body: content
        };
        if(articleId!==undefined)
            responseBody.articleId = articleId;

        fetch(baseUrl+"/api/articles",{
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(responseBody),
        }).then(response=>{
            if(response.status == 200){
                alert("성공...");
                window.location.replace(baseUrl+"/main/articles/0");
            }
            else{
                alert("실패");
            }
            return response.json();
        }).then(response=>console.log(response))
    });
}