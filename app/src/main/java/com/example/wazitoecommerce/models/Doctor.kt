package com.example.wazitoecommerce.models

class Doctor {
    var name:String = ""
    var specialisation:String = ""
    var age:String = ""
    var imageUrl:String = ""
    var id:String = ""

    constructor(name: String, specialisation: String, age: String, imageUrl: String, id: String) {
        this.name = name
        this.specialisation = specialisation
        this.age = age
        this.imageUrl = imageUrl
        this.id = id
    }

    constructor()

}