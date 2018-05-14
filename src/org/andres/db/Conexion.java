package org.andresrivera.conexion;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private Connection conexion;
    private Statement enunciado;
    private static Conexion instancia;

    public Conexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            conexion = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=ANALISISDESISTEMAS_v1;user=sa;Password=Admin123!");
            enunciado = conexion.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public ResultSet hacerConsulta(String consulta) {
        ResultSet resultado = null;
        try {
            resultado = enunciado.executeQuery(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void ejecutarSentencia(String sentencia) {
        try {
            enunciado.execute(sentencia);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public Connection getConexion() {
        return conexion;
    }
    
}
