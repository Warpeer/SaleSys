$(()=>{
    const url="/adminCheck";
    $.get(url, (response)=>{
        if(!response){
            window.location.href="../employee/dashboard.html";
        }
    });
})