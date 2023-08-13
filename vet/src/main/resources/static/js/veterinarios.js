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

let header = ["Matricula","Nombre","Apellido","Telefono","Direccion","Usuario","Password","Rol"];
listar();

function listar() {
    $.get("Veterinarios", function (data) {
    listadoVeterinarios(header, data);
    });
}

function listadoVeterinarios(arrayHeader, data) {
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
    	contenido += "<td class='text-center'>" + data[i].matricula + "</td>";
    	contenido += "<td>" + data[i].nombre + "</td>";
    	contenido += "<td>" + data[i].apellido + "</td>";
    	contenido += "<td>" + data[i].telefono + "</td>";
    	contenido += "<td>" + data[i].direccion + "</td>";
    	contenido += "<td>" + data[i].user.email + "</td>";
    	contenido += "<td>" + data[i].user.password + "</td>";
    	contenido += "<td>" + data[i].user.rol.descripcion + "</td>";
        
   	 	contenido += "<td class='d-flex justify-content-center'>";
    	contenido += "<button class='btn btn-outline-success me-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
    	contenido += "<button class='btn btn-outline-danger ms-4' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
    	contenido += "</td>";
    	contenido += "</tr>";
    
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-veterinario").html(contenido);
    
}

function completarCampos(id) {
    //AGREGO ID A LA URL
	$.ajax({
			url : 'Veterinarios/' + id,
			method: 'get',
			  data : {
			  	id : id,
			  },
			success : function(data) {
			 $("#txtID").val(data['id']);
       		 $("#txtMatricula").val(data['matricula']);
       	 	 $("#txtNombre").val(data['nombre']);
        	 $("#txtApellido").val(data['apellido']);
        	 $("#txtTelefono").val(data['telefono']);
        	 $("#txtEmail").val(data['user']['email']);
        	 $("#txtPassword").val(data['user']['password']);
         	 $("#txtDireccion").val(data['direccion']);

			}
		});
}

function modalEdit(id) {
    $("#staticBackdropLabel").text("Editar veterinario");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
    $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar veterinario");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar veterinario");
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
        let matricula = $("#txtMatricula").val();
        let nombre = $("#txtNombre").val();
        let apellido = $("#txtApellido").val();
        let telefono = $("#txtTelefono").val();
        let email = $("#txtEmail").val();
        let password = $("#txtPassword").val();
        let direccion = $("#txtDireccion").val();
        let json = {
			"id": id,
			"matricula": matricula,
			"nombre": nombre,
			"apellido": apellido,
			"telefono": telefono,
			"email": email,
			"password": password,
			"direccion": direccion,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar el veterinario?") == 1) {
				json["action"] = "delete";
                crudVeterinario(json);
            }
        } else if($("#btnAceptar").hasClass("modificar")){
			json["action"] = "update";
            crudVeterinario(json);
        }else{
			json["action"] = "save";
            crudVeterinario(json);
        }
    }
}



async function crudVeterinario(json) {
    //let datos = JSON.stringify(json);
    let datosUser = JSON.stringify({
        "email": json.email,
        "password": json.password,
        "rol":"USER"
    });

    let url = "Veterinarios";
    let urlUser;
    let method;
    let message;
    let user;

    if (json.action == "save") {
        urlUser = "/auth/nuevo";
        method = "POST";
        message = "El veterinario se guardo correctamente";
        user = await crudUser(method,urlUser,datosUser);
        console.log(user);
    }
    else if (json.action == "delete") {
        method = "DELETE";
        message = "El veterinario se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El veterinario se actualizó correctamente";
    }

    
    let datosVet = JSON.stringify({
	    "id": json.id,
	    "matricula": json.matricula,
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
            alert("Ya existe un veterinario con la misma matrícula");
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
