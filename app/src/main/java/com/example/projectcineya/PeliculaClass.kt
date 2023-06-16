package com.example.projectcineya

class PeliculaClass {
    var titulo:String?=null
    var director:String?=null
    var genero:String?=null
    var imagen:String?=null

    constructor(titulo: String?, director: String?, genero: String?, imagen: String?) {
        this.titulo = titulo
        this.director = director
        this.genero = genero
        this.imagen = imagen
    }
}