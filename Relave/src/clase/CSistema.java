
package clase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import javax.swing.JTextArea;

/**
 *
 * @author julio
 */
public class CSistema {
    
    
    public String Usuario;
    public String[] CarRes;
    public boolean bolAdmin; 
    public String BD;
    public String t1;   public String t2;   public String t3;
    public String t4;   public String t5;   public String t6;
    public boolean bolCom;
    public String strSep = "-----";
    //puntos
    public double p_fun =0;
    public double p_sql =0;
    public double p_tpcall =0;
    public double p_soap =0;
    public double p_fcc =0;
        
//    p_fun,    p_sql,    p_tpcall,    p_soap,    p_fcc
    public String VariablePaso;
    
    public CSistema(){
        Usuario="Admin";
        CarRes = new String[3];
        CarRes[0]=(char)92 + strSep + "TextEspSqlite1";
        CarRes[1]=(char)39 + strSep + "TextEspSqlite2";
        CarRes[2]=(char)34 + strSep + "TextEspSqlite3";
        bolAdmin=true;
        BD="";

    }
    
    public static void MensajeTx(JTextArea txt_proceso, String msg){
        try{
            
        
        String Text1="";
        boolean Pasa=false;
        
        Text1=txt_proceso.getText();
        txt_proceso.setText(Text1 +  "\n" + msg);
        Text1=txt_proceso.getText();
        
        if (Text1.equals("")==false){  
            
            int y = Text1.length()-1;
            String xC ="";
            int N13 = 13;
            String e13 = Character.toString((char)N13);

            while (y > 0) {
                xC =  Text1.substring(y,y+1);
                if (xC.equals("\n")==true) {
                    Pasa=true;
                    break;
                }
                y=y-1;
            }
        
            if (Pasa==true){
                y=y+1;
                txt_proceso.setSelectionStart(y);
                txt_proceso.setSelectionEnd(y);
            }
            
        }
        
        } catch (Exception e) {
		throw e;
	} 
    }
    
    public static void CopiaDirectorioRaiz(File dirOrigen, File dirDestino) throws Exception { 
	try {
		if (dirOrigen.isDirectory()) { 
			if (!dirDestino.exists())
				dirDestino.mkdir(); 
 
			String[] hijos = dirOrigen.list(); 
			for (int i=0; i < hijos.length; i++) { 
				CopiaDirectorioRaiz(new File(dirOrigen, hijos[i]), 
					new File(dirDestino, hijos[i])); 
			} // end for
		} else { 
			Copiar(dirOrigen, dirDestino); 
		} // end if
	} catch (Exception e) {
		throw e;
	} // end try
} // end CopiarDirectorio
 
public static void Copiar(File dirOrigen, File dirDestino) throws Exception { 
 
	InputStream in = new FileInputStream(dirOrigen); 
	OutputStream out = new FileOutputStream(dirDestino); 
 
	byte[] buffer = new byte[1024];
	int len;
 
	try {
		// recorrer el array de bytes y recomponerlo
		while ((len = in.read(buffer)) > 0) { 
			out.write(buffer, 0, len); 
		} // end while
		out.flush();
	} catch (Exception e) {
		throw e;
	} finally {
		in.close(); 
		out.close(); 
	} // end ty
} // end Copiar
 
public static void Copiar(String dirOrigen, String dirDestino) throws Exception { 
	InputStream in = new FileInputStream(dirOrigen); 
	OutputStream out = new FileOutputStream(dirDestino); 
 
	byte[] buffer = new byte[1024];
	int len;
 
	try {
		// recorrer el array de bytes y recomponerlo
		while ((len = in.read(buffer)) > 0) { 
			out.write(buffer, 0, len); 
		} // end while
		out.flush();
	} catch (Exception e) {
		throw e;
	} finally {
		in.close(); 
		out.close(); 
	} // end ty
} // end Copiar

    
public void Seapara2(String strText)
{
    
    String xArre[] = strText.split(strSep);
    int x = strText.indexOf(strSep);
    
    if (x>0){
        t1 = xArre[0];
        t2 =  xArre[1];
    } 

}

public void Seapara3(String strText)
{
    String xArre[] = strText.split(strSep);
    int x = strText.indexOf(strSep);
    
    if (x>0){
        t1 = xArre[0];
        t2 =  xArre[1];
        t3 =  xArre[2];
    } 
}

public void Seapara6(String strText)
{
    String xArre[] = strText.split(strSep);
    int x = strText.indexOf(strSep);
    
    if (x>0){
        t1 = xArre[0];
        t2 =  xArre[1];
        t3 =  xArre[2];
        t4 =  xArre[3];
        t5 =  xArre[4];
        t6 =  xArre[5];
    } 
}


