$('#txtAtencion').daterangepicker({
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
});

$('#txtPago').daterangepicker({
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
});

//Sacar esto creo
$("#txtIDuenio").val($.urlParam('id'));

listarAtenciones();
listarVeterinarios();
listarPracticas();
listarMascotasAtencion();

$.ajax({
	url : 'Clientes/'+$.urlParam('id'),
	method: 'get',
	data : {
		id : $.urlParam('id')
	},
	success : function(data) {
				$('#txtID').val(data['id']);
	        	$('#txtDNI').val(data['dni']);
	        	$('#txtNombre').val(data['nombre']);
	        	$('#txtApellido').val(data['apellido']);
	        	$('#txtDireccion').val(data['direccion']);
	        	$('#txtTelefono').val(data['telefono']);
	        	$('#txtEmail').val(data['user']['email']);
	        	$('#txtPassword').val(data['user']['password']);
	}
});

function listarAtenciones() {
	$.ajax({
		url : 'Atenciones/cliente',
		method: 'get',
		data : {
			id : $.urlParam('id'),
		},
		success : function(data) {
			let contenedor = $("#tarjetasAtenciones");
			let tarjeta = "";
	        for (let i = 0; i < data.length; i++) {
	            tarjeta += "<div class='card me-4 col-3 mb-4' style='width: 18rem;'>";
	            tarjeta += "<div class='card-body d-flex flex-column'>"
	            tarjeta += "<h5 class='card-text'>Nombre Animal: " + data[i]["mascota"]["nombre"] + "</h5>"
	          	tarjeta += "<p class='card-text'>Veterinario: " + data[i]["veterinario"]["apellido"] + ", " + data[i]["veterinario"]["nombre"] + "</p>"
				for (let n = 0; n < data[i]['practicas'].length; n++) {
	        		tarjeta += "<p class='card-text'>Practica: " + data[i]['practicas'][n]['descripcion'] + "</p>"
	        	}
	            tarjeta += "<p class='card-text'>Fecha de atencion: " + data[i]["fechaAtencion"] + "</p>"
	            tarjeta += "<p class='card-text'>Fecha de pago: " + data[i]["fechaPago"] + "</p>"
	            tarjeta += "<div class='d-flex justify-content-between mt-auto'>"
	            tarjeta += "<button class='btn btn-outline-dark' onclick='modalEditA(" + data[i]["id"] + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop' >Editar</button>"
	            tarjeta += "<button class='btn btn-danger' onclick='modalDeleteA(" + data[i]["id"] + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop' >Eliminar</button>"
	            tarjeta += "</div>"
	            tarjeta += "</div>"
	            tarjeta += "</div>"
	            tarjeta += "<br/>"
	            contenedor.html(tarjeta);
			}
		}
	});
}


function listarVeterinarios() {	
	$.ajax({
		url : 'Veterinarios',
		method: 'get',
		success : function(data) {
			let control = $("#comboVeterinarios");
			let contenido = "";
	        contenido += "<option value='' disabled >--Seleccione un veterinario--</option>";
	        for (let i = 0; i < data.length; i++) {
	            contenido += "<option value='" + data[i]["id"] + "'>";
	            contenido += data[i]["nombre"] + " " +data[i]["apellido"];
	            contenido += "</option>";
	        }
	        control.html(contenido);
		}
	});
}

function listarPracticas() {	
	$.ajax({
		url : 'Practicas',
		method: 'get',
		success : function(data) {
			let control = $("#comboPracticas");
			let contenido = "";
	        contenido += "<option value='' disabled >--Seleccione una o más practicas--</option>";
	        for (let i = 0; i < data.length; i++) {
	            contenido += "<option value='" + data[i]["id"] + "'>";
	            contenido += data[i]["descripcion"];
	            contenido += "</option>";
	        }
	        control.html(contenido);
		}
	});
}

