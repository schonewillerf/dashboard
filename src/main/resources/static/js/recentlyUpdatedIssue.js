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

        createList(spArray[i],statusArray[i], sumarryArray[i]);

   }
});


// create list group dynamic
function createList(storyPoints, statusArr, SummaryArr)
{
    // get parent element
    const parentElemet = document.querySelector(".list-group");

    const elementList = document.createElement("li");
    elementList.classList.add("list-group-item","d-flex","justify-content-between","align-items-center");

    const elementP = document.createElement("p");
    const elementTextSummary = document.createTextNode(SummaryArr);
    const elementSpanTaskStatus = document.createElement("span");

    const elementText2 = document.createTextNode("     -    "
        + statusArr.toUpperCase());
    const taskStatus = statusArr.toUpperCase();
    if(taskStatus === "DONE")
    {
        elementSpanTaskStatus.classList.add("green");
    }
    elementSpanTaskStatus.appendChild(elementText2);
    elementP.appendChild(elementTextSummary);
    elementP.appendChild(elementSpanTaskStatus);
    const elementSpan = document.createElement("span");
    elementSpan.classList.add("badge","badge-primary","badge-pill");
    const textSP = document.createTextNode(storyPoints);
    elementSpan.appendChild(textSP);
    elementList.appendChild(elementP);
    elementList.appendChild(elementSpan);
    parentElemet.appendChild(elementList);
}