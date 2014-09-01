/**
 * Created by dilshani on 7/20/14.
 */

function toggleTable() {
    if (document.getElementById("enable_check").checked) {
        var nodes = document.getElementById("toggle").getElementsByTagName('*');
        for (var i = 0; i < nodes.length; i++) {
            nodes[i].disabled = false;
        }

    } else {
        var nodes = document.getElementById("toggle").getElementsByTagName('*');
        for (var i = 0; i < nodes.length; i++) {
            nodes[i].disabled = true;
        }
    }
}
window.onload = toggleTable;


function alertMessage(value) {


    CARBON.showInfoDialog(value);

}

function alertError(value) {


    CARBON.showErrorDialog(value);

}


function DoValidation() {

    var password = document.forms["details_form"]["password"].value;
    var username = document.forms["details_form"]["username"].value;
    var url_address = document.forms["details_form"]["url_address"].value;

    if (password == null || password == "" || username == null || username == "" || url_address == null || url_address == "") {
        alertError("Please make sure that all properties are filled");
        return false;
    }
    else {

        return true;
    }
}

function validateURL() {

    var xmlhttp;
    var urls = document.getElementById("url_address").value;

    if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    }
    else {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            if (xmlhttp.responseText) {
                if ((xmlhttp.responseText) == "true") {
                    alertMessage("util validate Successful");
                } else {

                    alertError("util validate Failed");
                }
            } else {

                alertError(xmlhttp.responseText);

            }
        }
    };
    xmlhttp.open("GET", "/carbon/stat-publisher/URLValidateServlet?url_address=" + urls, true);
    xmlhttp.send();

}