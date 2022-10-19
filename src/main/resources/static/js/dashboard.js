const providerLabels = [];
const providerDataset = [];
$(()=>{
    fetchVisitors();
})
function checkAdmin(location){
    $.get("/adminCheck", (r)=>{
        if(r){
            window.location.href="../admin/"+location;
        }
        else console.log("Du er ikke admin!");
    })
}
function delVisitor(id){
    const url = "/deleteOneVisitor?id="+id;
    $.get(url, (response)=>{
        if(response){
            window.location.href="../employee/dashboard.html";
        }else console.log("Kunne ikke slette");
    })
}
function fetchVisitors(){
    $.get("/fetchEveryVisitor", (visitorList)=>{
        formatVisitorList(visitorList);
    })
        .fail((jqXHR)=>{
            const json = $.parseJSON(jqXHR.responseText);
            $('#visitorListDenied').html(json.message);
        })
}

function searchInVisitor(){
    const searchInput = $('#searchInVisitor').val().toString();
    const inputArray = searchInput.split(" ");
    const keyword = inputArray[0];
    const target = inputArray[2];
    if(target===undefined  || keyword===""){
        fetchVisitors();
    }else{
        console.log("Target = " + "'"+target +"'" + " Keyword = " + "'"+keyword+"'");
        const url = "/searchInVisitors?keyword="+keyword+"&target="+target;
        $.get(url, (visitorList)=>{
            formatVisitorList(visitorList);
        }).fail(function (jqXHR) {
            const json = $.parseJSON(jqXHR.responseText);
            $('#search-error').html(json.message);
        })
    }
}

function printList(list){
    for(const t of list){
        console.log(t);
    }
}
function getData(chart){
    fetchData();
    console.log("feridg med fetching!");
    chart.data.labels = providerLabels;
    chart.data.datasets[0].data = providerDataset;
    chart.render();
}

let providerChartConfig = {
    type:'doughnut',
    data:{
        labels:[],
        datasets: [{
            label: 'Antall',
            data:[],
            backgroundColor:[
                'dodgerblue',
                'purple',
                'red',
                'yellow',
                'green',
                'yellow',
                'dark_pink'
            ],
            borderWidth: 1,
            borderColor: "#c5c5c5",
            hoverBorderWidth: "#000",
        }]
    },
    options:{

    }
}
let ctx = document.getElementById('providerChart').getContext('2d');
let providerChart = new Chart(ctx, providerChartConfig);

getData(providerChart);

function fetchDataset(copyDS) {
    $.get("/fetchProviderSet", (dataset)=>{
        transformDataSet(dataset, copyDS);
    })
}

function fetchProviders(copyDS){
    $.get("/fetchProviders", (providerList)=>{
        transformProviders(providerList, copyDS);
    });
}

function transformDataSet(inputDS, dataSet){
    for(let i = 0; i<inputDS.length; i++){
        dataSet[i] = inputDS[i];
    }
    return dataSet;
}
function transformProviders(inputDS, dataSet){
    for(let i = 0; i<inputDS.length; i++){
        dataSet[i] = inputDS[i].name;
    }
}
function fetchData(){
    fetchDataset(providerDataset);
    fetchProviders(providerLabels);
}


function formatVisitorList(visitors){
    let output = "<table id='visitorList'><tr><th>ID</th><th>Fornavn</th><th>Etternavn</th><th>Mobilnummer</th><th>ISP</th><th>Pris /mnd</th><th>GB</th><th></th></tr>";
    for(const visitor of visitors){
        output += "<tr><td>"+visitor.id+"</td><td>"+visitor.first_name+"</td><td>"+visitor.last_name+"</td><td>"+visitor.phone_number+"</td><td>"+visitor.provider+"</td>" +
            "<td>"+visitor.price+"</td><td>"+visitor.dataAmount+"</td><td><button id='btnDel' onclick='delVisitor("+visitor.id+")'>X</button></td></tr>";
    }
    output+="</table>";
    $('#visitorList-wrapper').html(output);
}