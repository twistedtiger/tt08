/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cardsagainsthumanity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;

/**
 *
 * @author thoma_000
 */
public class SQLHandler {
    
    /*
    public String joinGame(String oauth) throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");
        String dbName = "C:\\CAH\\CAH-Black.db";
        String Jdbc = "jdbc:sqlite";
        String DbUrl = Jdbc + ":" + dbName;
        
        String makeSelect = "SELECT * FROM games";
        
        
        Connection con = DriverManager.getConnection(DbUrl);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(makeSelect);
        
        while(rs.next()){
            String sResult = rs.getString("authcode");
            if(sResult.equals(oauth)){
                String sIP = rs.getString("ip");
                int sID = Integer.parseInt(rs.getString("id"));
                int sPlayers = Integer.parseInt(rs.getString("players"));
                if(sPlayers<6){
                //System.out.println("RESULT: "+sResult+" @ " + sIP);
                //System.out.println(rs.getRow());
                rs.close();
                sPlayers++;
               int updateGame = st.executeUpdate("INSERT INTO games VALUES("+sID+",'"+sIP+"','"+sResult+"',"+sPlayers+")");
               if(updateGame==1){
                   System.out.println("Game updated");
               }else{
                   System.out.println("Problems updating game status");
               }
               return sIP;
                }else{
                    Object[] ob = {"Game is already full"};
                    JOptionPane.showMessageDialog(null, ob);
                }
            }
        }
        rs.close();
        st.close();
        con.close();
        return null;
    }
    */
    
    
    public String generateAuthCode(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest md = null;
        md = MessageDigest.getInstance("SHA");
        md.update(plaintext.getBytes(("UTF-8")));
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
    
    public String getRemote(){
        String printline = "";
        String p1 = "";
        String p2 = "";
        try{
        URL url = new URL("http://ipaddress.com/");
        URLConnection con = url.openConnection();
        BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String readLine;
        while((readLine = read.readLine())!=null){
            //System.out.println(readLine);
            if(readLine.contains("<tr><th>")){
                String[] name2 = readLine.split("<tr><th>");
                String[] name = name2[1].split("</th><td");
                if(readLine.contains("bold")){
                    String[] ip = name[1].substring(14).split("</td></tr>");
                    printline = name[0] + " : " + ip[0];
                    p1 = name[0];
                    p2 = ip[0];
                    return p2;
                }
            }
        }
        read.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
}
