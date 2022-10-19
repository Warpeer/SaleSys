
function showLogin(){
    $('#login-form').css('display', 'block');
    encryptFirstPassword();
}
function hideLogin(){
    $('#login-form').css('display', 'none');
}

function logIn() {
    const url = "/logIn?email="+$('#email').val()+"&password="+$('#password').val();
    $.get(url, (response)=>{
        if(response){
            window.location.href="../employee/dashboard.html"
        }else{
            $('#loginError').html("Feil brukernavn eller passord.");
        }
    })
}
function logOut(){
    const url = "/logOut";
    $.get(url, ()=>{
        window.location.href="../index.html";
    });
}