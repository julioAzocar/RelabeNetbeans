//Conexion con base de datos //Conexion con base de datos//Conexion con base de datos
package clase;

/**
 *
 * @author julio Conector para mysql
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CConector {
    public long idGenerado;
    public long rowsInserted;
    public String strMsg;
    Connection conn;
    public Statement ps;
    public PreparedStatement  prs;
    public ResultSet rs;
    public int colNo;
    
    public CConector(){
            conn = null;
    }

    public boolean Conectar(){
  
        try {
            conn = null;
            
            Class.forName("com.mysql.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost/arbolserviciosv3", "root", "");//local
//            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:33061/bd1", "root", "secret");//docker local
       
//conn = DriverManager.getConnection("jdbc:mysql://172.17.0.1:32771/arbolserviciosv3", "root", "1505");//local

conn = DriverManager.getConnection("jdbc:mysql://localhost/arbolserviciosv3", "root", "1234");//local

           if (conn!=null) {
            return true;
            }

        }catch (SQLException ex) {
            System.err.println("No se ha podido conectar a la base de datos\n"+ex.getMessage());
            strMsg = ex.getMessage();
            return false;
         } catch (ClassNotFoundException ex) {
            throw new ClassCastException(ex.getMessage());
         }
        
         return false;
         
    }
        
        
    public boolean desConectar(){
        try {
            rs.close();
            ps.close();
            conn.close();
            return true;
        } catch (Exception  ex) {
            strMsg = ex.getMessage();
            return false;
        }
    }
          
    public boolean Consulta(String StrSql){
        try {
//            Conectar();
//           
            ps = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            rs = null;
            PreparedStatement st = conn.prepareStatement(StrSql);
            rs = st.executeQuery();
            
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            colNo = rsmd.getColumnCount();
            
            strMsg="Fin Conectar " + colNo;
            
            return true;
        } catch (Exception  ex) {
            strMsg = ex.getMessage();
            desConectar();
            return false;
        }
    }
        
    public boolean InsertUpdate(String StrSql, String[] ValorDatos){
        try {
            
//            Conectar();

            prs = conn.prepareStatement(StrSql, Statement.RETURN_GENERATED_KEYS);
            
            if (ValorDatos[0].equals("XXXXX")==false){
                for (int x=0;x<ValorDatos.length;x++)
                {
                    prs.setString(x+1, ValorDatos[x]);
                }
            }

            rowsInserted = prs.executeUpdate();

            ResultSet generatedKeys = prs.getGeneratedKeys();
            if (generatedKeys.next()) {
                     idGenerado = generatedKeys.getInt(1);
            }
            
            if (rowsInserted > 0) {
                return true;
            }else{
                return false;
            }

        } catch (Exception  ex) {
            strMsg = ex.getMessage();
//            desConectar();
            return false;
        }
    }
    
    
    
}
