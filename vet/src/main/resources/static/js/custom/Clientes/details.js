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

$(document).ready(function () {
    $('.format-date-with-time').each(function () {
        let originalDate = $(this).text();
        if (originalDate) {
            let formattedDate = new Date(originalDate).toLocaleString('es-AR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
            $(this).text(formattedDate);
        }
    });
    $('.format-date').each(function () {
        let originalDate = $(this).text();
        if (originalDate) {
            let date = new Date(originalDate);
            date.setDate(date.getDate() + 1); // Se le agrega 1 día para que no se muestre un día menos
            let formattedDate = date.toLocaleString('es-AR', {
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            });
            $(this).text(formattedDate);
        }
    });
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

function createLocalDate(date, addDay = false) {
    if (addDay) {
        let newDate = new Date(date);
        newDate.setDate(newDate.getDate() + 1);
        return newDate.toLocaleString("es-AR", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
        });
    }
    return new Date(date).toLocaleString("es-AR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });
}

function formatDate(date) {
    // Convertir a formato yyyy-mm-dd
    let partesFecha = date.split("/");
    let fechaNueva = new Date(partesFecha[2], partesFecha[1] - 1, partesFecha[0]);
    let fechaISO = fechaNueva.toISOString().slice(0, 10);
    return fechaISO;
}

// Datepickers
$('#txtNacimiento').bootstrapMaterialDatePicker({
    maxDate: new Date(),
    time: false,
    format: 'DD/MM/YYYY',
    cancelText: "Cancelar",
    weekStart: 1,
    lang: 'es',
});
$('#txtAtencion').bootstrapMaterialDatePicker({
    currentDate: new Date(),
    maxDate: new Date(),
    time: false,
    format: 'DD/MM/YYYY',
    cancelText: "Cancelar",
    weekStart: 1,
    lang: 'es',
});
$('#txtPago').bootstrapMaterialDatePicker({
    time: false,
    format: 'DD/MM/YYYY',
    cancelText: "Cancelar",
    weekStart: 1,
    lang: 'es',
});

function deleteObj(id, tipo) {
    let title;
    if (tipo == 'mascota') {
        title = "¿Seguro deseas eliminar esta mascota?";
        action = "/Mascotas";
    } else {
        title = "¿Seguro deseas eliminar esta atención?";
        action = "/Atenciones";
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
    if ($(form).attr("action") == '/Mascotas') {
        formData = {
            id: $(form).find('[name="id"]').val(), 
            clienteID: $(form).find('[name="idCliente"]').val(), 
            nombre: $(form).find('[name="nombre"]').val(), 
            nacimiento: formatDate($(form).find('[name="nacimiento"]').val()), 
            sexo: $(form).find('[name="comboSexo"]').val(),
            razaID: $(form).find('[name="comboRazas"]').val(),
        };
    } else {
        let fechaPago = "";
        if ($(form).find('[name="pago"]').val() !== "") fechaPago = formatDate($(form).find('[name="pago"]').val())
        formData = {
            id: $(form).find('[name="id"]').val(), 
            mascotaID: $(form).find('[name="comboMascotas"]').val(), 
            practicasID: $(form).find('[name="comboPracticas"]').val(), 
            fechaAtencion: formatDate($(form).find('[name="atencion"]').val()), 
            fechaPago: fechaPago, 
        };

    }

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
            }).then((result) => {
                if (result.isConfirmed) {
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
    $("#btnSendModalM").text("Confirmar");
    $("input[name='idCliente']").val(entity.cliente.id);
    $("input[name='idCliente']").prop("disabled", false);
    $("#modalTitleM").text("Editar Mascota");
    $("input[name='nombre']").val(entity.nombre);
    $("input[name='nacimiento']").val(createLocalDate(moment(entity.nacimiento).format('YYYY-MM-DD'), true));
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
    $("#btnSendModalA").text("Confirmar");

    const formattedDateTimeAtencion = moment(entity.fechaAtencion).format('YYYY-MM-DD || HH:mm:ss');
    let formattedDateTimePago = 'No hay fecha';
    if (entity.fechaPago != null) {
        formattedDateTimePago = moment(entity.fechaPago).format('YYYY-MM-DD HH:mm:ss');
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
$("#btnSendModalA").on("click", function () {
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
    $("#formContainerM form select").val("");
    $("input[name='id']").prop("disabled", true);
    $("input[name='id-cliente']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});

$("#btnAddAtencion").on("click", function () {
    $("#modalTitleA").text("Agregar atención");
    $("#formContainerA form").attr("method", "POST");
    $("#formContainerA form").attr("id", "form-createA");
    $("#formContainerA form input:not([type='hidden']").val("");
    $("#txtAtencion").val(createLocalDate(new Date()));
    $("#formContainerA form select").val("");
    $("input[name='id']").prop("disabled", true);
    $("#btnSendModal").text("Agregar");
});

$('#DataTable').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ clientes",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 clientes",
        "sInfoFiltered": "(filtrado de _MAX_ clientes en total)",
        "emptyTable": 'No hay clientes que coincidan con la búsqueda',
        "sLengthMenu": "Mostrar _MENU_ clientes",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
});