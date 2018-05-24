package org.andres.bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Boleta {

    private int idBoleta;
    private String motivo;
    private LocalDate fechaVisita;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String descripcion;
    private LocalDate fechaCreado;
    private Clientes cliente;
    private String tecnico;
    private int estado;
    private String imagen;

    public Boleta() {
    }

    public Boleta(int idBoleta, String motivo, LocalDate fechaVisita, LocalTime horaEntrada, LocalTime horaSalida, String descripcion, LocalDate fechaCreado, Clientes cliente, String tecnico, int estado,String imagen) {
        this.idBoleta = idBoleta;
        this.motivo = motivo;
        this.fechaVisita = fechaVisita;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.descripcion = descripcion;
        this.fechaCreado = fechaCreado;
        this.cliente = cliente;
        this.tecnico = tecnico;
        this.estado = estado;
        this.imagen = imagen;
    }

    public int getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(int idBoleta) {
        this.idBoleta = idBoleta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public String getFechaVisitaES() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(fechaVisita);
    }

    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreado() {
        return fechaCreado;
    }

    public String getFechaCreadoES() {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(fechaCreado);
    }

    public void setFechaCreado(LocalDate fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public String getNombeCliente(){
        return this.cliente.getRazonSocial();
    }

    public int getIdCliente(){
        return this.cliente.getIdCliente();
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
