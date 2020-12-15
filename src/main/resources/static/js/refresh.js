/**
 * Makes Http call to backend and refreshes page when backend is done updating
 */
function refresh(){
    httpGetAsync('http://localhost:8080/refresh', 'GET', function (){
        location.reload();
    });
}