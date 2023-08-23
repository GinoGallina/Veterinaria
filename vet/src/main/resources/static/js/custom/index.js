// $.ajaxSetup({
//     beforeSend: function (xhr) {
//         // Obtener el token de localStorage
//         const token = localStorage.getItem('token');
//         if (token) {
//             // Agregar el token en el encabezado Authorization con el formato Bearer
//             xhr.setRequestHeader('Authorization', 'Bearer ' + token);
//         }
//     }
// });
// verificar_logeado();

// function verificar_logeado(){
//   token=localStorage.getItem('token');
//   if(!token){
//     window.location.href="http://localhost:8000/loginN"
//   }else{
//     $.ajax({
//     type: "GET",
//     url: "http://localhost:8000/auth/validate-token",
//     success: function () {
//         // El token es válido, continuar con lo que necesitas hacer
//         console.log("Token válido, usuario logeado.");
//       },
//       error: function (xhr, status, error) {
//         // El token no es válido, redirigir al inicio de sesión
//         alert("Token inválido, redireccionando al login.");
//         localStorage.removeItem('token');
//         window.location.href="http://localhost:8000/loginN"
//       }})
//   }
// }

// function extractUsernameFromToken(token) {
//   const tokenPayload = token.split('.')[1]; // Obtenemos la parte del payload del token
//   const decodedPayload = JSON.parse(atob(tokenPayload)); // Decodificamos el payload y lo convertimos en un objeto JavaScript
//   return decodedPayload.sub; // Suponiendo que el nombre de usuario se encuentra en la propiedad "sub" del payload
// }

// function extractRolesFromToken(token) {
//   const tokenPayload = token.split('.')[1];
//   const decodedPayload = JSON.parse(atob(tokenPayload));
//   return decodedPayload.roles; // Suponiendo que los roles se encuentran en una propiedad llamada "roles" del payload
// }

// // Ejemplo de uso
// const tokenN = localStorage.getItem('token'); // Aquí debes reemplazar '...' con el token JWT real
// const username = extractUsernameFromToken(token);
// const roles = extractRolesFromToken(token);
// console.log('Nombre de usuario:', username);
// console.log('Roles:', roles);