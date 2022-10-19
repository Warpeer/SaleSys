$(()=>{
    const url="/authenticate";
    $.get(url, (response)=>{
        if(!response){
            window.location.href="../index.html";
        }
    });
})