$(document).ready(function () {
    console.log('JQuery plugin is loaded successfully in index.js file');
    $('#responseMessageDiv').hide();
    $('#login-form').hide();
    hideResponse();
});

// Display login form
function showLoginForm() {
    $('#responseMessageDivForLogin').hide();
    $('#sign-up-form').hide();
    // Show login form div
    $('#login-form').show();

}

// Registering user in the database
function register() {
    console.log('Register Form submitted');

    // Checking all the fields are filled or not
    if ($('#user-name').val() === '' || $('#user-username').val() === '' || $('#user-password').val() === '') {
        $('#responseMessageDiv').show();
        $('.message').removeClass('text-success');
        $('.message').addClass('text-danger');
        $('.message').text("Please enter all required fields");
        hideResponse();
        return;
    }

    // Getting values from the fields
    const user = {
        name: $('#user-name').val(),
        username: $('#user-username').val(),
        password: $('#user-password').val(),
    }
    // Setting headers for the AJAX request
    const headers = {
        'Accept': '*',
        'Content-Type': 'application/json'
    }
    console.log('Registering user', user);
    // Making AJAX request for registering user
    $.ajax({
        url: 'http://localhost:8029/users/registration',
        type: 'post',
        data: JSON.stringify({ name: user.name, username: user.username, password: user.password }),
        crossDomain: true,
        headers: headers,
        success: function (response) {
            console.log('Response: ' + response);
            $('#responseMessageDiv').show();
            $('.message').removeClass('text-danger');
            $('.message').addClass('text-success');
            $('.message').text(response.message);

            // Resest form data
            $('#user-name').val('');
            $('#user-username').val('');
            $('#user-password').val('');

            hideResponse();
        },
        error: function (error) {
            console.log('Error: ' + error.message);
        }
    });
}

// This function automatically hides the response received from the server after a certain amount of time
function hideResponse() {
    setTimeout(() => {
        $('#responseMessageDiv').hide();
    }, 3500); // 👈️ time in milliseconds
}

// Authentication of the user is handled in this function
function login() {
    // Checking whether all the fields are filled or not
    if ($('#username').val() === '' || $('#password').val() === '') {
        $('#responseMessageDivForLogin').show();
        $('.messageForLogin').removeClass('text-success');
        $('.messageForLogin').addClass('text-danger');
        $('.messageForLogin').text('Please enter all required fields');
        setTimeout(() => {
            $('#responseMessageDivForLogin').hide();
        }, 2500);
        return;
    }

    // Getting data from the fields
    const user = {
        username: $('#username').val(),
        password: $('#password').val(),
    }
    const headers = {
        'Accept': '*',
        'Content-Type': 'application/json'
    }
    $.ajax({
        url: 'http://localhost:8029/users/authentication',
        type: 'post',
        data: JSON.stringify({ username: user.username, password: user.password }),
        crossDomain: true,
        headers: headers,
        success: function (response) {
            if (response.status === 'Failure') {
                console.log('Login failed: ');
                console.log(response.message);
                $('#responseMessageDivForLogin').show();
                $('.messageForLogin').removeClass('text-success');
                $('.messageForLogin').addClass('text-danger');
                $('.messageForLogin').text(response.message);
                setTimeout(() => {
                    $('#responseMessageDivForLogin').hide();
                }, 2500); // 👈️ time in milliseconds
            }
            else {
                // If user is authenticated redirect to homepage
                window.location.replace('./homepage.html?userId=' + response.data.id);
            }

        },
        error: function (error) {
            console.log('Error: ' + error.message);
        }
    });
}