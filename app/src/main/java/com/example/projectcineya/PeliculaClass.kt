package com.example.projectcineya

class PeliculaClass {
    var titulo:String?=null
    var director:String?=null
    var genero:String?=null
    var imagen:String?=null
    var cine:String?=null
    var key:String?=null



    constructor(){}
    constructor(
        titulo: String?,
        director: String?,
        genero: String?,
        imagen: String?,
        cine: String?
    ) {
        this.titulo = titulo
        this.director = director
        this.genero = genero
        this.imagen = imagen
        this.cine = cine
    }


}