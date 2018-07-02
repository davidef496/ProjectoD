package com.example.davidblanco.projectod

class Project(  var key:String="" , var titulo: String = "", var descripcion: String="", val escuela: String="", val fecha: String="", var tipo:Int=0) {

    constructor(titulo: String = "",descripcion: String="", escuela: String="", fecha: String="",tipo:Int=0):this("", titulo, descripcion, escuela, fecha, tipo){

    }



}