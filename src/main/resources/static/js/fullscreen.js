// This function toggles fullscreen if clicked
function toggleFullscreen() {
    // Lets to get isInFullScreen and select whole document
    const isInFullScreen = (document.fullscreenElement && true) || (document.webkitFullscreenElement && true) || (document.mozFullScreenElement && true) || (document.msFullscreenElement && true);
    const docElm = document.documentElement;

    // Request fullscreen if isInFullScreen is false
    if (!isInFullScreen) {
        if (docElm.requestFullscreen) {
            docElm.requestFullscreen();
        }

        else if (docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
        }

        else if (docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
        }

        else if (docElm.msRequestFullscreen) {
            docElm.msRequestFullscreen();
        }
    }

    // Exit fullscreen if isInFullScreen is true
    else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        }

        else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }

        else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        }

        else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        }
    }
}