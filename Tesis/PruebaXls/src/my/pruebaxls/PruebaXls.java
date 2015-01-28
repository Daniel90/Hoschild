package my.pruebaxls;
/**
 *
 * @author D.Abrilot
 */

import java.sql.*;
import java.text.NumberFormat.*;

import java.util.Calendar;

import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

public class PruebaXls {

    /**
     * Crea una hoja Excel y la guarda con datos filtrados.
     * author: Daniel
     * @param args
     */

    //metodo que verifica si un string es un número
    public static boolean verifica(String dato){
        try{
            Float.parseFloat(dato);       
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
    
    //metodo que llena el libro excel y retorn "i", para verificar si fueron guardados datos
    public static int llena(HSSFRow fila, int i, ResultSet result, HSSFSheet hoja, HSSFCellStyle style, HSSFCellStyle estiloNumero) throws SQLException{
        String[] datos = {"SUCS01","NPAR01","PROC01","DESC01",
                          "STKF01","UNIM01","TRNS01","RNVN01",
                          "RFAT01","STKV01","MINM01","MAXM01",
                          "TPCL01","SLDF01"};  
        while(result.next()){
            // Se crea una fila dentro de la hoja
            fila = hoja.createRow(i);  
            for(int next = 0;next<datos.length;next++){
                HSSFCell celda = fila.createCell((short) next);
                HSSFRichTextString texto = new HSSFRichTextString(result.getString(datos[next]));
                celda.setCellValue(texto);
                celda.setCellStyle(style);
                if(verifica(result.getString(datos[next]))){
                    celda.setCellStyle(estiloNumero);
                }
            }
            i++;      
        }
        return i;
    } 
    
    public static void main(String [] args) {
        // Se crea el libro
        HSSFWorkbook libro = new HSSFWorkbook();
        String[] cabecera = {"SUCURSAL","N° DE PARTE","PROCEDENCIA","DESCRIPCIÓN",
                             "STOCK FISICO","UNIDAD DE MEDIDA","TRANSITO",
                             "RESERVA POR NOTA DE VENTA","RESERVA POR FACTURACIÓN ANTICIPADA",
                             "STOCK VIRTUAL","MINIMO","MAXIMO","TIPO CALCULO","SALDO FINAL"};
        String[] sheet = {"Santiago","Temuco","Puerto Montt","Antofagasta","Hualpen","Copiapó"};
        //Para la cabecera y las hojas
                                                                 

        //Creo conexion al AS400 por cada archivo se hace una conexion
        Connection conectado401 = null;
        //String database401 = "amsdsgr.phpven";
        String database401 = "atgdsgr.rsldsk01";
        String ipserver = "192.168.2.118";
        String user = "USRCLIENTE";
        String pass = "CLIENTE";

        //creo variable que tendra el numero de fila
        int i=1;

        try{
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            String urlconexion401 = "jdbc:as400://" + ipserver + "/" + database401;
            conectado401 = DriverManager.getConnection(urlconexion401, user, pass);

            String sql401 = "select * from " + database401 + " where sucs01 = 1 and stkv01 < minm01";
            String sql402 = "select * from " + database401 + " where sucs01 = 2 and stkv01 < minm01";
            String sql403 = "select * from " + database401 + " where sucs01 = 3 and stkv01 < minm01";
            String sql405 = "select * from " + database401 + " where sucs01 = 5 and stkv01 < minm01";
            String sql406 = "select * from " + database401 + " where sucs01 = 6 and stkv01 < minm01";
            String sql407 = "select * from " + database401 + " where sucs01 = 7 and stkv01 < minm01";

            Statement s401 = conectado401.createStatement();
            Statement s402 = conectado401.createStatement();
            Statement s403 = conectado401.createStatement();
            Statement s405 = conectado401.createStatement();
            Statement s406 = conectado401.createStatement();
            Statement s407 = conectado401.createStatement();

            ResultSet res401 = s401.executeQuery(sql401);
            ResultSet res402 = s402.executeQuery(sql402);
            ResultSet res403 = s403.executeQuery(sql403);
            ResultSet res405 = s405.executeQuery(sql405);
            ResultSet res406 = s406.executeQuery(sql406);
            ResultSet res407 = s407.executeQuery(sql407);
                
            //comienzo la conexion
             try{         
                //por hojas
                for(int j = 0;j<6;j++){
                    HSSFSheet hoja = libro.createSheet(sheet[j]);
                
                    //estilo general del libro, de la cabecera y de los tipo numericos
                    HSSFCellStyle style=libro.createCellStyle();
                    HSSFCellStyle estiloCabecera = libro.createCellStyle();
                    HSSFCellStyle estiloNumero=libro.createCellStyle();
                    
                    
                    estiloNumero.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    estiloNumero.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    estiloNumero.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloNumero.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    estiloNumero.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    
                    //estilo de letra de la cabecera
                    HSSFFont font = libro.createFont();
                    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    
                    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    
                    estiloCabecera.setFillForegroundColor(HSSFColor.YELLOW.index);
                    estiloCabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    estiloCabecera.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    estiloCabecera.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloCabecera.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    estiloCabecera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCabecera.setFont(font);
                    
                    HSSFRow fila = hoja.createRow(0);
                    hoja.createFreezePane(0, 1);
                    
                    //recorremos cabecera insertando los titulos
                    for(int k = 0;k<cabecera.length;k++){

                        HSSFCell celda = fila.createCell((short) k);
                        HSSFRichTextString texto = new HSSFRichTextString(cabecera[k]);
                        celda.setCellValue(texto);
                        celda.setCellStyle(estiloCabecera);
                        hoja.autoSizeColumn(k); 
                    }
                    //dependiendo de la hoja en que este, ira insertando distintas consultas
                    if(j == 0){
                        i = llena(fila,i,res401,hoja,style,estiloNumero);
                    }
                    else if(j==1){
                        i = 1;
                        i = llena(fila,i,res402,hoja,style,estiloNumero);
                    }
                    else if(j==2){
                        i = 1;
                        i = llena(fila,i,res403,hoja,style,estiloNumero);
                    }
                    else if(j==3){
                        i = 1;
                        i = llena(fila,i,res405,hoja,style,estiloNumero);
                    }
                    else if(j==4){
                        i = 1;
                        i = llena(fila,i,res406,hoja,style,estiloNumero);
                    }
                    else if(j==5){
                        i = 1;
                        i = llena(fila,i,res407,hoja,style,estiloNumero);
                    }
                       
                }
                System.out.println(i);
                
                s401.close();
                s402.close();
                s403.close();
                s405.close();
                s406.close();
                s407.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }catch(Exception ex){
                ex.printStackTrace();
        }


// Se salva el libro solo si se ha grabado una celda para enviar la informacion por email
// Con ello aseguramos que el correo sera enviado como una alerta ya que si no tienen datos la consulta no entrara               
        try {
            if(i>1){
                Calendar fecha = Calendar.getInstance();
                int anoA = fecha.get(Calendar.YEAR);
                int mesA = fecha.get(Calendar.MONTH);
                int diaA = fecha.get(Calendar.DAY_OF_MONTH);
                mesA = mesA + 1;
                FileOutputStream elFichero = new FileOutputStream("C:\\\\historial\\\\Alerta_"+diaA+"_"+mesA+"_"+anoA+".xls");
                libro.write(elFichero);
                elFichero.close();
                //envioCorreo.envio("C:\\\\historial\\\\Alerta_"+diaA+"_"+mesA+"_"+anoA+".xls","Alerta_"+diaA+"_"+mesA+"_"+anoA+".xls");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


}

}