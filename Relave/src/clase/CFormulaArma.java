/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author assendofr
 */
public class CFormulaArma {

    public String xError = "";
    //condicion SI(a>b;1;2)
    String xCond = "";
    int xCondpos = 0;
    int xCondposFin = 0;
    int xCondpc1 = 0;
    int xCondpc2 = 0;
    public String FormulaConv="";
    
    public double FormulaTotal(String Formula) {
        //"SI(45=33;1+2+3+4+5+PR6/23+PR1;123+PR6"
        
        if (Formula.equals("")==true){
            xError = "Ingrese formula";
            return 0;
        }
        
        FormulaConv="";
        
        String fx = RemplazaPR(Formula).trim();
        FormulaConv=fx;
        double total = 0;

        if (xError.equals("") == false) {
            return 0;
        }

        if (fx.indexOf("SI") >= 0) {
            //evalua si condicion
            String cond1 = "";
            String cond2 = "";
            String val1 = "";
            String val2 = "";

            PosCond(fx);// > , < , = , >= , <=
            if (xCondpos == 999) {
                xError = "Posicion de condicion > , < , = , >= , <= no encontrada";
                return 0;
            }
            if (xCondpc1 == -1) {
                xError = "Falta caracter ; de inicio en condicion";
                return 0;
            }
            if (xCondpc2 == -1) {
                xError = "Falta caracter ; de fin en condicion";
                return 0;
            }
            cond1 = fx.substring(3, xCondpos);
            cond2 = fx.substring(xCondposFin, xCondpc1);
            val1 = fx.substring(xCondpc1 + 1, xCondpc2);
            val2 = fx.substring(xCondpc2 + 1, fx.length()-1);
            
            double eval1 = 0;
            double eval2 = 0;
            double eval3 = 0;
            double eval4 = 0;
            eval1 = Evalua(cond1);      if(xError.equals("") == false)return 0;
            eval2 = Evalua(cond2);      if(xError.equals("") == false)return 0;
            eval3 = Evalua(val1);      if(xError.equals("") == false)return 0;
            eval4 = Evalua(val2);      if(xError.equals("") == false)return 0;

            total = EvaluaCondicion(xCond, eval1, eval2, eval3, eval4);// > , < , = , >= , <=
            
        } else {
            //evalua sin condicion
            total = Evalua(fx);
        }

        return total;
    }

    private double EvaluaCondicion(String xCond,
            double eval1, double eval2,
            double eval3, double eval4)// > , < , = , >= , <=
    {
        double total = 0;
        boolean pasa=false;
        xError="";
        
        switch(xCond) {
          case ">":
            pasa=true;
            if(eval1 > eval2){
                total = eval3;
            }else{
                total = eval4;
            }
            break;
          case "<":
              pasa=true;
            if(eval1 < eval2){
                total = eval3;
            }else{
                total = eval4;
            }
            break;
          case "=":
              pasa=true;
            if(eval1 == eval2){
                total = eval3;
            }else{
                total = eval4;
            }
            break;
          case ">=":
              pasa=true;
            if(eval1 >= eval2){
                total = eval3;
            }else{
                total = eval4;
            }
            break;
          case "<=":
              pasa=true;
            if(eval1 <= eval2){
                total = eval3;
            }else{
                total = eval4;
            }
            break;
        }

        if (pasa == false){
            xError = "Condicion no reconocida, utilice > , < , = , >= , <=";
            
        }
        return total;

    }

    private double Evalua(String Formula) {
        double total = 0;

        if (Formula.indexOf("SI") >= 0) {
            xError = "Formula contiene sub condicion en parametro";
            return 0;
        }
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        
        engine.put("X", 3);
        try {
            String totstr = engine.eval(Formula).toString();
            String totstrCorta = "";

            int pos = totstr.indexOf(".");

            if (pos > 0) {
                totstrCorta = totstr.substring(0, pos) + "." + totstr.substring(pos + 1, pos + 7);
                totstr = totstrCorta;
            }

            total = new Double(totstr);
            System.out.println(new BigDecimal(total).toPlainString());

        } catch (ScriptException e) {
            e.printStackTrace();
            xError = e.getMessage();
        }

        return total;
    }

