//  <li class="list-group-item d-flex justify-content-between align-items-center">

//get array from backend

refreshProjectSummaryTable();
const HttpUpdates = new XMLHttpRequest();
const urlUpdates = 'http://localhost:8080/getUpdatedTasks';
HttpUpdates.open("GET", urlUpdates );
HttpUpdates.send();

HttpUpdates.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200) {

        var json = JSON.parse(HttpUpdates.responseText);
        //console.log("response: ", json);
        var sumarryArray=new Array();
        var statusArray=new Array();
        var spArray=new Array();
        // loop throught results
        for (var i=0; i<json.length;i++ )
        {
            var obj=json[i];
            console.log("object:");
            console.log(obj);
            spArray.push(obj.storyPoints);
            statusArray.push(obj.issueStatus);
            sumarryArray.push(obj.issueName);
            // get parent element

            var parentElemet=document.querySelector(".list-group");

            var elementList=document.createElement("li");
            elementList.classList.add("list-group-item","d-flex","justify-content-between","align-items-center");
            var elementText=document.createTextNode(sumarryArray[i]);
            var elementText2=document.createTextNode("     -    "
                +statusArray[i]);

//  <span class="badge badge-primary badge-pill">14</span>

            var elementSpan=document.createElement("span");
            elementSpan.classList.add("badge","badge-primary","badge-pill");
            var textSP=document.createTextNode(spArray[i]);
            elementSpan.appendChild(textSP);


            elementList.appendChild(elementText);
            elementList.appendChild(elementText2);
            elementList.appendChild(elementSpan);
            parentElemet.appendChild(elementList);

       // <li class="list-group-item d-flex justify-content-between align-items-center">




        }



    }
}


// function to refresh proejct summary table

function refreshProjectSummaryTable()
{
    var updates=["doing", "done", "testing", "sprint%20backlog"];

    for (var i=0; i<updates.length; i++)
    {
        var requestRefresh=new XMLHttpRequest();
        console.log("updates:");
        console.log(updates[i]);
        var requestUpdateUri="http://localhost:8080/updateProjectSummary?status="+updates[i];
        requestRefresh.open("GET", requestUpdateUri);
        requestRefresh.send();

    }


}