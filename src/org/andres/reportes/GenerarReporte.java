/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andres.reportes;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.andresrivera.conexion.Conexion;

/**
 *
 * @author d5
 */
public class GenerarReporte 
{
    private static GenerarReporte instancia;
 public static GenerarReporte getInstancia(){
 if(instancia == null){
     instancia = new GenerarReporte();
 }   
    return instancia;
}       
 public void GenerarReporte(Map parametros, String nombreReporte, String titulo){
     InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);
     try{
         JasperReport reporteMaestro = null;
         reporteMaestro =  (JasperReport) JRLoader.loadObject(reporte);
         JasperPrint reporteImpresion = JasperFillManager.fillReport(reporteMaestro, parametros, Conexion.getInstancia().getConexion());
         JasperViewer visor = new JasperViewer(reporteImpresion, false);
         visor.setTitle(titulo);
         visor.setVisible(true);
     }catch(Exception e){
         e.printStackTrace();
                 
     }
     
 }
    
}
