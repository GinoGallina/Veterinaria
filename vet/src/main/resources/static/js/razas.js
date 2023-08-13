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

let header = ["Descripcion", "Especie"];
listar();
listadoEspecies();

function listar() {
    $.get("Razas", function (data) {
        listadoRazas(header, data);
    });
}

function listadoRazas(arrayHeader, data) {
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
        contenido += "<td>" + data[i].descripcion + "</td>";
        contenido += "<td>" + data[i].especie.descripcion + "</td>";
        contenido += "<td class='d-flex justify-content-center'>";
        contenido += "<button class='btn btn-outline-success me-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
        contenido += "<button class='btn btn-outline-danger ms-4' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
        contenido += "</td>";
        contenido += "</tr>";
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-razas").html(contenido);
    
}
	
function listadoEspecies() {	
	$.ajax({
		url : 'Especies',
		method: 'get',
		success : function(data) {
			let control = $("#comboEspecies");
			let contenido = "";
	        contenido += "<option value='' disabled >--Seleccione una especie--</option>";
	        for (let i = 0; i < data.length; i++) {
	            contenido += "<option value='" + data[i]["id"] + "'>";
	            contenido += data[i]["descripcion"];
	            contenido += "</option>";
	        }
	        control.html(contenido);
		}
	});
}


function completarCampos(id) {
	$.ajax({
			url : 'Razas/'+id,
			method: 'get',
			data : {
				id : id,
			},
			success : function(data) {
		        $('#txtID').val(data['id']);
	        	$('#txtDescripcion').val(data['descripcion']);
        		$("#comboEspecies option[value = " + data['especie']['id'] + "]").attr('selected', 'selected');
			}
		});
}

function modalEdit(id) {
  $("#staticBackdropLabel").text("Editar raza");
  limpiarCampos();
  habilitarCampos();
	completarCampos(id);
  $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar raza");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar raza");
});

function limpiarCampos() {
    $(".limpiarCampo").val("");
    campos = $(".required");
    for (let i = 0; i < campos.length; i++) {
        $("#campo" + i).removeClass("error");
    }
    $("#btnAceptar").removeClass("eliminar");
    $("#btnAceptar").removeClass("modificar");
    $("#comboEspecies option").removeAttr('selected')
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

async function confirmarCambios() {
    if (campoRequired()) {
        let id = $("#txtID").val();
        let descripcion = $("#txtDescripcion").val();
        let especie = await completarCamposEspecie($("#comboEspecies").val());
        let json = {
			"id": id,
			"descripcion": descripcion,
			"especie": especie,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar la raza?") == 1) {
				json["action"] = "delete";
                crudRaza(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudRaza(json);
        }
         else {
			json["action"] = "save";
            crudRaza(json);
        }
    }
}

function crudRaza(json) {
    let datos = JSON.stringify(json);
    let url = "Razas";
    let method = "POST";
    let message = "La raza se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "La raza se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "La raza se actualizó correctamente";
    }
    console.log(datos)
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
            alert("Ya existe una  raza con la misma descripcion");
        } else if (xhr.status === 404) {
            alert("No existe dicha raza");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}
async function completarCamposEspecie(id) {
  try {
    const data = await $.ajax({
      url : 'Especies/'+id,
      method: 'get',
      data: {
        id: id,
      },
    });
    return data;
  } catch (error) {
    console.error('Error al obtener la data:', error);
    throw error;
  }
}