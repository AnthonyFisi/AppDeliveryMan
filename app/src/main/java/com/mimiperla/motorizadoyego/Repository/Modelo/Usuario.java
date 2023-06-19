package com.mimiperla.motorizadoyego.Repository.Modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Usuario implements Serializable {


    private Long idusuariogeneral;

    private String nombre;

    private String correo;

    private String contrasena;

    private String apellido;

    private String celular;

    private String foto;

    private Set<?> roles = new HashSet<>();


    public Long getIdusuariogeneral() {
        return idusuariogeneral;
    }

    public void setIdusuariogeneral(Long idusuariogeneral) {
        this.idusuariogeneral = idusuariogeneral;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Set<?> getRoles() {
        return roles;
    }

    public void setRoles(Set<?> roles) {
        this.roles = roles;
    }
}


