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

    public boolean save(Adres adres) throws SQLException {
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
        pst.setInt(6, adres.getReiziger().getReiziger_id());
        int rowCount = pst.executeUpdate();
        pst.close();
        return rowCount > 0;
    }

    public boolean update(Adres adres) throws SQLException {
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
        pst.setInt(5, adres.getReiziger().getReiziger_id());
        pst.setInt(6, adres.getAdres_id());
        int rowCount = pst.executeUpdate();
        pst.close();
        return rowCount > 0;
    }

    public boolean delete(Adres adres) throws SQLException {
        String statementString = """
                DELETE FROM adres
                WHERE adres_id = ?""";
        PreparedStatement pst = conn.prepareStatement(statementString);
        pst.setInt(1, adres.getAdres_id());
        int rowCount = pst.executeUpdate();
        pst.close();

        return rowCount > 0;

    }

    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String statementString = """
                SELECT * FROM adres
                 WHERE reiziger_id = ?""";
        PreparedStatement pst = conn.prepareStatement(statementString);
        pst.setInt(1, reiziger.getReiziger_id());
        ResultSet rs = pst.executeQuery();
        rs.next();
        return new Adres(rs.getInt("adres_id"),
                rs.getString("postcode"),
                rs.getString("huisnummer"),
                rs.getString("straat"),
                rs.getString("woonplaats"),
                rdao.findById(rs.getInt("reiziger_id")));
    }

    public List<Adres> findAll() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("select * from adres");
        List<Adres> listAdres = new ArrayList<>();
        while (rs.next()) {
            listAdres.add(new Adres(rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rdao.findById(rs.getInt("reiziger_id"))));
        }
        statement.close();
        return listAdres;
    }

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public ReizigerDAO getRdao() {
        return this.rdao;
    }
}
