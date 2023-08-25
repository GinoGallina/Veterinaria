
$(document).on("click", "button[name='btnSale']", function () {
    let entity = $(this).data("entity");
    if (entity.stock != null) {
        Swal.fire({
            title: 'Ingrese la cantidad:',
            text: `Stock: ${entity.stock} u.`,
            input: 'number',
            inputAttributes: {
                min: 1,
                max: entity.stock,
                step: 1
            },
            showCancelButton: true,
            confirmButtonText: 'Agregar',
            cancelButtonText: 'Cancelar',
            buttonsStyling: false,
            customClass: {
                confirmButton: 'btn btn-success waves-effect waves-light px-3 py-2',
                cancelButton: 'btn btn-default waves-effect waves-light px-3 py-2'
            },
            showLoaderOnConfirm: true,
            inputValidator: (value) => {
                if (!value || value < 1 || value > entity.stock) {
                    return 'Por favor, ingrese una cantidad válida';
                }
            },
            preConfirm: (cantidad) => {
                addToSelectedTable(entity, cantidad);
                removeFromTable("#DataTable", entity.id);
            },
        });
    } else {
        addToSelectedTable(entity, 1);
        removeFromTable("#DataTable", entity.id);
    }
});

$(document).on("click", "button[name='btnDelete']", function () {
    let entity = $(this).data("entity");
    addToProductsTable(entity);
    removeFromTable("#productsSelected", entity.id);
});

let paymentMethodsData;
let productsData;

$("#btnSend").on("click", function () {
    let total = 0;
    $("#productsSelected").DataTable().rows().every(function () {
        let row = $(this.node());
        let price = row.find("td:nth-child(3)").text().replace("$", "").replace(".", "");
        let quantity = row.find("td:nth-child(4)").text();
        total += price * quantity;
    });
    Swal.fire({
        icon: 'question',
        title: '¿Seguro que desea realizar la reserva?',
        text: `El total es de $${total}`,
        showCancelButton: true,
        confirmButtonText: 'Confirmar',
        cancelButtonText: 'Cancelar',
        buttonsStyling: false,
        customClass: {
            confirmButton: 'btn btn-success waves-effect waves-light px-3 py-2',
            cancelButton: 'btn btn-default waves-effect waves-light px-3 py-2'
        },
    }).then((result) => {
        if (result.isConfirmed) {
            sendForm();
        }
    });
});

function sendForm() {
    createProductsData();
    let form = $("#form-create");
    // Enviar solicitud AJAX
    $.ajax({
        url: $(form).attr('action'), // Utiliza la ruta del formulario
        method: $(form).attr('method'), // Utiliza el método del formulario
        data: JSON.stringify(productsData), // Utiliza los datos del formulario
        contentType: "application/json",
        success: function (response) {
            Swal.fire({
                title: response.message,
                icon: 'success',
                showCancelButton: false,
                confirmButtonColor: '#1e88e5',
                confirmButtonText: 'OK',
                allowOutsideClick: false,
            })
            .then((result) => {
                if (result.isConfirmed) {
                    window.location.href = window.location.origin;
                }
            });
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
};

function createProductsData() {
    let products = [];
    $("#productsSelected").DataTable().rows().every(function () {
        let row = $(this.node());
        let productID = row.data("id");
        let quantity = row.data("quantity");
        let product = {
            producto: {
                id: productID
            },
            cantidad: quantity
        };
        products.push(product);
    });
    productsData = products;
}

function removeFromTable(tableID, rowID) {
    let table = $(tableID);
    let row = table.find(`tbody tr[data-id='${rowID}']`);
    table.DataTable().row(row).remove().draw();

    let rows = $("#productsSelected").DataTable().rows().count();
    if (rows > 0) {
        $("#btnSend").show();
    } else {
        $("#btnSend").hide();
    }
}

function addToSelectedTable(entity, cantidad) {
    let table = $("#productsSelected");
    let row = $('#DataTable').find(`tbody tr[data-id='${entity.id}']`);
    let content = `
        <tr data-id="${entity.id}" data-quantity="${cantidad}">
            <td>${entity.descripcion}</td>
            <td>${entity.stock}</td>
            <td>$${entity.precio}</td>
            <td>${cantidad}</td>
            <td>
                <div class='d-flex flex-row justify-content-center'>
                    <button name="btnDelete" type='button' class='btn btn-outline-danger mr-2' data-entity='${JSON.stringify(entity)}'><i class="bi bi-trash"></i></button>
                </div>
            </td>
        </tr>
    `;

    table.DataTable().row.add($(content)).draw();
}

function addToProductsTable(entity) {
    let table = $("#DataTable");
    let row = $('#productsSelected').find(`tbody tr[data-id='${entity.id}']`);
    let content = `
        <tr data-id="${entity.id}">
            <td>${entity.descripcion}</td>
            <td>${entity.stock}</td>
            <td>$${entity.precio}</td>
            <td>
                <div class='d-flex flex-row justify-content-center'>
                    <button name="btnSale" type='button' class='btn btn-outline-info mr-2' data-entity='${JSON.stringify(entity)}'><i class="bi bi-plus-lg"></i></button>
                </div>
            </td>
        </tr>
    `;

    table.DataTable().row.add($(content)).draw();
}

$('#DataTable').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ productos",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 productos",
        "sInfoFiltered": "(filtrado de _MAX_ productos en total)",
        "emptyTable": 'No hay productos que coincidan con la búsqueda',
        "sLengthMenu": "Mostrar _MENU_ productos",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
});

$('#productsSelected').DataTable({
    "language": {
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ productos",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 productos",
        "sInfoFiltered": "(filtrado de _MAX_ productos en total)",
        "emptyTable": 'No hay productos elegidos',
        "sLengthMenu": "Mostrar _MENU_ productos",
        "sSearch": "Buscar:",
        "oPaginate": {
            "sFirst": "Primero",
            "sLast": "Último",
            "sNext": "Siguiente",
            "sPrevious": "Anterior",
        },
    },
    paging: false,
    searching: false,
    ordering: false,
    info: false
});