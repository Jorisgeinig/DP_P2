package Domeinklassen;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    private Connection conn;

    private AdresDAO adao;

    private OVChipkaartDAO odao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public boolean save(Reiziger reiziger) {
        try {
            String statementString = """
                    INSERT INTO reiziger (reiziger_id,
                                          voorletters,
                                          tussenvoegsel,
                                          achternaam,
                                          geboortedatum)
                    VALUES (?, ?, ?, ?, ?)
                     """;
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, reiziger.getReiziger_id());
            pst.setString(2, reiziger.getVoorletters());
            pst.setString(3, reiziger.getTussenvoegsel());
            if (reiziger.getTussenvoegsel() == "") {
                pst.setString(3, null);
            }
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, reiziger.getGeboortedatum());
            pst.execute();
            pst.close();
            if (this.adao != null && reiziger.getAdres() != null) {
                this.adao.save(reiziger.getAdres());
            }

            if (this.odao != null) {
                for (OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                    this.odao.save(ovChipkaart);
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error in save() ReizigerDAOPsql" + e);
            return false;
        }
    }

    public boolean update(Reiziger reiziger){
        try {
            String statementString = """
                    UPDATE reiziger 
                    SET voorletters = ?,
                        tussenvoegsel = ?,
                        achternaam = ?,
                        geboortedatum = ?
                    WHERE reiziger_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setString(1, reiziger.getVoorletters());
            pst.setString(2, reiziger.getTussenvoegsel());
            if (reiziger.getTussenvoegsel() == "") {
                pst.setString(2, null);
            }
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, reiziger.getGeboortedatum());
            pst.setInt(5, reiziger.getReiziger_id());
            pst.execute();
            pst.close();
            if (this.adao != null && reiziger.getAdres() != null) {
                this.adao.update(reiziger.getAdres());
            }

            if (this.odao != null && reiziger.getOvChipkaarten() != null) {
                for (OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                    this.odao.update(ovChipkaart);
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error in update() ReizigerDAOPsql" + e);
            return false;
        }
    }

    public boolean delete(Reiziger reiziger){
        try {
            if(this.adao != null && reiziger.getAdres() != null){
                this.adao.delete(reiziger.getAdres());
            }
            if(this.odao != null && reiziger.getOvChipkaarten() != null){
                for(OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()){
                    this.odao.delete(ovChipkaart);
                }
            }
            String statementString = """
                    DELETE FROM reiziger
                    WHERE reiziger_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, reiziger.getReiziger_id());
            pst.execute();
            pst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in delete() ReizigerDAOPsql" + e);
            return false;
        }
    }

    public Reiziger findById(int id) throws SQLException {
        String statementString = """
                SELECT * FROM reiziger
                 WHERE reiziger_id = ?""";
        PreparedStatement pst = conn.prepareStatement(statementString);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        rs.next();
        Reiziger reiziger = new Reiziger(rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboortedatum"));
        reiziger.setAdres(adao.findByReiziger(reiziger));
        return reiziger;
        }

    public List<Reiziger> findByGbdatum(String datum) {
        try {
            String statementString = """
                    SELECT * FROM reiziger
                     WHERE geboortedatum = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setDate(1, java.sql.Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();
            List<Reiziger> listReiziger = new ArrayList<Reiziger>();
            while (rs.next()) {
            listReiziger.add(new Reiziger(rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboortedatum")));
        }
            pst.close();
            return listReiziger;
        } catch (SQLException e) {
            System.err.println("Error in findByGbdatum() ReizigerDAOPsql" + e);
            return null;
        }
    }

    public List<Reiziger> findAll(){
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from reiziger");
            List<Reiziger> listReiziger = new ArrayList<>();
            while (rs.next()) {
                listReiziger.add(new Reiziger(rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum")));
            }
            statement.close();
            return listReiziger;
        } catch (SQLException e) {
            System.err.println("Error in findAll() ReizigerDAOPsql" + e);
            return null;
        }
        }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public AdresDAO getAdao() {
        return this.adao;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }
    public OVChipkaartDAO getOdao() {
        return this.odao;
    }
}
