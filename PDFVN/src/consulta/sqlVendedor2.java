/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consulta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Daniel
 * PDF que muestra las familias desplegadas por vendedor a nivel nacional
 */
public class sqlVendedor2 {
    public ResultSet consultaVendedor2(int fechaInicial, int fechaFinal,int mes,int ano){
        ResultSet resultadoSql = null;
        
        Connection punteroConexion = null;
        String database_clmhp403 = "mhexidat.mhr057pv";  //base de datos
        String database_clmhp403b = "mhexidat.mhr057pf";
        
        String ipserver = "192.168.2.118";
        String user = "USRCLIENTE";
        String pass = "CLIENTE";
        
        try{
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            String urlconexion_clmhp403 = "jdbc:as400://" + ipserver + "/" + database_clmhp403;
            try{
                punteroConexion = DriverManager.getConnection(urlconexion_clmhp403, user, pass);
          
                        String sql_clmhp403 = "select famv57,cvev57,nven57,kilv57,vtav57,prev57,pmmv57,paav57 from " + database_clmhp403 +" where pmmv57 = "+mes+" and paav57 = "+ano+" order by vtav57 DESC";
                        System.out.println("verficar consulta: " + sql_clmhp403);
                        Statement s_clmhp403 = punteroConexion.createStatement();
                        resultadoSql = s_clmhp403.executeQuery(sql_clmhp403);    
                }catch(Exception e){
                e.printStackTrace();
            }        
                    
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultadoSql;
    }
}
