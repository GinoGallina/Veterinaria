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





function completarCampos(id) {
	$.ajax({
			url : 'Especies/'+id,
			method: 'get',
			data : {
				id : id,
			},
			success : function(data) {
		        $('#txtID').val(data['id']);
	        	$('#txtDescripcion').val(data['descripcion']);
			}
		});
}

function modalEdit(data) {
    //datos= JSON.parse(data);
    console.log(data);
    console.log(datos);
    $("#staticBackdropLabel").text("Editar especie");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
  $("#btnAceptar").addClass("modificar");
}

function modalDelete(data) {
    //datos= JSON.parse(data);
    console.log(data);
    console.log(datos);
    $("#staticBackdropLabel").text("Eliminar especie");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar especie");
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
        let json = {
			"id": id,
			"descripcion": descripcion,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar la especie?") == 1) {
				json["action"] = "delete";
                crudEspecie(json);
            }
        } else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudEspecie(json);
        }
         else {
			json["action"] = "save";
            crudEspecie(json);
        }
    }
}

// function crudEspecie(json) {
//     let datos= JSON.stringify(json);
//     if(json.action=="save"){
//     $.ajax({	
//          type: "POST",
//          url: "Especies",
//          contentType: "application/json",
//          //dataType: 'json',
//          data: datos,
//          success: function (data) {
//             alert("La especie se guardo correctamente");
//             location.reload();
//          },
//          error: function (xhr, status, error) {
//             // Ocurrió un error en la solicitud
//             console.log(xhr,status,error)
//             if (xhr.status === 400) {
//                 // Si la respuesta es un código 400, significa que ya existe un especie con la misma matrícula
//                 alert("Ya existe una especie con la misma descripcion");
//             } else {
//                 // Si se produce otro error, muestra un mensaje genérico
//                 alert("Error al guardar la especie");
//             }
//         }
//           });
//     }else if(json.action=="delete"){

//     $.ajax({	
//         type: "DELETE",
//         url: "Especies",
//         data : {
// 			  	id : json.id,
// 			  },
//          success: function (data) {
//             alert("La especie se eliminó correctamente");
//             location.reload();
//          },
//          error: function (xhr, status, error) {
//             // Ocurrió un error en la solicitud
//             console.log(xhr,status,error)
//             if (xhr.status === 404) {
//                 alert("No existe dicha especie");
//             } else {
//                 // Si se produce otro error, muestra un mensaje genérico
//                 alert("Error al eliminar la especie");
//             }
//         }
//           });
//     }else{
//     $.ajax({	
//          type: "PUT",
//          url: "Especies",
//          contentType: "application/json",
//          //dataType: 'json',
//          data: datos,
//          success: function (data) {
//             alert("La especie se actualizó correctamente");
//             location.reload();
//          },
//          error: function (xhr, status, error) {
//             // Ocurrió un error en la solicitud
//             console.log(xhr,status,error)
//             if (xhr.status === 400) {
//                 alert("Ya existe una especie con la misma descripción");
//             } else {
//                 // Si se produce otro error, muestra un mensaje genérico
//                 alert("Error al guardar la especie");
//             }
//         }
//           });   
//     }
   
//  }

 function crudEspecie(json) {
    let datos = JSON.stringify(json);
    let url = "Especies";
    let method = "POST";
    let message = "La especie se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "El especie se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El especie se actualizó correctamente";
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
            alert("Ya existe una especie con la misma desc");
        } else if (xhr.status === 404) {
            alert("No existe dicha especie");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}