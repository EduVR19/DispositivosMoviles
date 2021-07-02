package com.example.dispositivosmoviles

data class UsuarioClass(
    var correo : String? = "",
    var nombre : String? = "",
    var telefono : String? = "",
    var admin : Boolean? = null,
    var mascota : Mascota? = null,
    var reservacion : Reservacion? = null

)

public data class Mascota(
    var nombre : String? = "",
    var sexo : String? ="",
    var edad : String? =""
)

public data class Reservacion(
    var fechafin: String? = "",
    var fechainicio : String? = "",
    var dias : String? = "",
    var nota : String? = "",
    var hora: String? = ""
)
