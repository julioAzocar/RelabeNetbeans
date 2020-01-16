/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;


//evalua
import java.math.BigDecimal;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 *
 * @author assendofr
 */
class CTest {
//para depurar debug file no run file
    String xError="";
    String xCond="";
    
    public static void main(String[] args){
        
        CFormulaArma objx = new CFormulaArma();
        String Formula = "SI(45=33;1+2+3+4+5+PR6/23+PR5;123+PR6";
        double total = objx.FormulaTotal(Formula);
        
        /*
       System.out.println("Halo Java");
       String fx = RemplazaPR("SI(45=33;1+2+3+4+5+PR6/23+PR1;123+PR6").trim();
       
       
       if (fx.indexOf("SI") >= 0){
           //evalua si condicion
           String cond1="";String cond2="";
           String val1="";String val2="";
           //pos cond PosCond(Formula) > , < , = , >= , <=
 
       }else{
            //evalua sin condicion
            Evalua(fx);
       }
       
       System.out.println(fx);
       System.out.println(fx

        */
    }
    
    
    private static double Evalua(String Formula){
        double total=0;
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        engine.put("X", 3);
        try {

            total = new Double(engine.eval(Formula).toString());
            System.out.println(new BigDecimal(total).toPlainString());

        } catch (ScriptException e) {
            e.printStackTrace();
        }
         
        return total;
    }
   
    private static String RemplazaPR(String Formula){
        String FormulaRes=Formula;
        String strSepPar="PR";

        //CaracteresNum
        String Caracter = "";String pr="";String formula;
        String f1=""; String f2="";
        
        int pos=0;
        
        pos = FormulaRes.indexOf("PR");
        if (pos >= 0){
            while (pos>=0){
                int xini = PosPRini(FormulaRes);
                int xfin = PosPRfin(FormulaRes,xini);
                pr = FormulaRes.substring(xini, xfin);
                formula = BuscarParametro(pr);
                f1 = FormulaRes.substring(0, xini);
                f2 = FormulaRes.substring(xfin, FormulaRes.length());
                FormulaRes = f1 + formula + f2;
                pos = FormulaRes.indexOf("PR");
            }
            
        }else{
            return Formula;
        }
    
        return FormulaRes;
    }
    
    private static String BuscarParametro(String pr){
        String formula = "";
        
        if (pr.equals("PR6")==true){
            formula = "1+2+45";
        }
        
        if (pr.equals("PR1")==true){
            formula = "2/45";
        }
        
        return formula;
    }
    
    private int PosCond(String Formula){
        //> , < , = , >= , <=
        xCond="";
        int pos = Formula.indexOf(">");
        if (pos>=0){
            return pos;
        }
        pos = Formula.indexOf("<");
        if (pos>=0){
            return pos;
        }
        pos = Formula.indexOf("=");
        if (pos>=0){
            return pos;
        }
        pos = Formula.indexOf(">=");
        if (pos>=0){
            return pos;
        }
        pos = Formula.indexOf("<=");
        if (pos>=0){
            return pos;
        }
        
        return 999;
    }
        
    private static int PosPRini(String Formula){
        int pos = Formula.indexOf("PR");
        return pos;
    }
    
    private static int PosPRfin(String Formula, int xini){
        int pos=0;
        String CarcateresNum = "0123456789";
        for (int x=xini+2; x < Formula.length(); x++){
            String xcar = Formula.substring(x,x+1);
            if (CarcateresNum.indexOf(xcar) == -1){
                pos = x;
                return pos;
            }
        }
        if (pos==0){
            pos = Formula.length();
        }
        return pos;
    }
}
