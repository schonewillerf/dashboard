//  <li class="list-group-item d-flex justify-content-between align-items-center">

//get array from backend

refreshProjectSummaryTable();

httpGetAsync('http://localhost:8080/getUpdatedTasks', 'GET', function (result){
    const json = JSON.parse(result);
    const sumarryArray = [];
    const statusArray = [];
    const spArray = [];

    // loop throught results
    for (let i=0; i<json.length; i++ )
    {
        const obj = json[i];
        spArray.push(obj.storyPoints);
        statusArray.push(obj.issueStatus);
        sumarryArray.push(obj.issueName);

        // get parent element
        const parentElemet = document.querySelector(".list-group");

        const elementList = document.createElement("li");
        elementList.classList.add("list-group-item","d-flex","justify-content-between","align-items-center");

        const elementP = document.createElement("p");
        const elementTextSummary = document.createTextNode(sumarryArray[i]);
        const elementSpanTaskStatus = document.createElement("span");

        const elementText2 = document.createTextNode("     -    "
            + statusArray[i].toUpperCase());
        const taskStatus = statusArray[i].toUpperCase();
        if(taskStatus === "DONE")
        {
            elementSpanTaskStatus.classList.add("green");
        }
        else if (taskStatus === "DOING")
        {
            elementSpanTaskStatus.classList.add("orange");
        }

        else if (taskStatus === "TESTING")
        {
            elementSpanTaskStatus.classList.add("yellow");
        }


        elementSpanTaskStatus.appendChild(elementText2);

        elementP.appendChild(elementTextSummary);
        elementP.appendChild(elementSpanTaskStatus);

        const elementSpan = document.createElement("span");
        elementSpan.classList.add("badge","badge-primary","badge-pill");
        const textSP = document.createTextNode(spArray[i]);
        elementSpan.appendChild(textSP);
        elementList.appendChild(elementP);
        elementList.appendChild(elementSpan);
        parentElemet.appendChild(elementList);
    }
});

// function to refresh proejct summary table
function refreshProjectSummaryTable()
{
    const updates = ["doing", "done", "testing", "sprint%20backlog"];

    for (let i=0; i<updates.length; i++)
    {
        const requestRefresh = new XMLHttpRequest();
        console.log("updates:");
        console.log(updates[i]);
        const requestUpdateUri = "http://localhost:8080/updateProjectSummary?status=" + updates[i];
        requestRefresh.open("GET", requestUpdateUri);
        requestRefresh.send();
    }
}