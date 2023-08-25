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


function fillTable(item) {
    let content = `
        <tr data-id='${item.id}'>
            <td>${item.descripcion}</td>
            <td>${item.ultimoPrecio.valor}</td>
            <td>${item.ultimoPrecio.createdAt}</td>
            <td>${item.updatedAt}</td>
            <td class='d-flex flex-row justify-content-center'>
                <button type='button' class='btn btn-outline-info btn-rounded btn-sm mr-2' data-practica=${JSON.stringify(item)} onclick="edit(this.getAttribute('data-practica'))" data-bs-toggle="modal" data-bs-target="#modalCreate"><i class="bi bi-pencil"></i></button>
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
        title: "¿Seguro deseas eliminar esta practica?",
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

    let formData = {
      id: $(form).find('[name="id"]').val(), // Ajusta el nombre del campo según tu formulario
      descripcion: $(form).find('[name="descripcion"]').val(), // Ajusta el nombre del campo según tu formulario
      precio: $(form).find('[name="precio"]').val(), // Ajusta el nombre del campo según tu formulario
      // Otros campos del formulario aquí
    };
    // Enviar solicitud AJAX
    console.log(formData)
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
            let errorMessage = errorThrown.responseJSON.messages.join("<br>");
            Swal.fire({
            icon: "error",
            title: errorThrown.responseJSON.title,
            html: errorMessage,
            confirmButtonColor: "#1e88e5",
            });
        },
    });
}

function edit(json) {
    let entity = JSON.parse(json);
    console.log(entity)
    $("#formContainer form input:not([type='hidden']").val("");
    $("input[name='id']").val(entity.ID);
    $("input[name='id']").prop("disabled", false);

    $("#modalTitle").text("Editar practica");
    $("#formContainer form").attr("method", "PUT");
    $("#formContainer form").attr("id", "form-edit");
    $("#btnSendModal").text("Confirmar");

    $("input[name='descripcion']").val(entity.descripcion);
    $("input[name='precio']").val(entity.ultimoPrecio.valor);
} 

$("#btnSendModal").on("click", function () {
    if ($("#formContainer form").attr('id') === 'form-create') {
        sendForm("create");
    } else if ($("#formContainer form").attr('id') === 'form-edit') {
        sendForm("edit");
    }
});
$("#btnAdd").on("click", function () {
    $("#modalTitle").text("Agregar practica");
    $("#formContainer form").attr("method", "POST");
    $("#formContainer form").attr("id", "form-create");
    $("#formContainer form input:not([type='hidden']").val("");
    $("input[name='id']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});

$('#DataTable').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ practicas",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 practicas",
        "sInfoFiltered": "(filtrado de _MAX_ practicas en total)",
        "emptyTable": 'No hay practicas que coincidan con la búsqueda',
        "sLengthMenu": "Mostrar _MENU_ practicas",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
});