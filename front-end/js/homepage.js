let selectedUserId = 0;
let currentLoggedInUser = null;

$(document).ready(function () {
    console.log('JQuery plugin is loaded successfully in homepage.js file');

    // When home page is loaded hide below divs
    $('#albums').hide();
    $('#gallery').hide();
    $('#photo-uploader-form').hide();

    // Get id from url
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('userId');
    console.log('Current user id',id);

    // If user is not logged in then redirect to the login page
    if(id == null){
        window.location.replace('./index.html');
    }
    else{
        getUserDetailsById(id);
    }
});

// Logging out user from the application
function logout(){
    console.log('Loging out');
    window.location.replace('./index.html');
}

// Checking the role of the current logged in user
function checkRoleOfTheCurrentUser(currentUser){
    // If the current user has role of Admin then show privileges for the admin
    // otherwise show the privileges of the customer
    if(currentUser.role === 'Admin'){
        console.log('Current user is admin');
        $('.adminLink').show();
        $('.customerLink').hide();
    }
    else{
        $('.customerLink').show();
        $('.adminLink').hide();
    }
}

// Navigate to homepage
function showHomepage(){
    $('#mainDiv').show();
    $('#albums').hide();
    $('#photo-uploader-form').hide();
    $('#gallery').hide();
}

// Showing photos for the current logged in customer
function showPhotoByCurrentLoggedInUser(){
    console.log('showPhotoByCurrentLoggedInUser: ' + currentLoggedInUser);
    const userId = currentLoggedInUser.id;
    $('#gallery').html('');
    // Making AJAX request for fetching photos for the current user logged in user
    $.ajax({
        url: `http://localhost:8029/users/photos/${userId}`,
        async: true,
        dataType: 'json',
        success: function (response) {
            console.log();
            $('#mainDiv').hide();
            $('#albums').hide();
            $('#photo-uploader-form').hide();
            $('#gallery').show();
            console.log('showPhotoByCurrentLoggedInUser Response -> ', response)
            // Check whether the photos exist for the current user or not
            if(response.files.length > 0) {
                var title = '<h5> Your Photos </h5>';
                var card = '<div class="row mt-2" >';
                for (var i = 0; i < response.files.length; i++) {
                    card += createImageItemForCurrentUser(response.files[i]);
                }
                card += '</div>';
                $('#gallery').append(title);
                $('#gallery').append(card);
            }
            else{
                var title = '<h5> This album is empty! Upload some photos </h5>';
                $('#gallery').append(title);
            }

            
        },
        error: function (error) {
            console.log('Error -> ', error);
        }
    });

}

// Getting user information by id
function getUserDetailsById(userId){
    console.log('getUserDetailsById called', userId);
    // Making AJAX request to fetch user information
    $.ajax({
        url: `http://localhost:8029/users/${userId}`,
        async: true,
        dataType: 'json',
        success: function (response) {
            // Check the status of the response received
            if(response.status === "Failure"){
                console.log(response.message);
            }
            else{
                currentLoggedInUser = response.data;
                console.log('currentLoggedInUser ',currentLoggedInUser);
                checkRoleOfTheCurrentUser(currentLoggedInUser);
                $('#currentUserName').text(currentLoggedInUser.name)
            }
                
        },
        error: function (error) {
            console.log('populateAlbumsList Error -> ', error);
        }
    });
}

// Populating all the albums in the album list
function populateAlbumsList() {
    $('#albumsList').html('');
    console.log('Populating albums list');
    $.ajax({
        url: 'http://localhost:8029/users/albums',
        async: true,
        dataType: 'json',
        success: function (response) {
            console.log('populateAlbumsList Response -> ', response)
            var select = "<select id='albumId' class='form-control' />";
            select += '<option selected disabled> Select Album </option>';
            for (var i = 0; i < response.data.length; i++) {
                select += '<option value="'+response.data[i].id+'"> '+response.data[i].name+' </option>';
            }
            // Appending album into album list
            $('#albumsList').append(select);
        },
        error: function (error) {
            console.log('populateAlbumsList Error -> ', error);
        }
    });
}

// Show all the albums availabl

function showAlbums(){
    // Show only albums div and hide all other divs
    $('#mainDiv').hide();
    $('#gallery').hide();
    $('#photo-uploader-form').hide();
    $('#albums').show();
    $('#albums').html('');
    $.ajax({
        url: 'http://localhost:8029/users/albums',
        async: true,
        dataType: 'json',
        success: function (response) {
            console.log('Response -> ', response)
            var card = '<div class="row">';
            for (var i = 0; i < response.data.length; i++) {
                card += createAlbumItem(response.data[i]);
            }
            card += '</div>';
            $('#albums').append(card);
        },
        error: function (error) {
            console.log('Error -> ', error);
        }
    });
}

