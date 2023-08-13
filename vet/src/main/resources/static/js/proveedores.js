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

let header = ["Cuil","Razon Social","Telefono","Direccion","Email"];
listar();

function listar() {
    $.get("Proveedores", function (data) {
    listadoProveedores(header, data);
    });
}

function listadoProveedores(arrayHeader, data) {
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
    	contenido += "<td class='text-center'>" + data[i].cuil + "</td>";
    	contenido += "<td>" + data[i].razonSocial + "</td>";
    	contenido += "<td>" + data[i].telefono + "</td>";
    	contenido += "<td>" + data[i].direccion + "</td>";
    	contenido += "<td>" + data[i].email + "</td>";
        
   	 	contenido += "<td class='d-flex justify-content-center'>";
    	contenido += "<button class='btn btn-outline-success me-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
    	contenido += "<button class='btn btn-outline-danger ms-4' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
    	contenido += "</td>";
    	contenido += "</tr>";
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-proveedores").html(contenido);
    
}

function completarCampos(id) {
    //AGREGO ID A LA URL
    console.log('Proveedores/' + id)
	$.ajax({
			url : 'Proveedores/' + id,
			method: 'get',
			  data : {
			  	id : id,
			  },
			success : function(data) {
        console.log(data)
			 $("#txtID").val(data['id']);
       		 $("#txtCuil").val(data['cuil']);
       	 	 $("#txtRazonSocial").val(data['razonSocial']);
        	 $("#txtTelefono").val(data['telefono']);
        	 $("#txtEmail").val(data['email']);
         	 $("#txtDireccion").val(data['direccion']);

			}
		});
}

function modalEdit(id) {
    $("#staticBackdropLabel").text("Editar Proveedor");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
    $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar Proveedor");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar Proveedor");
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
        let cuil = $("#txtCuil").val();
        let razonSocial = $("#txtRazonSocial").val();
        let telefono = $("#txtTelefono").val();
        let email = $("#txtEmail").val();
        let direccion = $("#txtDireccion").val();
        let json = {
			"id": id,
			"cuil": cuil,
			"razonSocial": razonSocial,
			"telefono": telefono,
			"email": email,
			"direccion": direccion,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar el Proveedor?") == 1) {
				json["action"] = "delete";
                crudProveedor(json);
            }
        } else if($("#btnAceptar").hasClass("modificar")){
			json["action"] = "update";
            crudProveedor(json);
        }else{
			json["action"] = "save";
            crudProveedor(json);
        }
    }
}




 function crudProveedor(json) {
    let datos = JSON.stringify(json);
    let url = "Proveedores";
    let method = "POST";
    let message = "El Proveedor se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "El Proveedor se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El Proveedor se actualizó correctamente";
    }

    $.ajax({
        type: method,
        url: url,
        contentType: "application/json",
        data: datos
    })
    .done(function(data) {
        alert(message);
        location.reload();
    })
    .fail(function(xhr, status, error) {
        console.log(xhr, status, error);
        if (xhr.status === 400) {
            alert("Ya existe un Proveedor con el mismo cuil o email");
        } else if (xhr.status === 404) {
            alert("No existe dicho Proveedor");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}