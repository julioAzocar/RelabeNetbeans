/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julio
 */
public class CConectorSqlite {

//    String url = "C:\\Users\\julio\\Documents\\NetBeansProjects\\Relave\\bdsqlite\\bd.db";
    
    String sep = System.getProperty("file.separator");
    String url = System.getProperty("user.dir") + sep + "bdsqlite" + sep + "bd.db";
    
    Connection connect;
    public String ConError="";

    public CConectorSqlite() {
        ConError="";
    }
    
    
    public void connect() {
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
            if (connect != null) {
                System.out.println("Conectado");
            }
        } catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n" + ex.getMessage());
            ConError = ex.getMessage();
        }
    }

    public void close() {
        try {
            connect.close();
        } catch (SQLException ex) {
            Logger.getLogger(CConectorSqlite.class.getName()).log(Level.SEVERE, null, ex);
            ConError = ex.getMessage();
        }
    }
}
