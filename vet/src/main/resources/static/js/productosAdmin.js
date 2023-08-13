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

let header = ["Descripcion","Stock", "Imagen", "Precio"];
listar();

function listar() {
    $.get("ProductosAdmin", function (data) {
    	listadoProductos(header, data);
    });
}

function listadoProductos(arrayHeader, data) {
    console.log(data)
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
        contenido += "<td>" + data[i].stock + "</td>";
        let src = "data:image/png;base64,"+ data[i].img;
        contenido += "<td> <img id='imagen' src='"+src+"' alt='Imagen'>" + "</td>";
        //document.body.appendChild(image)
        contenido += "<td>$" + data[i].precio + "</td>";
        contenido += "<td class='d-flex justify-content-center'>";
        contenido += "<button class='btn btn-outline-success me-4' onclick='modalEdit(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-pencil-square'></i></button>";
        contenido += "<button class='btn btn-outline-danger ms-4' onclick='modalDelete(" + data[i].id + ")' data-bs-toggle='modal' data-bs-target='#staticBackdrop'><i class='bi bi-trash3'></i></button>";
        contenido += "</td>";
        contenido += "</tr>";
    }
    contenido += "</tbody>";
    contenido += "</table>";
    $("#tabla-producto").html(contenido);
    
}

function completarCampos(id) {
	$.ajax({
			url : 'ProductosAdmin/'+id,
			method: 'get',
			data : {
				id : id,
				pagina: 'Productos',
			},
			success : function(data) {
	        	$('#txtDescripcion').val(data['descripcion']);
		        $('#txtID').val(data['id']);
        		$("#txtStock").val(data['stock']);
         		$("#txtTipo").val(data['tipo']);
         		$("#txtPrecio").val(data['precio']);
			}
		});
}

function modalEdit(id) {
    $("#staticBackdropLabel").text("Editar producto");
    limpiarCampos();
    habilitarCampos();
	completarCampos(id);
  $("#btnAceptar").addClass("modificar");
}

function modalDelete(id) {
    $("#staticBackdropLabel").text("Eliminar producto");
    limpiarCampos();
    deshabilitarCampos();
	completarCampos(id);
	$("#btnAceptar").addClass("eliminar");
}

jQuery('#btnAgregar').on('click', function () {
    limpiarCampos();
    habilitarCampos();
    $("#staticBackdropLabel").text("Agregar producto");
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
        let stock = $("#txtStock").val();
        let tipo = $("#txtTipo").val();
		    let precio = $("#txtPrecio").val();
        let json = {
			"id": id,
			"descripcion": descripcion,
			"stock": stock,
			"tipo": tipo,
			"precio": precio,
			"action": ""
		};
        if ($("#btnAceptar").hasClass("eliminar")) {
            if (confirm("Seguro que desea eliminar el producto?") == 1) {
				json["action"] = "delete";
                crudProducto(json);
            }
        }else if($("#btnAceptar").hasClass("modificar")){
			      json["action"] = "update";
            crudProducto(json);
        }
         else {
			json["action"] = "save";
            crudProducto(json);
        }
    }
}

function crudProducto(json) {
  console.log(datos)
    let url = "ProductosAdmin";
    let method = "POST";
    let message = "El producto se guardo correctamente";

    if (json.action == "delete") {
        method = "DELETE";
        message = "El producto se eliminó correctamente";
    } else if (json.action == "update") {
        method = "PUT";
        message = "El producto se actualizó correctamente";
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
            alert("Ya existe un prodcto con la misma descripcion");
        } else if (xhr.status === 404) {
            alert("No existe dicho prodcto");
        } else {
            alert("Error al procesar la solicitud");
        }
    });
}
// async function completarCamposPrecio(id) {
//   try {
//     const data = await $.ajax({
//       url : 'Precios/'+id,
//       method: 'get',
//       data: {
//         id: id,
//       },
//     });
//     return data;
//   } catch (error) {
//     console.error('Error al obtener la data:', error);
//     throw error;
//   }
// }