function listarMascotasAtencion() {	
	$.ajax({
		url : 'Mascotas',
		method: 'get',
		data : {
		idCliente : $.urlParam('id'),
		},
		success : function(data) {
			let control = $("#comboAnimales");
			let contenido = "";
	        contenido += "<option value='' disabled >--Seleccione una mascota--</option>";
	        for (let i = 0; i < data.length; i++) {
	            contenido += "<option value='" + data[i]["id"] + "'>";
	            contenido += data[i]["nombre"];
	            contenido += "</option>";
	        }
	        control.html(contenido);
		}
	});
}

jQuery('#btnAgregarAt').on('click', function () {
    limpiarCamposA();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar atencion");
    $("#txtIDCliente").val($.urlParam('id'));
    $("#comboCursos").val("0");
});

function completarCamposA(id) {
	$.ajax({
			url : 'Atenciones/'+id,
			method: 'get',
			data : {
				id : id,
			},
			success : function(data) {
    	      $("#txtIDA").val(data["id"]);
	        	$('#comboVeterinarios').val(data['veterinario']['nombre']);
	        	$('#comboAnimales').val(data['mascota']['id']);
	        	let array = [];
	        	for (let i = 0; i < data['practicas'].length; i++) {
	        		array [i] = data['practicas'][i]['id'];
	        	}
	        	$('#comboPracticas').val(array);
	        	$('#txtAtencion').val(data['fechaAtencion']);
	        	$('#txtPago').val(data['fechaPago']);
			}
		});
}

function modalEditA(id) {
    $("#staticBackdropLabel").text("Editar atencion");
    limpiarCamposA();
    habilitarCampos();
		$("#btnAceptar").addClass("modificar");
    completarCamposA(id);
}

function modalDeleteA(id) {
    $("#staticBackdropLabel").text("Eliminar atencion");
    limpiarCamposA();
    deshabilitarCampos();
    $("#btnAceptar").addClass("eliminar");
    completarCamposA(id);
}

function limpiarCamposA() {
    $(".limpiarCampo").val("");
    campos = $(".required2");
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

function campoRequiredA() {
    campos = $(".required2");
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

async function confirmarCambiosAtenciones() {
    if (campoRequiredA()) {
        let id = $("#txtIDA").val();
        let veterinario = await completarCamposVeterinario($("#comboVeterinarios").val());
        let mascota = await completarCamposMascota($("#comboAnimales").val());
        let practicas = await completarCamposPracticas($("#comboPracticas").val());
        let atencion = $("#txtAtencion").val();
        let pago = $("#txtPago").val();
        let json = {
			"id": id,
			"veterinario": veterinario,
			"mascota": mascota,
			"practicas": practicas,
			"fechaAtencion": atencion,
			"fechaPago": pago,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar la atencion?") == 1) {
				json["action"] = "delete";
                crudAtenciones(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudAtenciones(json);
        } 
				 else {
			json["action"] = "save";
            crudAtenciones(json);
        }
    }
}

 function crudAtenciones(json) {
    let datos = JSON.stringify(json);
    let url = "Atenciones";
    let method = "POST";
    let message = "La atencion se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "El atencion se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El atencion se actualizó correctamente";
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
            alert("Ya existe una atencion con la misma ?");
        } else if (xhr.status === 404) {
            alert("No existe dicha atencion");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}


async function completarCamposVeterinario(id) {
  try {
    const data = await $.ajax({
      url : 'Veterinarios/'+id,
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

async function completarCamposMascota(id) {
  try {
    const data = await $.ajax({
      url : 'Mascotas/'+id,
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

async function completarCamposPracticas(listaIds) {
	lista= listaIds.join(',');
  try {
    const data = await $.ajax({
      url : 'Practicas/elegidas',
      method: 'get',
      data: {
        ids: lista,
      },
    });
    return data;
  } catch (error) {
    console.error('Error al obtener la data:', error);
    throw error;
  }
}