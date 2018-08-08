package com.example.davidblanco.projectod

class Project(  var key:String="" , var titulo: String = "", var descripcion: String="", val escuela: String="", val fecha: Int=0, var tipo:Int=0,var email:String ="",var comentario:String="") {

    constructor(titulo: String = "",descripcion: String="", escuela: String="", fecha: Int=0,tipo:Int=0, email:String =""):this("", titulo, descripcion, escuela, fecha, tipo,email,""){

    }



}