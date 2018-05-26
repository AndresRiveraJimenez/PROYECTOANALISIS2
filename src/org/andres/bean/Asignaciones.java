/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.bean;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author d5
 */
public class Asignaciones {
    private int idAsignacion;
    private Date fecha;
    private String nombreTecnico;
    private String razonSocial;
    private String descripcion;
    private Tecnico tecnico;
    private Clientes cliente;

    public Asignaciones() {
    }

    public Asignaciones(int idAsignacion, Date fecha,String descripcion, Tecnico tecnico, Clientes cliente) {
        this.idAsignacion = idAsignacion;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tecnico = tecnico;
        this.cliente = cliente;
    }

    public Asignaciones(int idAsignacion, Date fecha, String nombreTecnico, String razonSocial, String descripcion) {
        this.idAsignacion = idAsignacion;
        this.fecha = fecha;
        this.nombreTecnico = nombreTecnico;
        this.razonSocial = razonSocial;
        this.descripcion = descripcion;
    }

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Date getFecha() {
        return fecha;
    }
    public String getFechaString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(this.fecha); 
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    
    
}
