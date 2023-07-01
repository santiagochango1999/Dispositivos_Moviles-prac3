package com.example.dispositivosmoviles.Proyecto.data.validator

import com.example.dispositivosmoviles.Proyecto.data.entities.LoginUser

class LoginValidator {
    fun checkLogin(name: String, password: String): Boolean {
        val admin = LoginUser()
        //codigo de evaluacion / condicion
        return (admin.name == name &&
                admin.password == password)
    }
}
