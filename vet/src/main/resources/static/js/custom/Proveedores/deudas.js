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

function createDate(date) {
    return new Date(date).toLocaleString("es-AR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });
}

$("#btnAdd").on("click", function () {
    $("#form-create input[name='descripcion']").val("");
    $("#form-create input[name='precio']").val("");
});

function pay(item) {
    item = JSON.parse(item);
    console.log(item)
    $('#form-pay input[name="deudaID"]').val(item.ID);
    $("#modalTitle").text(`Pagar deuda de "${item.descripcion}"`);
    $("#form-pay input[name='pago']").val("");
    let totalAmount = item.pagosDeuda.reduce(function (sum, payment) {
        return sum + payment.pago;
    }, 0);
    $("#modalSubtitle").text("Adeudas: $" + (item.price - totalAmount));
}

function fillTable(item) {
    let total = item.pagosDeuda.reduce(function (sum, payment) {
        return sum + payment.pago;
    }, 0);
    let content = `
        <tr data-id='${item.id}'>
            <td>${item.descripcion}</td>
            <td>$${item.precio.toLocaleString("de-DE") }</td>
            <td>$${total.toLocaleString("de-DE") }</td>
            <td>${createDate(item.createdAt)}</td>
            <td>
                <div class="d-flex justify-content-center">
                    <button type='button' class='btn btn-outline-info btn-rounded btn-sm mr-2' onclick='pay(${JSON.stringify(item)})' data-toggle="modal" data-target="#modalPay"><i class="bi bi-cash"></i></button>
                    <button type='button' class='btn btn-danger btn-rounded btn-sm ml-2' onclick='deleteObj(${item.id})'><i class='bi bi-trash3'></i></button>
                </div>
            </td>
        </tr>`;
    $('#DataTable').DataTable().row.add($(content)).draw();
}

function removeFromTable(id) {
    $('#DataTable').DataTable().row(`[data-id="${id}"]`).remove().draw();
}

function deleteObj(id) {
    Swal.fire({
        title: "¿Seguro deseas eliminar esta deuda?",
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Eliminar',
        buttonsStyling: false,
        customClass: {
            confirmButton: 'btn btn-danger waves-effect waves-light px-3 py-2',
            cancelButton: 'btn btn-default waves-effect waves-light px-3 py-2'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $("#form-delete input[name='id']").val(id);
            sendForm("delete");
        }
    });
}

function sendForm(action) {
    let form = document.getElementById(`form-${action}`);

    // Enviar solicitud AJAX
    $.ajax({
        url: $(form).attr('action'), // Utiliza la ruta del formulario
        method: $(form).attr('method'), // Utiliza el método del formulario
        data: $(form).serialize(), // Utiliza los datos del formulario
        success: function (response) {
            Swal.fire({
                icon: 'success',
                title: response.message,
                confirmButtonColor: '#1e88e5',
            });
            if (action === 'create') {
                $("#btnCloseModal").click();
                fillTable(response.data);
            } else if (action === 'delete') {
                removeFromTable(response.data);
            } else if (action === 'pay') {
                $("#btnCloseModalPay").click();
                removeFromTable(response.data.id);
                fillTable(response.data);
            }
        },
        error: function (errorThrown) {
            Swal.fire({
                icon: 'error',
                title: errorThrown.responseJSON.title,
                text: errorThrown.responseJSON.message,
                confirmButtonColor: '#1e88e5',
            });
        }
    });
}

$("#btnSendDebt").on("click", function (e) {
    sendForm("create");
});

$("#btnSendPay").on("click", function (e) {
    let totalAmount = $("#modalSubtitle").text().split("$")[1];
    let inputValue = $("#form-pay input[name='pago']").val();
    if (inputValue === "") {
        Swal.fire({
            icon: 'warning',
            title: 'ALERTA',
            text: 'Debes ingresar un monto',
            confirmButtonColor: '#1e88e5',
        });
        return;
    } else if (parseInt(inputValue) <= 0) {
        Swal.fire({
            icon: 'warning',
            title: 'ALERTA',
            text: 'El monto debe ser superior a $0',
            confirmButtonColor: '#1e88e5',
        });
        return;
    } else if (parseInt(inputValue) > parseInt(totalAmount)) {
        Swal.fire({
            icon: 'warning',
            title: 'ALERTA',
            text: 'El monto no puede ser mayor a la deuda',
            confirmButtonColor: '#1e88e5',
        });
        return;
    }
    sendForm("pay");
});

$('#DataTable').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ deudas",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 deudas",
        "sInfoFiltered": "(filtrado de _MAX_ deudas en total)",
        "emptyTable": 'No hay deudas que coincidan con la búsqueda',
        "sLengthMenu": "Mostrar _MENU_ deudas",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
    "order": [[3, "desc"]],
});