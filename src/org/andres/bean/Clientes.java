
package org.andres.bean;

public class Clientes{
    private int idCliente;
    private String razonSocial;
    private String telefono;
    private String correo;
    private String direccion;
    private int estado;


    public Clientes() {
    }

    public Clientes(int idCliente, String razonSocial) {
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
    }

    public Clientes(int idCliente, String razonSocial, String telefono, String correo, String direccion, int estado) {
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return razonSocial;
    }
    
}
