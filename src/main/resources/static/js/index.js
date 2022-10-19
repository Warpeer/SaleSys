$(()=>{
    let activeBreadCrumb = $('#stage-1');
    updateBreadcrumbs(activeBreadCrumb);
})

function encryptFirstPassword(){
    $.get("/encryptFirstPassword", (response)=>{
        if(response){
            console.log("Kryptert!");
        }else console.log("Kunne ikke kryptere passord! D:");
    });
}

function openForm() {
    const form = document.getElementById("myForm");
    form.style.height = "70vh";
    form.style.overflowX = "hidden";
    form.style.bottom = "0";
    form.style.marginBottom = "15vh";


    const formContent = document.getElementById("form-content");
    formContent.style.paddingBottom = "2rem";

}
function closeForm() {
    const form = document.getElementById("myForm");
    form.style.height = "0";
    form.style.bottom = "-5rem";
    form.style.marginBottom = "0";
}

const slider = document.getElementById("dataAmount");
slider.oninput = function () {
    let value = slider.value;
    $('#sliderValue').val(value);
}


function showResults() {
    updateBreadcrumbs($('#stage-2'));
    const form = $('#myForm');
    const height = form.height();

    const price = $('#price').val();
    const dataAmount = $('#sliderValue').val();

    saveVisitor();

    const contentArea = $('#form-content-wrapper');
    const contentHeight = contentArea.height();
    contentArea.html("");
    form.height(height);

    resultBuilder(contentHeight);

    generateResults(price, dataAmount);

}
function saveVisitor(){
    const PersonalSubscribtion = {
        first_name : $('#fname').val(),
        last_name : $('#lname').val(),
        phone_number : $('#phoneNumber').val(),
        provider : $('#provider').val().toUpperCase(),
        price : $('#price').val(),
        dataAmount : $('#dataAmount').val(),
    }
    if(PersonalSubscribtion.first_name===""){
        PersonalSubscribtion.first_name=null;
    }
    if(PersonalSubscribtion.last_name===""){
        PersonalSubscribtion.last_name=null;
    }
    $.post("/saveVisitor", PersonalSubscribtion, ()=>{

    });
}

function resultBuilder(height){
    let result = "";
    result += "<div id='resultContent'><p></p></div>"
    const contentArea = $('#form-content-wrapper');
    contentArea.html(result);
    contentArea.height(height);
    contentArea.css("display", "flex");
    contentArea.css("justify-content", "space-around");
    contentArea.css("align-items", "center");
    contentArea.css("flex-direction", "column");

}

function updateBreadcrumbs(stage){
    let formStage = stage;
    if(formStage===$('#stage-2')){
        console.log("Stage two");
        formStage.css("background-color", "dodgerblue");
        formStage.css("color", "white");
        $('#stage-1').css("background-color", "dodgerblue");
        $('#stage-1').css("color", "white");
    }else{
        formStage.css("background-color", "dodgerblue");
        formStage.css("color", "white");
    }

    const divider = $('#breadcrumb-divider');
    divider.css("background-color", "dodgerblue");
    divider.css("color", "white");
    divider.css("border", "none");
}

