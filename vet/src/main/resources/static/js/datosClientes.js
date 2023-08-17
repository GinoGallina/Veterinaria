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
    maxDate: hoy,
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
    maxDate: hoy,
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

function deleteObj(id) {
    Swal.fire({
        title: "¿Seguro deseas eliminar esta veterinario?",
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
            sendForm("delete");
        }
    });
}

function sendForm(action) {
    let form = document.getElementById(`form-${action}`);
    let formData;
    if($(form).attr("action")=='Mascotas'){
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
          dni: $(form).find('[name="dni"]').val(), // Ajusta el nombre del campo según tu formulario
          nombre: $(form).find('[name="nombre"]').val(), // Ajusta el nombre del campo según tu formulario
          apellido: $(form).find('[name="apellido"]').val(), // Ajusta el nombre del campo según tu formulario
          direccion: $(form).find('[name="direccion"]').val(), // Ajusta el nombre del campo según tu formulario
          telefono: $(form).find('[name="telefono"]').val(), // Ajusta el nombre del campo según tu formulario
          email:$(form).find('[name="email"]').val(), // Ajusta el nombre del campo según tu formulario,
          password:$(form).find('[name="password"]').val(),
          // Otros campos del formulario aquí
        };

    }
    console.log(formData)
    // Enviar solicitud AJAX
    $.ajax({
        url: "/"+$(form).attr("action"), // Utiliza la ruta del formulario
        method: $(form).attr("method"), // Utiliza el método del formulario
        data: JSON.stringify(formData), // Utiliza los datos del formulario
        contentType: "application/json",
        success: function (response) {
            let data = JSON.parse(response.data);
            Swal.fire({
            icon: "success",
            title: response.message,
            confirmButtonColor: "#1e88e5",
            });
            $("#btnCloseModalCreate").click();
            if (action === "create") {
                fillTable(data);
            } else if (action === "edit") {
                fillTable(data);
                removeFromTable(data.id);
            } else {
                removeFromTable(data.id);
            }
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

function edit(json,tipo) {
  let entity = JSON.parse(json);
  console.log(entity)
  $("#formContainer form input:not([type='hidden']").val("");
  $("input[name='id']").val(entity.id);
  $("input[name='id']").prop("disabled", false);
  
  $("#formContainer form").attr("method", "PUT");
  $("#formContainer form").attr("id", "form-edit");
  $("#btnSendModal").text("Confirmar");
  if(tipo=='mascota'){
    $("input[name='idCliente']").val(entity.cliente.id);
    $("input[name='idCliente']").prop("disabled", false);
    $("#modalTitle").text("Editar Mascota");
    $("input[name='nombre']").val(entity.nombre);
    $("input[name='nacimiento']").val(entity.nacimiento);
    $("select[name='comboRazas']").val(entity.raza.id);
    $("select[name='comboSexo']").val(entity.sexo);
    
  }else if(tipo=='atencion'){
    console.log(entity)
    $("#modalTitle").text("Editar Atencion");
    $("select[name='comboVeterinarios']").val(entity.veterinario.id);
    $("select[name='comboMascotas']").val(entity.mascota.id);
    $("select[name='comboPracticas']").val('');
    $("select[name='precioPactado']").val('');
    $("input[name='atencion']").val(entity.fechaAtencion);
    $("input[name='pago']").val(entity.fechaPago);
    
  }
  

} 


$("#btnSendModal").on("click", function () {
    if ($("#formContainer form").attr('id') === 'form-create') {
        sendForm("create");
    } else if ($("#formContainer form").attr('id') === 'form-edit') {
        sendForm("edit");
    }
});
$("#btnAddMascota").on("click", function () {
    $("#modalTitle").text("Agregar mascota");
    $("#formContainer form").attr("method", "POST");
    $("#formContainer form").attr("id", "form-create");
    $("#formContainer form input:not([type='hidden']").val("");
    $("input[name='id']").prop("disabled", true);
    $("input[name='id-cliente']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});
$("#btnAddAtencion").on("click", function () {
    $("#modalTitle").text("Agregar veterinario");
    $("#formContainer form").attr("method", "POST");
    $("#formContainer form").attr("id", "form-create");
    $("#formContainer form input:not([type='hidden']").val("");
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

function selectPersona(id) {
    window.location.href = "Clientes/datos?id=" + id;
}