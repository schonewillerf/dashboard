/**
 * Makes Http call to backend and refreshes page when backend is done updating
 */
function refresh(){
    httpGetAsync('/refresh', 'GET', function (){
        location.reload();
    });
}