function generateResults(theirPrice, theirDataAmount) {
    const resultContent = $('#resultContent');
    let html = "";
    //SAMMENLIGNING MÅTE 1
    const ourAmount = generateClosest(theirDataAmount, "amount");
    const ourPrice = findValInArray(ourPrices, ourAmount, "amount");
    let savePrice = theirPrice-ourPrice;
    let saveAmount = ourAmount-theirDataAmount;

    const ourFactor = ourPrice/ourAmount;
    const theirFactor = theirPrice/theirDataAmount;

    const flag = ourFactor < theirFactor;
    // console.log(flag + " Vår faktor = " + ourFactor + " Deres faktor: " + theirFactor);
    // console.log("ourPrice: "+ ourPrice + ", theirPrice: " + theirPrice);
    // console.log("ourAmount: "+ ourAmount + " Their amount: " + theirDataAmount);
    //SAMMENLIGNING MÅTE 2
    const ourPrice1 = generateClosest(theirPrice, "price");
    let ourAmount1 = findValInArray(ourPrices, ourPrice1, "price");
    const savePrice1 = ourPrice1-theirPrice;


    if(ourPrice<=theirPrice && flag){
        if(savePrice===0){
            html = "<p>Med oss kan du få <span class='resultFormatting'>" + saveAmount +"GB</span> ekstra til samme pris!</p>";
        }else if(saveAmount!==0 && saveAmount>0){
            html = "<p>Vi tilbyr hele <span class='resultFormatting'>" + saveAmount +"GB</span> ekstra der du sparer <span class='resultFormatting'>"+savePrice+"kr</span> /måned!</p>";
        }else if(saveAmount<0 && saveAmount!==0){
            html = "<p>Dersom du vil spare kan vi tilby "+ (-saveAmount) +"GB mindre der du sparer <span class='resultFormatting'>"+savePrice+"kr</span> /måned.</p>";
        }else{
            html = "<p>Vi kan tilby <span class='resultFormatting'>SAMME</span> datamengde der du sparer <span class='resultFormatting'>"+savePrice+"kr</span> /måned!</p>";
        }
    }else{
        // console.log("ourPrice1: "+ ourPrice1 + ", theirPrice: " + theirPrice);
        // console.log("ourAmount1: "+ ourAmount1 + " Their amount: " + theirDataAmount);
        if(ourAmount1>theirDataAmount && savePrice1>0){
            html = "<p>Hos oss kan du få <span class='resultFormatting'>" + ourAmount1 + "GB</span> /måned for bare <span class='resultFormatting'>" + savePrice1 + ",-</span ekstra!</p>";
        }else if(ourAmount1>theirDataAmount && savePrice1<0){
            html = "<p>Hos oss kan du få hele <span class='resultFormatting'>" + ourAmount1 + "GB</span> /måned for <span class='resultFormatting'>" + (-savePrice1) + ",-</span> billigere!</p>";
        }else if(ourAmount1>theirDataAmount && savePrice1===0){
            html = "<p>Hos oss kan du få hele <span class='resultFormatting'>" + ourAmount1 + "GB</span> /måned for <span class='resultFormatting'>samme</span> pris!</p>";
        }else{
            html="<p>Vi kan dessverre ikke tilby bedre tjeneste. Ha en fin dag :)</p>";
        }
    }
    html += "</br></br><p style='color: #484857'>Les mer hos <a href='https://www.ice.no/mobilabonnement/?gclid=Cj0KCQjw166aBhDEARIsAMEyZh467XkeONKN5goSV-RnXohtcYW7nB7bgwPkqPH6JctYoogwYO9i5ewaAhFrEALw_wcB&gclsrc=aw.ds' style='color: dodgerblue'>ice.no</a>.</p>";
    resultContent.html(html);
    //$('#resultContent p').contents().unwrap().wrapAll('<p>');
}



function findValInArray(a, valToFind, inputAmountOrPrice){
    if(inputAmountOrPrice.toLowerCase()==="amount"){
        for(let i = 0; i<a.length;i++){
            if(a[i].dataAmount===valToFind){
                return a[i].price;
            }
        }
    }else if(inputAmountOrPrice.toLowerCase()==="price"){
        for(let i = 0; i<a.length;i++){
            if(a[i].price===valToFind){
                return a[i].dataAmount;
            }
        }
    }

}

function generateClosest(amount, priceOrAmount){
    let closest= -1;
    if(priceOrAmount.toLowerCase()==="price"){
        let minDif = 9000;
        for(let i = 0; i<ourPrices.length; i++){
            let difference = amount-ourPrices[i].price;
            if(difference<0){
                difference=-difference;
            }
            if(difference<=minDif){
                minDif=difference;
                if(i+1>ourPrices.length){
                    closest=ourPrices[i+1].price;
                }else closest=ourPrices[i].price;

            }
        }
    }else if(priceOrAmount.toLowerCase()==="amount"){
        let minDif = 9000;
        for(let i = 0; i<ourPrices.length; i++){
            let difference = amount-ourPrices[i].dataAmount;

            if(difference<0){
                difference=-difference;
            }
            //console.log("Input: "+amount+ ", Our amount: "+ourPrices[i].dataAmount+ " dif: " + difference );
            if(difference<=minDif){
                minDif=difference;
                closest=ourPrices[i].dataAmount;
            }
        }
    }
    return closest;
}