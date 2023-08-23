$.ajaxSetup({
    beforeSend: function (xhr) {
        //Obtener el token de localStorage
        const token = localStorage.getItem('token');
        if (token) {
            //Agregar el token en el encabezado Authorization con el formato Bearer
            xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        }
    }
});


$("#login-btn").on("click", function () {
    email = document.getElementById("email").value;
    password = document.getElementById("password").value;
    let json = {
        "email": email,
        "password": password,
    }
    let datos = JSON.stringify(json);
    $.ajax({
        type: "POST",
        url: "/auth/login",
        contentType: "application/json",
        data: datos,
        success: function (response) {
            localStorage.removeItem('token');
            localStorage.setItem('token', data.token);
        },
        error: function (errorThrown) {
            console.log(errorThrown);
            Swal.fire({
                icon: 'error',
                title: errorThrown.responseJSON.title,
                text: errorThrown.responseJSON.message,
                confirmButtonColor: '#1e88e5',
            });
        }
    });
});
