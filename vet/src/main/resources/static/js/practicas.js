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

let header = ["id","Descripcion","Precio"];
listar();


function listar() {
    $.get("Practicas", function (data) {
    listadoPracticas(header, data);
    });
}

let precio;
async function listadoPracticas(arrayHeader, data) {
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
        contenido += "<td class='text-center'>" + data[i].id+ "</td>";
        contenido += "<td>" + data[i].descripcion + "</td>";
        precio= await findPrecio(data[i].id);
        contenido += "<td class='text-center'>$" + precio.valor+ "</td>";

        
        contenido += "<td class='d-flex justify-content-center'>";
        contenido += "<button class='btn btn-outline-success me-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
        contenido += "<button class='btn btn-outline-danger ms-4' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
        contenido += "</td>";
        contenido += "</tr>";
    
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-practica").html(contenido);
    
}


async function findPrecio(id){
    try{
        let precio = await $.ajax({
            type: 'GET',
            url: 'Precios',
            contentType: "application/json",
            data: {
                id:id
            }
        });
        return precio
    }catch(e){
        console.log(e);
    }
}

function completarCampos(id) {
	$.ajax({
			url : 'Practicas/'+id,
			method: 'get',
			data : {
				id : id,
			},
			success : function(data) {
			 $("#txtID").val(data['id']);
       		 $("#txtDescripcion").val(data['descripcion']);
       		 $("#txtPrecio").val(data.precio.valor);

			}
		});
}

function modalEdit(id) {
    $("#staticBackdropLabel").text("Editar practica");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
  $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar practica");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar practica");
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
        let descripcion = $("#txtDescripcion").val();
        let precio = $("#txtPrecio").val();
        let json = {
			"id": id,
			"descripcion": descripcion,
			"precio": precio,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar la practica?") == 1) {
				json["action"] = "delete";
                crudPractica(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudPractica(json);
        }
         else {
			json["action"] = "save";
            crudPractica(json);
        }
    }
}



function crudPractica(json) {
    let datosPractica = JSON.stringify({
        "id":json.id,
        "descripcion":json.descripcion,
    });
    let url = "Practicas";
    let method = "POST";
    let message = "La raza se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "La raza se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "La raza se actualizó correctamente";
    }
    console.log(json)
    $.ajax({
        type: method,
        url: url + "?precio=" + json.precio,
        contentType: "application/json",
        data: datosPractica
    })
    .done(function(data) {
        alert(message);
        location.reload();
    })
    .fail(function(xhr, status, error) {
        console.log(xhr, status, error);
        if (xhr.status === 400) {
            alert("Ya existe una  raza con la misma descripcion");
        } else if (xhr.status === 404) {
            alert("No existe dicha raza");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}