function updateUpdates() {
    httpGetAsync('/getUpdatedTasks', 'GET', function (result) {
        const json = JSON.parse(result);
        document.querySelector(".list-group").innerHTML = "";
        let lastUpdatedIssueDone = Boolean(false);

        // loop throught results
        for (let i = 0; i < json.length; i++) {
            let obj = json[i];
            // get parent element
            const parentElemet = document.querySelector(".list-group");

            const elementList = document.createElement("li");
            elementList.classList.add("list-group-item", "justify-content-between", "align-items-center", "container");
            //create div row
            // add div row
            const elementDivRow = document.createElement('div');
            elementDivRow.classList.add("row", "align-items-center");
            //add div row2
            const elementDivRow2 = document.createElement("div");
            elementDivRow2.classList.add("row", "align-items-center");
            // col spacer
            const divRowspacer = document.createElement("div");
            divRowspacer.classList.add("col-1");
            elementDivRow2.appendChild(divRowspacer);
            // col-11
            const divRow12 = document.createElement("div");
            divRow12.classList.add("col-11");
            elementDivRow2.appendChild(divRow12);

            // add col with img
            const elementColImg = document.createElement("div");
            elementColImg.classList.add("col-1", "contributerImg");
            elementDivRow.appendChild(elementColImg);
            const elemenAuthorAvatar = document.createElement("img");
            elemenAuthorAvatar.src = obj.avatarUrl;
            elemenAuthorAvatar.title = obj.author;
            elementColImg.appendChild(elemenAuthorAvatar);

            // add col with issue summary col 9
            const elementCol9 = document.createElement("div");
            elementCol9.classList.add("col-11", "IssueSummary");
            elementDivRow.appendChild(elementCol9);
            const elementPWithSummary = document.createElement("p");
            elementCol9.appendChild(elementPWithSummary);
            const textSummary = document.createTextNode(obj.itemSummary);
            elementPWithSummary.appendChild(textSummary);

            // div col-1 for sp
            const elementSpanSP = document.createElement("span");
            elementSpanSP.classList.add("badge", "badge-primary", "badge-pill");
            const textSP = document.createTextNode(obj.storyPoints);
            elementSpanSP.appendChild(textSP);

            // item  status
            const ElementPStatus = document.createElement("p");
            if (obj.itemType === "status") {
                const iArrow = document.createElement("i");
                iArrow.classList.add("fas", "fa-arrow-right", "fa-xs", "ml-2", "mr-2");

                const textStatusP = document.createElement("p");
                const textNodeStatusFrom = document.createTextNode(obj.changedStatusFrom);
                const textNodeStatusTo = document.createTextNode(obj.changedStatusTo);

                textStatusP.appendChild(textNodeStatusFrom);
                textStatusP.appendChild(iArrow);
                textStatusP.appendChild(textNodeStatusTo);
                divRow12.appendChild(textStatusP);

                //Get todays date
                let today = new Date();
                let dd = String(today.getDate()).padStart(2, '0');
                let mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
                let yyyy = today.getFullYear();
                let time = today.getTime();
                let correctedTime = time - 30000;

                let issueDate = new Date(obj.lastChangedOn + " " + obj.lastChangedOnTime);
                let issueTime = issueDate.getTime();

                today = yyyy + '-' + mm + '-' + dd;

                //Check if status is set to done AND it has been changed today
                if(obj.resolved === true && obj.lastChangedOn === today && issueTime > correctedTime){
                    lastUpdatedIssueDone = true;
                }

            } else if (obj.itemType === "description") {
                const textStatusP = document.createElement("p");
                const textStatusItem = document.createTextNode("De omschrijving is gewijzigd.");
                textStatusP.appendChild(textStatusItem);
                divRow12.appendChild(textStatusP);

            }

            elementList.appendChild(elementDivRow);
            elementList.appendChild(elementDivRow2)
            elementList.appendChild(elementSpanSP);

            parentElemet.appendChild(elementList);

        }
        if(lastUpdatedIssueDone){
            confetti();
        }
    });
}
updateUpdates();