const HttpUpdates = new XMLHttpRequest();
const urlUpdates = 'http://localhost:8080/getUpdatedTasks';
HttpUpdates.open("GET", urlUpdates );
HttpUpdates.send();
var parentElemet=document.querySelector(".list-group");

HttpUpdates.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {

        var json = JSON.parse(HttpUpdates.responseText);
        var sumarryArray=new Array();
        var statusArray=new Array();
        var spArray=new Array();
        // loop throught results
        for (var i=0; i<json.length;i++ )
        {
            var obj=json[i];


            spArray.push(obj.storyPoints);
            statusArray.push(obj.issueStatus);
            sumarryArray.push(obj.issueName);
            createList(spArray[i],statusArray[i],sumarryArray[i]);
        }
    }
}


// create list group dynamic
function createList(storyPoints, statusArr, SummaryArr)
{
    var elementList=document.createElement("li");
    elementList.classList.add("list-group-item","d-flex","justify-content-between","align-items-center");

    var elementP=document.createElement("p");
    var elementTextSummary=document.createTextNode(SummaryArr);
    var elementSpanTaskStatus =document.createElement("span");

    var elementText2=document.createTextNode("     -    "
        +statusArr.toUpperCase());
    var taskStatus =statusArr.toUpperCase();
    if(taskStatus=="DONE")
    {
        elementSpanTaskStatus.classList.add("green");
    }
    else if (taskStatus="DOING")
    {
        elementSpanTaskStatus.classList.add("orange");
    }

    else if (taskStatus="TESTING")
    {
        elementSpanTaskStatus.classList.add("yellow");
    }


    elementSpanTaskStatus.appendChild(elementText2);
    elementP.appendChild(elementTextSummary);
    elementP.appendChild(elementSpanTaskStatus);
    var elementSpan=document.createElement("span");
    elementSpan.classList.add("badge","badge-primary","badge-pill");
    var textSP=document.createTextNode(storyPoints);
    elementSpan.appendChild(textSP);
    elementList.appendChild(elementP);
    elementList.appendChild(elementSpan);
    parentElemet.appendChild(elementList);
}