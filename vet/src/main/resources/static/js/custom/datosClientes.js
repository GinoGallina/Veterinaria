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
});
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
    opens: 'right',
    /*isInvalidDate: function (date) {
        if (date.day() == 1 ||  date.day() == 2  || date.day() == 3 ||  date.day() == 4 ||  date.day() == 5)
            return false;
        return true;
    }*/
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
    opens: 'right',
    /*isInvalidDate: function (date) {
        if (date.day() == 1 ||  date.day() == 2  || date.day() == 3 ||  date.day() == 4 ||  date.day() == 5)
            return false;
        return true;
    }*/
})

function fillTable(item) {
    let content = `
        <tr data-id='${item.id}'>
            <td>${item.dni}</td>
            <td>${item.nombre}</td>
            <td>${item.apellido}</td>
            <td>${item.telefono}</td>
            <td>${item.direccion}</td>
            <td>${item.direccion}</td>
            <td class='d-flex flex-row justify-content-center'>
                <button type='button' class='btn btn-outline-info btn-rounded btn-sm mr-2' data-veterinario=${JSON.stringify(item)} onclick="edit(this.getAttribute('data-veterinario'))" data-bs-toggle="modal" data-bs-target="#modalCreateMascota"><i class="bi bi-pencil"></i></button>
                <button type='button' class='btn btn-danger btn-rounded btn-sm ml-2' data-id=${item.id} onclick="deleteObj(this.getAttribute('data-id'))"><i class='bi bi-trash3'></i></button>
            </td>
        </tr>`;
    $('#DataTable').DataTable().row.add($(content)).draw();
}

function removeFromTable(id) {
    $('#DataTable').DataTable().row(`[data-id="${id}"]`).remove().draw();
}

function deleteObj(id,tipo) {
	let title;
	if(tipo=='mascota'){
		title= "¿Seguro deseas eliminar esta mascota?";
		action="/Mascotas";
	}else{
		title= "¿Seguro deseas eliminar esta atención?";
		action="/Atenciones";
	}
    Swal.fire({
        title: title,
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Eliminar',
        buttonsStyling: false,
        customClass: {
            confirmButton: 'btn btn-danger waves-effect waves-light px-3 py-2',
            cancelButton: 'btn btn-default waves-effect waves-light px-3 py-2'
        }
    })
    .then((result) => {
        if (result.isConfirmed) {
            $("#form-delete input[name='id']").val(id);
						$("#form-delete").attr("action", action);
            sendForm("delete");
        }
    });
}

function sendForm(action) {
    let form = document.getElementById(`form-${action}`);
    let formData;
    if($(form).attr("action")=='/Mascotas'){
          formData = {
          id: $(form).find('[name="id"]').val(), // Ajusta el nombre del campo según tu formulario
          clienteID: $(form).find('[name="idCliente"]').val(), // Ajusta el nombre del campo según tu formulario
          nombre: $(form).find('[name="nombre"]').val(), // Ajusta el nombre del campo según tu formulario
          nacimiento: $(form).find('[name="nacimiento"]').val(), // Ajusta el nombre del campo según tu formulario
          sexo:$(form).find('[name="comboSexo"]').val(), // Ajusta el nombre del campo según tu formulario,
          razaID:$(form).find('[name="comboRazas"]').val(),
          // Otros campos del formulario aquí
        };
    }else{
          formData = {
          id: $(form).find('[name="id"]').val(), // Ajusta el nombre del campo según tu formulario
          veterinario: $(form).find('[name="comboVeterinarios"]').val(), // Ajusta el nombre del campo según tu formulario
          mascota: $(form).find('[name="comboMascotas"]').val(), // Ajusta el nombre del campo según tu formulario
          practicas: $(form).find('[name="comboPracticas"]').val(), // Ajusta el nombre del campo según tu formulario
          fechaAtencion: $(form).find('[name="atencion"]').val(), // Ajusta el nombre del campo según tu formulario
          fechaPago: $(form).find('[name="pago"]').val(), // Ajusta el nombre del campo según tu formulario
          // Otros campos del formulario aquí
        };

    }
    console.log(formData)
    // Enviar solicitud AJAX
    $.ajax({
        url: $(form).attr("action"), // Utiliza la ruta del formulario
        method: $(form).attr("method"), // Utiliza el método del formulario
        data: JSON.stringify(formData), // Utiliza los datos del formulario
        contentType: "application/json",
        success: function (response) {
            let data = JSON.parse(response.data);
            Swal.fire({
            icon: "success",
            title: response.message,
            confirmButtonColor: "#1e88e5",
            }).then((result)=>{
							if(result.isConfirmed){
								location.reload();
							}
						}
						);
        },
        error: function (errorThrown) {
            Swal.fire({
            icon: "error",
            title: errorThrown.responseJSON.title,
            text: errorThrown.responseJSON.message,
            confirmButtonColor: "#1e88e5",
            });
        },
    });
}

