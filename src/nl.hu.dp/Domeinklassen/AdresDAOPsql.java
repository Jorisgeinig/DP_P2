package Domeinklassen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }

    public boolean save(Adres adres) {
        try {
            String statementString = """
                    INSERT INTO adres (adres_id,
                                          postcode,
                                          huisnummer,
                                          straat,
                                          woonplaats,
                                          reiziger_id)
                    VALUES (?, ?, ?, ?, ?, ?)
                     """;
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, adres.getAdres_id());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReizigerid());
            pst.execute();
            pst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in save() AdresDAOPsql" + e);
            return false;
        }
    }

    public boolean update(Adres adres) {
        try{
            String statementString = """
                    UPDATE adres 
                    SET postcode = ?,
                        huisnummer = ?,
                        straat = ?,
                         woonplaats = ?,
                         reiziger_id = ?
                        
                    WHERE adres_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReizigerid());
            pst.setInt(6, adres.getAdres_id());
            pst.execute();
            pst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in update() AdresDAOPsql" + e);
            return false;
        }
    }

    public boolean delete(Adres adres) {
        try{
            String statementString = """
                    DELETE FROM adres
                    WHERE adres_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, adres.getAdres_id());
            pst.executeUpdate();
            pst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in delete() AdresDAOPsql" + e);
            return false;
        }
    }

    public Adres findByReiziger(Reiziger reiziger) {
        try {
            String statementString = """     
                SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id FROM adres
             WHERE reiziger_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, reiziger.getReiziger_id());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Adres adres = new Adres(rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id"));
                pst.close();
                rs.close();
                return adres;
            } else {
                pst.close();
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error in findByReiziger() AdresDAOPsql" + e);
            return null;
        }
    }

    public List<Adres> findAll() {
        List<Adres> listAdres = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from adres");
            while (rs.next()) {
                listAdres.add(new Adres(rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id")));
            }
            statement.close();
            rs.close();
            return listAdres;
        } catch (SQLException e) {
            System.err.println("Error in findAll() AdresDAOPsql" + e);
            return listAdres;
        }
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public ReizigerDAO getRdao() {
        return this.rdao;
    }
}
