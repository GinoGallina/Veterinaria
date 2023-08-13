let hoy = new Date();
let dd = String(hoy.getDate()).padStart(2, '0');
let mm = String(hoy.getMonth() + 1).padStart(2, '0');
let yyyy = hoy.getFullYear();
hoy = yyyy + '-' + mm + '/' + dd;
moment.locale('es');

$.urlParam = function (name) {
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    return results[1] || 0;
}

$('#txtNacimiento').daterangepicker({
    "autoApply": true,
    "locale": {
 		"format": "YYYY-MM-DD",
        "separator": " - ",
        "applyLabel": "Aplicar",
        "cancelLabel": "Cancelar",
        "fromLabel": "Hasta",
        "toLabel": "Desde",
    },
    
    singleDatePicker: true,     
    maxDate: hoy,
    opens: 'right',
    /*isInvalidDate: function (date) {
        if (date.day() == 1 ||  date.day() == 2  || date.day() == 3 ||  date.day() == 4 ||  date.day() == 5)
            return false;
        return true;
    }*/
})

listarMascotas();
listarRazas();

function listarRazas() {
    	$.ajax({
		url : 'Razas',
		method: 'get',
		success : function(data) {
			let control = $("#comboRazas");
			let contenido = "";
	        contenido += "<option value='' disabled >--Seleccione una raza--</option>";
	        for (let i = 0; i < data.length; i++) {
	            contenido += "<option value='" + data[i]["id"] + "'>";
	            contenido += data[i]["descripcion"];
	            contenido += "</option>";
	        }
	        control.html(contenido);
		}
	});
}

function listarMascotas() {
	$.ajax({
		url : 'Mascotas/cliente',
		method: 'get',
		data : {
			id : $.urlParam('id'),
		},
		success : function(data) {
			let contenedor = $("#tarjetasMascotas");
			let tarjeta = "";
	        for (let i = 0; i < data.length; i++) {
	            tarjeta += "<div class='card me-4 col-3 mb-4' style='width: 18rem;'>";
	            tarjeta += "<div class='card-body d-flex flex-column'>"
	            tarjeta += "<h5 class='card-title'>Raza: " + data[i]["raza"]["descripcion"] + "</h5>"
	            tarjeta += "<h5 class='card-title'>Especie: " + data[i]["raza"]["especie"]["descripcion"] + "</h5>"
	            tarjeta += "<p class='card-text'>Nombre: " + data[i]["nombre"] + "</p>"
	            tarjeta += "<p class='card-text'>Fecha de nacimiento: " + data[i]["nacimiento"] + "</p>"
	            let sexo = "";
	            if (data[i]["sexo"] === 'm') {
					sexo = "Masculino";
				} else {
					sexo = "Femenino";
				}
	            tarjeta += "<p class='card-text'>Sexo: " + sexo + "</p>"
	            tarjeta += "<div class='d-flex justify-content-between mt-auto'>"
	            tarjeta += "<button class='btn btn-outline-dark' onclick='modalEditM(" + data[i]["id"] + ")' data-bs-toggle='modal' data-bs-target='#staticBackdropMascota' >Editar</button>"
	            tarjeta += "<button class='btn btn-danger' onclick='modalDeleteM(" + data[i]["id"] + ")' data-bs-toggle='modal' data-bs-target='#staticBackdropMascota' >Eliminar</button>"
	            tarjeta += "</div>"
	            tarjeta += "</div>"
	            tarjeta += "</div>"
	            tarjeta += "<br/>"
	            contenedor.html(tarjeta);
			}
		}
	});
}

jQuery('#btnAgregarMas').on('click', function () {
    limpiarCamposM();
    habilitarCampos();
    $("#staticBackdropLabelMascota").text("Agregar mascota");
    $("#txtIDCliente").val($.urlParam('id'));
    $("#comboCursos").val("0");
});

function completarCamposM(id) {
	$.ajax({
			url : 'Mascotas/'+id,
			method: 'get',
			data : {
				id : id,
			},
			success : function(data) {
        console.log(data)
    	        $("#txtIDM").val(data["id"]);
	        	$('#txtIDCliente').val(data['cliente']['id']);
	        	$('#txtNombreMascota').val(data['nombre']);
	        	$('#txtNacimiento').val(data['nacimiento']);
	        	$('#comboSexo').val(data['sexo']);
	        	$('#comboRazas').val(data['raza']['id']);
			}
		});
}

function modalEditM(id) {
    $("#staticBackdropLabelMascota").text("Editar mascota");
    limpiarCamposM();
    habilitarCampos();
    $("#btnAceptar").addClass("modificar");
    completarCamposM(id);
}

function modalDeleteM(id) {
    $("#staticBackdropLabelMascota").text("Eliminar mascota");
    limpiarCamposM();
    deshabilitarCampos();
    $("#btnAceptar").addClass("eliminar");
    completarCamposM(id);
}

function limpiarCamposM() {
    $(".limpiarCampo").val("");
    campos = $(".required");
    for (let i = 0; i < campos.length; i++) {
        $("#campo" + i).removeClass("error");
    }
    $("#btnAceptar").removeClass("eliminar");
    $("#btnAceptar").removeClass("modificar");
}

function campoRequiredM() {
    campos = $(".required");
    for (let i = 0; i < campos.length; i++) {
        if (campos[i].value == "") {
            $("#campo" + i).addClass("error");
            return false;
        } else {
            $("#campo" + i).removeClass("error");
            $("#campo" + i).removeClass("modificar");
        }
    }
    return true;
}

async function confirmarCambios() {
    if (campoRequiredM()) {
        let id = $("#txtIDM").val();
        let cliente = await completarCamposCliente($("#txtIDCliente").val());
        let raza = await completarCamposRaza($("#comboRazas").val());
        let nombre = $("#txtNombreMascota").val();
        let nacimiento = $("#txtNacimiento").val();
        let sexo = $("#comboSexo").val();
        let json = {
			"id": id,
			"cliente": cliente,
			"nombre": nombre,
			"nacimiento": nacimiento,
			"sexo": sexo,
			"raza": raza,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar la mascota?") == 1) {
				json["action"] = "delete";
                crudMascota(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudMascota(json);
        } 
         else {
			json["action"] = "save";
            crudMascota(json);
        }
    }
}

 function crudMascota(json) {
    let datos = JSON.stringify(json);
    let url = "Mascotas";
    let method = "POST";
    let message = "La mascota se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "El mascota se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El mascota se actualizó correctamente";
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
            alert("Ya existe una mascota con la misma ?");
        } else if (xhr.status === 404) {
            alert("No existe dicha mascota");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}

async function completarCamposRaza(id) {
  try {
    const data = await $.ajax({
      url : 'Razas/'+id,
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

async function completarCamposCliente(id) {
  try {
    const data = await $.ajax({
      url : 'Clientes/'+id,
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