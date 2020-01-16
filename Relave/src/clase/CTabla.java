/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author julio
 */
public class CTabla {

    long idtabla;
    String descripcion;
    public String xError="";
    public CTabla(long idtabla, String descripcion) {
        this.idtabla = idtabla;
        this.descripcion = descripcion;
        this.xError="";
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getIdtabla() {
        return idtabla;
    }

    public void setIdtabla(long idtabla) {
        this.idtabla = idtabla;
    }
    
    public void Guardar() {
        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        
        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return;
        }
        
        String des = this.getDescripcion().trim();
        if (des.equals("") == true){
            return;
        }
        
        if (this.idtabla == 0) {
            try {
                PreparedStatement st = con.connect.prepareStatement("insert into tabla (descripcion) values (?)");
                st.setString(1, this.getDescripcion());
                st.execute();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                xError=ex.getMessage();
            }
        } else {
            try {
                PreparedStatement st = con.connect.prepareStatement("update tabla set descripcion=? where idtabla=?");
                st.setString(1, this.getDescripcion());
                st.setString(2, String.valueOf(this.getIdtabla()));
                st.execute();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                xError=ex.getMessage();
            }
        }

        con.close();
    }

    public void Eliminar() {
        CConectorSqlite con = new CConectorSqlite();
        con.connect();

        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return;
        }
        
        if (this.idtabla > 0) {
            try {
                PreparedStatement st = con.connect.prepareStatement("delete from tabla where idtabla = ?");
                st.setString(1, String.valueOf(this.getIdtabla()));
                st.execute();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                xError=ex.getMessage();
            }
        }

        con.close();
    }

    
            
//        ModEjemplo= new DefaultTableModel(Datos,ColumnNames)
//            {
//                @Override
//                public boolean isCellEditable(int row, int column)
//                {
//                    return false;
//                }
//            };
        
        
        
    public DefaultTableModel Listar() {
        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        
        ResultSet result = null;
        //tabla model
        DefaultTableModel aModel;
        String[] tableColumnsName = {"idtabla", "descripcion"};
        aModel = new DefaultTableModel(null, tableColumnsName)           {
                @Override
                public boolean isCellEditable(int row, int column)
                {
                    return false;
                }
            };
        aModel.setColumnIdentifiers(tableColumnsName);

        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return aModel;
        }
        
        try {
            PreparedStatement st = con.connect.prepareStatement("select * from tabla order by descripcion");
            result = st.executeQuery();
            while (result.next()) {
                System.out.println(result.getInt("idtabla"));
                System.out.println(result.getString("descripcion"));

                //String[] datos = {String.valueOf(result.getLong("idtabla")), result.getString("descripcion")};
                //new Object[]{"",""};
                JButton b1 = new JButton("Editar");
                String idt = String.valueOf(result.getLong("idtabla"));
                String des = result.getString("descripcion");
                //aModel.addRow(new Object[][]{{"a1","a2",b1},{"a3","a4",b1}});
                aModel.addRow(new Object[]{idt,des,b1});
                
            }
            
            
            /*
            DefaultTableModel d = new DefaultTableModel(
                new Object[][]{{"a1","a2",b1},{"a3","a4",b1}},
                new Object[]{"c1","c2","E"}
            );
            */
            
     
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            xError=ex.getMessage();
        }

        con.close();
        return aModel;
    }


}
