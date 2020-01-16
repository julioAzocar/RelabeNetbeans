/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author assendofr
 */
public class CParametro {

    long idparametro;
    String descripcion;
    long idtipo;
    String formula;
    long idtabla;
    public String strMsg = "";
    public String[] strDatosCombo;
    public String xError = "";
    String CaracteresNum = "0123456789";
    
    public CParametro(long idparametro, String descripcion, long idtipo, String formula, long idtabla) {
        this.idparametro = idparametro;
        this.descripcion = descripcion;
        this.idtipo = idtipo;
        this.formula = formula;
        this.idtabla = idtabla;
        this.xError = "";
    }

    public long getIdparametro() {
        return idparametro;
    }

    public void setIdparametro(long idparametro) {
        this.idparametro = idparametro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getidTipo() {
        return idtipo;
    }

    public void setidTipo(long idtipo) {
        this.idtipo = idtipo;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
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

        if (con.ConError.equals("") == false) {
            xError = con.ConError;
            return;
        }

        String des = this.getDescripcion().trim();
        if (des.equals("") == true) {
            return;
        }
        if (this.getidTipo() == 0) {
            return;
        }
        if (this.getFormula().trim().equals("") == true) {
            return;
        }
        if (this.getIdtabla() == 0) {
            return;
        }

        if (this.getIdparametro() == 0) {
            try {
                PreparedStatement st = con.connect.prepareStatement("insert into parametro "
                        + "(descripcion,idtipo,formula,idtabla) values (?,?,?,?)");

                st.setString(1, this.getDescripcion());
                st.setString(2, String.valueOf(this.getidTipo()));
                st.setString(3, this.getFormula());
                st.setString(4, String.valueOf(this.getIdtabla()));

                st.execute();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                xError=ex.getMessage();
            }
        } else {
            try {
                PreparedStatement st = con.connect.prepareStatement("update parametro set "
                        + "descripcion=?,idtipo=?,formula=?,idtabla=? "
                        + "where idparametro=?");

                st.setString(1, this.getDescripcion());
                st.setString(2, String.valueOf(this.getidTipo()));
                st.setString(3, this.getFormula());
                st.setString(4, String.valueOf(this.getIdtabla()));
                st.setString(5, String.valueOf(this.getIdparametro()));

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
        
        if (this.getIdparametro() > 0) {
            try {
                PreparedStatement st = con.connect.prepareStatement("delete from parametro where idparametro = ?");
                st.setString(1, String.valueOf(this.getIdparametro()));

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
        String[] tableColumnsName = {"idparametro", "descripcion", "idtipo", "tipo", "formula", "idtabla", "tabla"};
        aModel = new DefaultTableModel(null, tableColumnsName) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        aModel.setColumnIdentifiers(tableColumnsName);

        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return aModel;
        }
        
        try {
            String sql = "select parametro.*,"
                    + " tabla.descripcion as tabla, \n"
                    + " tipo.descripcion as tipo \n"
                    + " from tabla,parametro,tipo \n"
                    + " where tabla.idtabla=parametro.idtabla \n"
                    + " and tipo.idtipo=parametro.idtipo \n"
                    + " order by descripcion";

            PreparedStatement st = con.connect.prepareStatement(sql);

            result = st.executeQuery();
            while (result.next()) {

                String[] datos = {String.valueOf(result.getLong("idparametro")),
                    result.getString("descripcion"),
                    String.valueOf(result.getLong("idtipo")),
                    result.getString("tipo"),
                    result.getString("formula"),
                    String.valueOf(result.getLong("idtabla")),
                    result.getString("tabla")};

                aModel.addRow(datos);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            xError=ex.getMessage();
        }

        con.close();
        return aModel;
    }

    public void LLenarTabla() {

        String strSql = "select idtabla,descripcion from tabla order by descripcion";

        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        ResultSet result = null;

        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return;
        }
        
        try {

            PreparedStatement st = con.connect.prepareStatement(strSql);
            int x = 0;
            result = st.executeQuery();
            strDatosCombo = new String[0];

            while (result.next()) {
                strDatosCombo = Arrays.copyOf(strDatosCombo, strDatosCombo.length + 1);
                strDatosCombo[x] = result.getString("idtabla") + " " + result.getString("descripcion");

                x = x + 1;
            }

        } catch (Exception ex) {

            strMsg = ex.getMessage();
            xError=ex.getMessage();
        }

        con.close();

    }

    public void LLenarTipo()//lista de proyectos
    {

        String strSql = "select idtipo,descripcion from tipo order by descripcion";

        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        ResultSet result = null;
        
        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return;
        }
        
        try {

            PreparedStatement st = con.connect.prepareStatement(strSql);
            int x = 0;
            result = st.executeQuery();
            strDatosCombo = new String[0];

            while (result.next()) {
                strDatosCombo = Arrays.copyOf(strDatosCombo, strDatosCombo.length + 1);
                strDatosCombo[x] = result.getString("idtipo") + " " + result.getString("descripcion");

                x = x + 1;
            }

        } catch (Exception ex) {

            strMsg = ex.getMessage();
            xError=ex.getMessage();
        }

        con.close();
    }

    public void LLenarParametro()//lista de proyectos
    {

        String strSql = "select idparametro,descripcion from parametro order by descripcion";

        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        ResultSet result = null;

        if (con.ConError.equals("")==false){
            xError=con.ConError;
            return;
        }
        
        try {

            PreparedStatement st = con.connect.prepareStatement(strSql);
            int x = 0;
            result = st.executeQuery();
            strDatosCombo = new String[0];

            while (result.next()) {
                strDatosCombo = Arrays.copyOf(strDatosCombo, strDatosCombo.length + 1);
                strDatosCombo[x] = result.getString("idparametro") + " " + result.getString("descripcion");

                x = x + 1;
            }

        } catch (Exception ex) {

            strMsg = ex.getMessage();
            xError=ex.getMessage();
        }

        con.close();
    }

}