    public void GuardaEnter(String Ruta)
    {

          File archivo = null;
          FileReader fr = null;
          BufferedReader br = null;

          int N10 = 10;int N13 = 13;String e1013;
          String e10 = Character.toString((char)N10);
          String e13 = Character.toString((char)N13);
          e1013 = e13 + e10;

          //busca fila solo con enter 10 lo remplaza por enter 10 y 13

          try {
             // lee file
             archivo = new File (Ruta);
             fr = new FileReader (archivo);
             br = new BufferedReader(fr);

             // Lectura del fichero

             String linea;
             while((linea=br.readLine())!=null){
                System.out.println(linea);

                if (linea.indexOf(e10)>0 && linea.indexOf(e13)==0){
                    linea.replace(e10,e1013);
                }

             }

          }
          catch(Exception e){
             e.printStackTrace();
          }finally{
             // finaliza cierra file
             try{                    
                if( null != fr ){   
                   fr.close();     
                }                  
             }catch (Exception e2){ 
                e2.printStackTrace();
             }
          }

    }

    public String Comentario(String Linea_Actual){
        
        int intPos1; int intPos2;
        String TextOri; String TextSacar;

        TextOri = Linea_Actual;
        
        //quita continuidad de comentario
        if (bolCom == true && Linea_Actual.replace(" ", "").indexOf("*//*")>=0){
            intPos1 = Linea_Actual.indexOf("*/");
            intPos2 = Linea_Actual.indexOf("/*");
            if (intPos1 < intPos2){
                Linea_Actual = Linea_Actual.substring(0,intPos1) + Linea_Actual.substring(intPos2+2) ;
            }
        }

        //saca comentario de codigo
        if (Linea_Actual.indexOf("/*") >=0 || Linea_Actual.indexOf("*/") >=0){
            if (Linea_Actual.indexOf("/*") >=0 && Linea_Actual.indexOf("*/") >=0){ //eliminar lo contenido
                intPos1 = Linea_Actual.indexOf("/*");
                intPos2 = Linea_Actual.indexOf("*/");
                TextSacar = Linea_Actual.substring(intPos1,intPos2 + 2);
                Linea_Actual = Linea_Actual.replace(TextSacar, "");
                bolCom = false;
            }else{
                if (Linea_Actual.indexOf("/*") >=0 && Linea_Actual.indexOf("*/") < 0){
                    intPos1 = Linea_Actual.indexOf("/*");
                    TextSacar = Linea_Actual.substring(intPos1,Linea_Actual.length());
                    Linea_Actual = Linea_Actual.replace(TextSacar, "");
                    bolCom = true;
                }else{
                    intPos1 = Linea_Actual.indexOf("*/");
                    TextSacar = Linea_Actual.substring(0,intPos1+2);
                    Linea_Actual = Linea_Actual.replace(TextSacar, "");
                    bolCom = false;
                }
            }
        }else{
            if (bolCom == true){
                Linea_Actual = "";
            }else{
                bolCom=false;
            }
        }  
            
        return Linea_Actual;

    }

    public String ADAPTA_VARIABLE_SQL(String DATA,String tipo){
        //se usa en
        //t_invocaciones.TextFila
        //t_rutinas.codigo
  
        try {

        int inPos=0; String CarEsp=""; String TextRemplaza="";
        
        //recorrer arreglo
            for (int x=0; x < CarRes.length; x++) { 
                if (CarRes[x] != ""){
                    inPos = CarRes[x].indexOf(strSep);
                    CarEsp = CarRes[x].substring(0,inPos);
                    TextRemplaza = CarRes[x].substring(inPos + strSep.length());
                    
                    if (tipo=="ING"){
                        inPos = DATA.indexOf(CarEsp);
                        if (inPos >=0){
                            DATA =  DATA.replace(CarEsp, TextRemplaza); //remplaza caracteres especiales como \   '   "
                        }
                    }else{
                        inPos = DATA.indexOf(TextRemplaza);
                        if (inPos >=0){
                            DATA =  DATA.replace(TextRemplaza, CarEsp); //remplaza caracteres especiales como \   '   "
                        }
                    }
                    
                }
            } // end for

            return DATA;

        }
          catch(Exception e){
             e.printStackTrace();
             return DATA;
          }    
    }

    public String De_Num_a_Tx_01(double lNumero , 
                                   boolean bEntero , 
                                   int nDecimales ){

        String sNumero="";
        int nLong1;
        int nCont1;
        DecimalFormat formateador;

        if (bEntero==true){
            sNumero = String.valueOf((int)lNumero); 
        }else{

    //        DecimalFormat formateador = new DecimalFormat("######.##");
    //        //Este daria a la salida 1,000
    //        System.out.println (formateador.format (1000));
    //        //Este otro 10,000
    //        System.out.println (formateador.format (10000));
            switch (nDecimales) {
            case 1:
                formateador = new DecimalFormat("########0.#");
                sNumero = formateador.format (lNumero);
                break;
            case 2:
                formateador = new DecimalFormat("########0.0#");
                sNumero = formateador.format (lNumero);
                break;
            case 3:
                formateador = new DecimalFormat("########0.00#");
                sNumero = formateador.format (lNumero);
                break;
            }
        }

        return sNumero;

    }
}
