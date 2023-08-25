$("span[name='calculateTotal']").each(function () {
    var reserva = JSON.parse($(this).attr("data-total"));
    var total = 0;
    reserva.reservasProductos.forEach(function (reservaProducto) {
        total += reservaProducto.cantidad * reservaProducto.precio;
    });
    $(this).text("$" + total);
});