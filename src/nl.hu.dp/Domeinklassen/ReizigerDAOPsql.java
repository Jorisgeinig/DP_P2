package Domeinklassen;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    public Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }
    public boolean save(Reiziger reiziger) throws SQLException {
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
        int rowCount = pst.executeUpdate();

        pst.close();

        return rowCount > 0;
    }

    public boolean update(Reiziger reiziger) throws SQLException {
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
            pst.setString(2, null);}
        pst.setString(3, reiziger.getAchternaam());
        pst.setDate(4, reiziger.getGeboortedatum());
        pst.setInt(5, reiziger.getReiziger_id());
        int rowCount = pst.executeUpdate();
        pst.close();
        return rowCount > 0;
    }

    public boolean delete(Reiziger reiziger) throws SQLException {
        String statementString = """
                DELETE FROM reiziger
                WHERE reiziger_id = ?""";
        PreparedStatement pst = conn.prepareStatement(statementString);
        pst.setInt(1, reiziger.getReiziger_id());
        int rowCount = pst.executeUpdate();
        pst.close();
        return rowCount > 0;
    }

    public Reiziger findById(int id) throws SQLException {
        String statementString = """
                SELECT * FROM reiziger
                 WHERE reiziger_id = ?""";
        PreparedStatement pst = conn.prepareStatement(statementString);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        rs.next();
        return new Reiziger(rs.getInt("reiziger_id"),
                rs.getString("voorletters"),
                rs.getString("tussenvoegsel"),
                rs.getString("achternaam"),
                rs.getDate("geboortedatum"));
        }

    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
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
    }

    public List<Reiziger> findAll() throws SQLException {
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
    }
}
