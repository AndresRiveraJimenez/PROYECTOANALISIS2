package org.andres.bean;

import groovy.transform.ToString;

public class Tecnico {
    private int idTecnico;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String usuarioD;
    private String departamento;
    private int estado;

    public Tecnico() {
    }

    public Tecnico(int idTecnico, String nombre) {
        this.idTecnico = idTecnico;
        this.nombre = nombre;
    }
    
    public Tecnico(int idTecnico, String nombre, String apellido, String telefono, String correo, String usuarioD, String departamento, int estado) {
        this.idTecnico = idTecnico;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.usuarioD = usuarioD;
        this.departamento = departamento;
        this.estado = estado;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuarioD() {
        return usuarioD;
    }

    public void setUsuarioD(String usuarioD) {
        this.usuarioD = usuarioD;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }

    
}