function editM(json) {
  let entity = JSON.parse(json);
  $("#formContainerM form input:not([type='hidden']").val("");
  $("input[name='id']").val(entity.id);
  $("input[name='id']").prop("disabled", false);
  
  $("#formContainerM form").attr("method", "PUT");
  $("#formContainerM form").attr("id", "form-editM");
  $("#btnSendModal").text("Confirmar");
	const formattedDate = moment(entity.nacimiento).format('YYYY-MM-DD');
	$("input[name='idCliente']").val(entity.cliente.id);
	$("input[name='idCliente']").prop("disabled", false);
	$("#modalTitleM").text("Editar Mascota");
	$("input[name='nombre']").val(entity.nombre);
	$("input[name='nacimiento']").val(formattedDate)
	$("select[name='comboRazas']").val(entity.raza.id);
	$("select[name='comboSexo']").val(entity.sexo);
    
  }
  


function editA(json) {
  let entity = JSON.parse(json);
  $("#formContainerA form input:not([type='hidden']").val("");
  $("input[name='id']").val(entity.id);
  $("input[name='id']").prop("disabled", false);
  
  $("#formContainerA form").attr("method", "PUT");
  $("#formContainerA form").attr("id", "form-editA");
  $("#btnSendModal").text("Confirmar");

	console.log(entity)
	const formattedDateTimeAtencion = moment(entity.fechaAtencion).format('YYYY-MM-DD || HH:mm:ss');
	let formattedDateTimePago='No hay fecha'; 
	if(entity.fechaPago!=null){
		formattedDateTimePago	=moment(entity.fechaPago).format('YYYY-MM-DD HH:mm:ss');
	} 
	$("#modalTitleA").text("Editar Atencion");
	$("select[name='comboVeterinarios']").val(entity.veterinario.id);
	$("select[name='comboMascotas']").val(entity.mascota.id);
	$("select[name='comboPracticas']").val('');
	$("select[name='precioPactado']").val('');
	$("input[name='atencion']").val(formattedDateTimeAtencion);
	$("input[name='pago']").val(formattedDateTimePago);
	
  
  

} 


$("#btnSendModalM").on("click", function () {
    if ($("#formContainerM form").attr('id') === 'form-createM') {
        sendForm("createM");
    } else if ($("#formContainerM form").attr('id') === 'form-editM') {
        sendForm("editM");
    }
});
$(" #btnSendModalA").on("click", function () {
    if ($("#formContainerA form").attr('id') === 'form-createA') {
        sendForm("createA");
    } else if ($("#formContainerA form").attr('id') === 'form-editA') {
        sendForm("editA");
    }
});


$("#btnAddMascota").on("click", function () {
    $("#modalTitleM").text("Agregar mascota");
    $("#formContainerM form").attr("method", "POST");
    $("#formContainerM form").attr("id", "form-createM");
    $("#formContainerM form input:not([type='hidden']").val("");
    $("input[name='id']").prop("disabled", true);
    $("input[name='id-cliente']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});

$("#btnAddAtencion").on("click", function () {
    $("#modalTitleA").text("Agregar atencion");
    $("#formContainerA form").attr("method", "POST");
    $("#formContainerA form").attr("id", "form-createA");
    $("#formContainerA form input:not([type='hidden']").val("");
    $("input[name='id']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});

$('#DataTable').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ cliente",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 cliente",
        "sInfoFiltered": "(filtrado de _MAX_ cliente en total)",
        "emptyTable": 'No hay cliente que coincidan con la búsqueda',
        "sLengthMenu": "Mostrar _MENU_ cliente",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
});

