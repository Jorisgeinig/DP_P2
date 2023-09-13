package Main;

import Domeinklassen.Reiziger;
import Domeinklassen.ReizigerDAO;
import Domeinklassen.ReizigerDAOPsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "Joris999!";
        connection = DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");


        // Maak nieuw reiziger, persisteer die en wijzig die vervolgens met een andere reiziger
        System.out.print("[Test] Eerst word er een nieuwe reiziger aangemaakt, op dit moment zijn er " + reizigers.size() + " reizigers");
        Reiziger wopke = new Reiziger(55, "W", "", "Hoekstra", java.sql.Date.valueOf("1976-04-20"));
        rdao.save(wopke);
        System.out.print("\nNu zijn er: " + reizigers.size() + " reizigers, na ReizigerDAO.save() , deze reiziger wordt gewijzigd: " + rdao.findById(55));
        Reiziger wopkeVervanger = new Reiziger(55, "S", "", "Kaag", java.sql.Date.valueOf("1966-03-19"));
        rdao.update(wopkeVervanger);
        System.out.print("\nNa ReizigerDAO.update() is de reiziger met id 55 geupdate naar: " + rdao.findById(55));

        Reiziger mark = new Reiziger(99, "M", "", "Rutte", java.sql.Date.valueOf("1967-02-10"));
        rdao.save(mark);
        reizigers = rdao.findAll();
        System.out.println("\n\n[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(mark);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        System.out.println("[TEST] Zoek reiziger(s) o.b.v geboortedatum: " + (rdao.findByGbdatum("2002-12-03")));
    }


    public static void main(String[] args) {
        try {
            getConnection();
        } catch(SQLException noCon) {
            System.out.println("Something went wrong establishing the connection with the database: " + noCon);
        }

        ReizigerDAOPsql rdao = new ReizigerDAOPsql(connection);

        try {
            testReizigerDAO(rdao);
        }
        catch(NullPointerException | SQLException e) {
            System.out.println("Something went wrong: " + e);
        }
    }
}