    public String RemplazaPR(String Formula) {
        String FormulaRes = Formula;
        String strSepPar = "PR";

        //CaracteresNum
        String Caracter = "";
        String pr = "";
        String formula;
        String f1 = "";
        String f2 = "";

        int pos = 0;
        int loop = 0;

        pos = FormulaRes.indexOf("PR");
        if (pos >= 0) {
            while (pos >= 0) {
                int xini = PosPRini(FormulaRes);
                int xfin = PosPRfin(FormulaRes, xini);
                pr = FormulaRes.substring(xini, xfin);
                formula = BuscarParametro(pr);
                if (formula.equals("") == true) {
                    return "";
                }
                f1 = FormulaRes.substring(0, xini);
                f2 = FormulaRes.substring(xfin, FormulaRes.length());
                FormulaRes = f1 + formula + f2;
                pos = FormulaRes.indexOf("PR");
                loop = loop + 1;
                if (loop >= 200) {
                    xError = " Formula genero loop infinito";
                    return "";
                }
            }

        } else {
            return Formula;
        }

        return FormulaRes;
    }

    private int PosPRini(String Formula) {
        int pos = Formula.indexOf("PR");
        return pos;
    }

    private int PosPRfin(String Formula, int xini) {
        int pos = 0;
        String CarcateresNum = "0123456789";
        for (int x = xini + 2; x < Formula.length(); x++) {
            String xcar = Formula.substring(x, x + 1);
            if (CarcateresNum.indexOf(xcar) == -1) {
                pos = x;
                return pos;
            }
        }
        if (pos == 0) {
            pos = Formula.length();
        }
        return pos;
    }

    private String BuscarParametro(String pr) {
        xError = "";

        String formula = "";

        String idp = pr.replace("PR", "");

        String strSql = "select formula from parametro "
                + " where idparametro = ?";

        CConectorSqlite con = new CConectorSqlite();
        con.connect();
        ResultSet result = null;

        if (con.ConError.equals("") == false) {
            xError = con.ConError;
            return formula;
        }

        try {

            PreparedStatement st = con.connect.prepareStatement(strSql);
            st.setString(1, idp);

            int x = 0;
            result = st.executeQuery();

            while (result.next()) {
                formula = result.getString("formula");
            }

        } catch (Exception ex) {

            xError = ex.getMessage();
        }

        if (formula.equals("") == true) {
            xError = " Formula : " + pr + " sin formula";
        }

        con.close();
        return formula;
    }

    private void PosCond(String Formula) {
        //> , < , = , >= , <=
        xCond = "";
        xCondpos = 0;

        xCondpos = Formula.indexOf(">=");
        xCondpc1 = Formula.indexOf(";");
        xCondpc2 = Formula.substring(xCondpc1 + 1, Formula.length()).indexOf(";");
        if (xCondpc2 >= 0) {
            xCondpc2 = xCondpc2 + xCondpc1 + 1;
        } else {
            return;
        }

        if (xCondpos >= 0) {
            xCond = ">=";
            xCondposFin = xCondpos + 2;
            return;
        }
        xCondpos = Formula.indexOf("<=");
        if (xCondpos >= 0) {
            xCond = "<=";
            xCondposFin = xCondpos + 2;
            return;
        }
        xCondpos = Formula.indexOf(">");
        if (xCondpos >= 0) {
            xCond = ">";
            xCondposFin = xCondpos + 1;
            return;
        }
        xCondpos = Formula.indexOf("<");
        if (xCondpos >= 0) {
            xCond = "<";
            xCondposFin = xCondpos + 1;
            return;
        }
        xCondpos = Formula.indexOf("=");
        if (xCondpos >= 0) {
            xCond = "=";
            xCondposFin = xCondpos + 1;
            return;
        }

        xCondpos = 999;
    }

}
