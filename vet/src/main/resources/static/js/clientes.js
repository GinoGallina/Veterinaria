$.ajaxSetup({
    beforeSend: function (xhr) {
        // Obtener el token de localStorage
        const token = localStorage.getItem('token');
        if (token) {
            // Agregar el token en el encabezado Authorization con el formato Bearer
            xhr.setRequestHeader('Authorization', 'Bearer ' + token);
        }
    }
});


let header = ["ID","Dni", "Nombre","Apellido","Direccion","Telefono","Email","Password"];
listar();

function listar() {
    $.get("Clientes", function (data) {
        listadoClientes(header, data);
    });
}

function listadoClientes(arrayHeader, data) {
    let contenido = "";
    contenido += "<table id='tabla-generic' class='table table-oscura table-striped table-bordered table-hover'>";
    contenido += "<thead>";
    contenido += "<tr class='fw-bold'>";
    for (let i = 0; i < arrayHeader.length; i++) {
        contenido += "<td class='text-center'>";
        contenido += arrayHeader[i];
        contenido += "</td>";
    }
    contenido += "<td class='no-sort text-center'>Accion</td>";
    contenido += "</tr>";
    contenido += "</thead>";
    contenido += "<tbody>";
    for (let i = 0; i < data.length; i++) {
        contenido += "<tr>";
      	contenido += "<td class='text-center'>" + data[i].id + "</td>";
        contenido += "<td class='text-center'>" + data[i].dni + "</td>";
        contenido += "<td>" + data[i].nombre + "</td>";
        contenido += "<td>" + data[i].apellido + "</td>";
        contenido += "<td>" + data[i].direccion + "</td>";
        contenido += "<td>" + data[i].telefono + "</td>";
        contenido += "<td>" + data[i].user.email + "</td>";
        contenido += "<td>" + data[i].user.password + "</td>";
        contenido += "<td class='d-flex justify-content-between'>";
        contenido += "<button class='btn btn-outline-success ms-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
        contenido += "<button class='btn btn-outline-danger' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
        contenido += "<button class='btn btn-outline-light me-4' onclick='selectPersona(" + data[i].id + ")'>Seleccionar</button>";
        contenido += "</td>";
        contenido += "</tr>";
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-cliente").html(contenido);
    
}

function completarCampos(id) {
	$.ajax({
			url : 'Clientes/'+id,
			method: 'get',
			data : {
				id : id,
				//pagina: 'Especies',
			},
			success : function(data) {
				$('#txtID').val(data['id']);
	        	$('#txtDni').val(data['dni']);
	        	$('#txtNombre').val(data['nombre']);
	        	$('#txtApellido').val(data['apellido']);
	        	$('#txtDireccion').val(data['direccion']);
	        	$('#txtTelefono').val(data['telefono']);
	        	$('#txtEmail').val(data['user']['email']);
	        	$('#txtPassword').val(data['user']['password']);
			}
		});
}


function modalEdit(id) {
    $("#staticBackdropLabel").text("Editar cliente");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
  $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar cliente");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar cliente");
});

function limpiarCampos() {
    $(".limpiarCampo").val("");
    campos = $(".required");
    for (let i = 0; i < campos.length; i++) {
        $("#campo" + i).removeClass("error");
    }
    $("#btnAceptar").removeClass("eliminar");
    $("#btnAceptar").removeClass("modificar");
}

function habilitarCampos() {
    $(".habilitarCampo").removeAttr("disabled");
}

function deshabilitarCampos() {
    $(".deshabilitarCampo").attr("disabled", "disabled");
}

function campoRequired() {
    campos = $(".required");
    for (let i = 0; i < campos.length; i++) {
        if (campos[i].value == "") {
            $("#campo" + i).addClass("error");
            return false;
        } else {
            $("#campo" + i).removeClass("error");
        }
    }
    return true;
}

function confirmarCambios() {
    if (campoRequired()) {
        let id = $("#txtID").val();
        let dni = $("#txtDni").val();
        let nombre = $("#txtNombre").val();
        let apellido = $("#txtApellido").val();
        let direccion = $("#txtDireccion").val();
        let telefono= $("#txtTelefono").val();
        let email = $("#txtEmail").val();
        let password = $("#txtPassword").val();
        let json = {
			"id": id,
			"dni": dni,
			"nombre": nombre,
			"apellido": apellido,
			"direccion": direccion,
			"telefono": telefono,
			"email": email,
			"password": password,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar el cliente?") == 1) {
				json["action"] = "delete";
                crudCliente(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudCliente(json);
        } 
        else {
			json["action"] = "save";
            crudCliente(json);
        }
    }
}

// function crudCliente(json) {
//     $.ajax({
//         type: "POST",
//         url: "Clientes",
//         dataType: 'json',
//         data: json,
//         success: function (data) {
//             if (data == 1) {
//                 if ($("#btnAceptar").hasClass("eliminar")) {
//                     alert("El cliente se elimino correctamente");
//                 } else {
//                     alert("El cliente se guardo correctamente");
//                 }
//          		location.reload();
//             } else if ( data == -1) {
//                 alert("El cliente ingresado ya existe");
//             } else {
//                 alert("Los cambios no se guardaron. Error en la base de datos");
//             }
//         }
//     });
// }

async function crudCliente(json) {
    //let datos = JSON.stringify(json);
    let datosUser = JSON.stringify({
        "email": json.email,
        "password": json.password,
        "rol":"USER"
    });

    let url = "Clientes";
    let urlUser;
    let method;
    let message;
    let user;

    if (json.action == "save") {
        urlUser = "/auth/nuevo";
        method = "POST";
        message = "El cliente se guardo correctamente";
        user = await crudUser(method,urlUser,datosUser);
        console.log(user);
    }
    else if (json.action == "delete") {
        method = "DELETE";
        message = "El cliente se eliminó correctamente";
    } else if (json.action == "update") {
        urlUser = "/auth/update";
        method = "PUT";
        message = "El cliente se actualizó correctamente";
        user = await crudUser(method,urlUser,datosUser);
    }

    
    let datosVet = JSON.stringify({
	    "id": json.id,
	    "dni": json.dni,
	    "nombre": json.nombre,
	    "apellido": json.apellido,
	    "telefono": json.telefono,
      "direccion": json.direccion,
      "user":user
    });
    console.log(datosVet)
    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: datosVet
    })
    .done(function(data) {
        alert(message);
        location.reload();
    })
    .fail(function(xhr, status, error) {
        console.log(xhr, status, error);
        if (xhr.status === 400) {
            alert("Ya existe un cliente con el mismo dni");
        } else if (xhr.status === 404) {
            alert("No existe dicho veterinario");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}


async function crudUser(method,url,datos) {
    console.log(datos)
    try{
        const data= await $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: datos
    });
    alert("Usuario creado/modificado correctamente");
    return data;
    }catch(error){
    console.error('Error al obtener la data:', error);
    throw error;
    }
  }





function selectPersona(id) {
    window.location.href = "/datosCliente?id=" + id;
}