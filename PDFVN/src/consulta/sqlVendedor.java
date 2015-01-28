/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package consulta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import consulta.sqlVendedor2;
/**
 *
 * @author AMaroevich
 */
public class sqlVendedor {
    public ResultSet consultaVendedor(int fechaInicial, int fechaFinal,int mes,int ano){
        ResultSet resultadoSql = null;
        
        Connection punteroConexion = null;
        String database_clmhp403 = "mhexidat.mhr057pf";  //base de datos

        String ipserver = "192.168.2.118";
        String user = "USRCLIENTE";
        String pass = "CLIENTE";
        
        try{
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver());
            String urlconexion_clmhp403 = "jdbc:as400://" + ipserver + "/" + database_clmhp403;
            try{
                punteroConexion = DriverManager.getConnection(urlconexion_clmhp403, user, pass);
          
                        String sql_clmhp403 = "select famf57,desf57,kilf57,vtaf57,pref57,pmmf57,paaf57 from " + database_clmhp403 +" where pmmf57 = "+mes+" and paaf57 = "+ano;
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
