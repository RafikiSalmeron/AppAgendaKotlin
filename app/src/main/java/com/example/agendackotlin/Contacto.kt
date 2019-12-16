package com.example.agendackotlin

import java.io.Serializable
import java.util.ArrayList

class Contacto(
    var contactoId: Int,
    var nombre: String,
    var direccion: String,
    var webBlog: String,
    var telefonos: ArrayList<String>,
    var email: String,
    var foto: String
) : Serializable