// Create album
function createAlbumItem(album) {
    return '<div class="col">' +
        '<div class="card" style="width: 18rem;">' +
        '<div class="card-body">' +
        '<h5 class="card-title">' + album.name + '</h5>' +
        '<button class="btn btn-dark" onClick="showPhotosByAlbumId('+album.id+')"> View </button>' +
        '</div>' +
        '</div>' +
        '</div>';
}

// Show all the photos of the selected album
function showPhotosByAlbumId(albumId) {
    console.log('Show photos by album id: ' + albumId);
    selectedUserId = albumId;
    console.log('selectedUserId', selectedUserId);
    $('#gallery').html('');
    $.ajax({
        url: `http://localhost:8029/users/photos/${albumId}`,
        async: true,
        dataType: 'json',
        success: function (response) {
            console.log();
            $('#albums').hide();
            $('#photo-uploader-form').hide();
            $('#gallery').show();
            console.log('showPhotosByAlbumId Response -> ', response)
            
            if(response.files.length > 0) {
                var title = '<h5> '+response.name+' Album </h5>';
                var card = '<div class="row mt-2" >';
                for (var i = 0; i < response.files.length; i++) {
                    card += createImageItem(response.files[i]);
                }
                card += '</div>';
                $('#gallery').append(title);
                $('#gallery').append(card);
            }
            else{
                var title = '<h5> This album is empty! Upload some photos </h5>';
                $('#gallery').append(title);
            }

            
        },
        error: function (error) {
            console.log('Error -> ', error);
        }
    });
}

// Create image item for the current user
function createImageItemForCurrentUser(file){
    var imageUrl = '../photos/' + file.name;
    return '<div class="col">' +
        '<div class="card mt-2" style="width: 15rem;">' +
        '<img src="'+imageUrl+'" class="card-img-top" alt="..." style="width:15rem; height:15rem;">' +
        '<div class="card-body">' +
        '<span>'+
        '<a href="'+imageUrl+'" class="btn btn-success m-2">Download</a>' +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';

}

// Create image item for the selected image
function createImageItem(file) {
    var imageUrl = '../photos/' + file.name;
    return '<div class="col">' +
        '<div class="card" style="width: 15rem;">' +
        '<img src="'+imageUrl+'" class="card-img-top" alt="..." style="width:15rem; height:15rem;">' +
        '<div class="card-body">' +
        '<span>'+
        '<button class="btn btn-danger" onClick="deleteFileById('+file.id+')"> Delete </button>' +
        '<a href="'+imageUrl+'" class="btn btn-success m-2">Download</a>' +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
}

// Delete photo from the album
function deleteFileById(fileId){
    console.log('deleteFileId ', fileId);
    const headers = { 
        'Accept': '*',
        'Content-Type': 'application/json' 
    }
    $.ajax({
        url: `http://localhost:8029/files/${fileId}`,
        type: 'delete',
        crossDomain: true,
        headers: headers,
        success: function (response) {
            console.log('Response: ' + response.message);
            showPhotosByAlbumId(selectedUserId);
        },
        error: function (error) {
            console.log('Error: ' + error.message);
        }
    });
}

// Show form to upload photos

function showPhotoUploaderForm(){
    populateAlbumsList();
    $('#mainDiv').hide();
    $('#albums').hide();
    $('#gallery').hide();
    $('#photo-uploader-form').show();
}


// Uploading files in the database
function uploadFile() {
    console.log('Uploading file to server --------------------------------');
    var albumId = $("select#albumId option").filter(":selected").val();
    console.log('Album upload', albumId);
    var fd = new FormData();
    // Getting file contents for the selected file
    var files = $('#file')[0].files[0];
    // Appending file into form data object
    fd.append('file', files);
    $.ajax({
        url: `http://localhost:8029/files/upload/${albumId}`,
        type: 'post',
        data: fd,
        contentType: false,
        processData: false,
        success: function (response) {
            console.log('File uploaded to server successfully --------------------------------');
            $('#file').val('');
            $('#imagePreview').hide();
            alert('File uploaded successfully');
        },
        error: function (error) {
            console.log('Unable to upload file to server successfully --------------------------------');
            console.log('Error: ' + error);
        }
    });
}