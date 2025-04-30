/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UtilSQL;

import java.sql.*;

/**
 *
 * @author ester
 */
public class ConexaoSQL {

    public static Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/spotifei_db";
        String usuario = "postgres";
        String senha = "fei";

        return DriverManager.getConnection(url, usuario, senha);
    }
}
