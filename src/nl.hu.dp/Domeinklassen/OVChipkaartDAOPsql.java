package Domeinklassen;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements  OVChipkaartDAO{
    private Connection conn;
    private ReizigerDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public boolean save(OVChipkaart ovChipkaart){
            try {
            String statementString = """
                    INSERT INTO ov_chipkaart (kaart_nummer,
                                              geldig_tot,
                                              klasse,
                                              saldo,
                                              reiziger_id)
                    VALUES (?, ?, ?, ?, ?)
                     """;
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            pst.setDate(2, ovChipkaart.getGeldig_tot());
            pst.setInt(3, ovChipkaart.getKlasse());
            pst.setDouble(4, ovChipkaart.getSaldo());
            pst.setInt(5, ovChipkaart.getReizigerid());
            pst.execute();
            pst.close();
            return true;
            }
            catch (SQLException e) {
                System.err.println("Error in save() OVChipkaartDAOPsql" + e);
                return false;
            }

        }

        public boolean update(OVChipkaart ovChipkaart){
        try {
                String statementString = """
                        UPDATE ov_chipkaart 
                        SET geldig_tot = ?,
                            klasse = ?,
                            saldo = ?,
                            reiziger_id = ?
                        WHERE kaart_nummer = ?""";
                PreparedStatement pst = conn.prepareStatement(statementString);
                pst.setDate(1, ovChipkaart.getGeldig_tot());
                pst.setInt(2, ovChipkaart.getKlasse());
                pst.setDouble(3, ovChipkaart.getSaldo());
                pst.setInt(4, ovChipkaart.getReizigerid());
                pst.setInt(5, ovChipkaart.getKaart_nummer());
                pst.execute();
                pst.close();
                return true;
                }
            catch (SQLException e) {
                System.err.println("Error in update() OVChipkaartDAOPsql" + e);
                return false;
            }
        }

        public boolean delete(OVChipkaart ovChipkaart) {
            try {
                String statementString = """
                        DELETE FROM ov_chipkaart
                    WHERE kaart_nummer = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            pst.execute();
            pst.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in delete() OVChipkaartDAOPsql" + e);
            return false;
        }
    }

    public OVChipkaart findbyKaartNummer(int kaart_nummer) {
        try {
            String statementString = """
                    SELECT geldig_tot,
                           klasse,
                           saldo,
                           reiziger_id
                    FROM ov_chipkaart
                    WHERE kaart_nummer = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, kaart_nummer);
            ResultSet rs = pst.executeQuery();
            OVChipkaart ovChipkaart = null;
            while (rs.next()) {
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int reiziger_id = rs.getInt("reiziger_id");
                ovChipkaart = new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id);
                ovChipkaart.setReizigerid(reiziger_id);
            }
            rs.close();
            pst.close();
            return ovChipkaart;
        } catch (SQLException e) {
            System.err.println("Error in findbyKaartNummer() OVChipkaartDAOPsql" + e);
            return null;
        }
    }


    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        try {
            String statementString = """
                    SELECT kaart_nummer,
                           geldig_tot,
                           klasse,
                           saldo,
                           reiziger_id
                    FROM ov_chipkaart
                    WHERE reiziger_id = ?""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            pst.setInt(1, reiziger.getReiziger_id());
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> ovChipkaarten = new ArrayList<>();
            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int reiziger_id = rs.getInt("reiziger_id");
                OVChipkaart ovChipkaart = new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id);
                ovChipkaarten.add(ovChipkaart);
            }
            rs.close();
            pst.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.err.println("Error in findByReiziger() OVChipkaartDAOPsql" + e);
            return null;
        }
    }

        public List<OVChipkaart> findAll(){
        try {
            String statementString = """
                    SELECT kaart_nummer,
                           geldig_tot,
                           klasse,
                           saldo,
                           reiziger_id
                    FROM ov_chipkaart""";
            PreparedStatement pst = conn.prepareStatement(statementString);
            ResultSet rs = pst.executeQuery();
            List<OVChipkaart> ovChipkaarten = new ArrayList<>();
            while (rs.next()) {
                int kaart_nummer = rs.getInt("kaart_nummer");
                Date geldig_tot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                float saldo = rs.getFloat("saldo");
                int reiziger_id = rs.getInt("reiziger_id");
                OVChipkaart ovChipkaart = new OVChipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id);
                ovChipkaart.setReizigerid(reiziger_id);
                ovChipkaarten.add(ovChipkaart);
            }
            rs.close();
            pst.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.err.println("Error in findAll() OVChipkaartDAOPsql" + e);
            return null;
        }
        }

        public ReizigerDAO getRdao() {
            return rdao;
        }

        public void setRdao(ReizigerDAO rdao) {
            this.rdao = rdao;
        }

        public ProductDAO getPdao() {
            return pdao;
        }

        public void setPdao(ProductDAO pdao) {
            this.pdao = pdao;
        }

}
