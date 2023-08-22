 $.ajaxSetup({
     beforeSend: function (xhr) {
          //Obtener el token de localStorage
         const token = localStorage.getItem('token');
         if (token) {
              //Agregar el token en el encabezado Authorization con el formato Bearer
             xhr.setRequestHeader('Authorization', 'Bearer ' + token);
         }
     }
 });

 verificar_logeado();

 function verificar_logeado(){
   token=localStorage.getItem('token');
   if(token){
     //VER QUE SEA VALIDO
     $.ajax({
     type: "GET",
     url: "/auth/validate-token",
     success: function () {
          //El token es válido, continuar con lo que necesitas hacer
         alert("Token válido, usuario logeado.");
         //window.location.href="http:localhost:8000/inicio"
       },
       error: function (xhr, status, error) {
          //El token no es válido, redirigir al inicio de sesión
         alert("Token inválido, redireccionando al login.");
         localStorage.removeItem('token');
         //window.location.reload();
       }})
   }
 }


 document.addEventListener('click',(e)=>{
  
   if(e.target.matches("#login-btn")){
     e.preventDefault();
     email=document.getElementById("email").value;
     password=document.getElementById("password").value;
     let json= {
       "email":email,
       "password":password,
     }
     let datos= JSON.stringify(json);
      $.ajax({	
          type: "POST",
           url: "/auth/login",
          contentType: "application/json",
          data: datos,
          success: function (data) {
             console.log(data);
             localStorage.removeItem('token');
             localStorage.setItem('token', data.token);
             alert("Usuario LOGIN correcto");
             ////window.location.href="http:localhost:8000/inicio"
   },
                     error: function (error) {
                          //Si ocurre un error en la solicitud (p. ej., token inválido o expirado), redirigir a la página de inicio de sesión
                         //window.location.href = "/index";
                     }
                 });
          }
   })
