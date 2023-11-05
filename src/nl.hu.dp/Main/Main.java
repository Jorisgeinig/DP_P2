package Main;

import Domeinklassen.*;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
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

    private static void testAdres(AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Voorbeeld reiziger om het adres aan te koppelen
        Reiziger wopke = new Reiziger(88, "W", "", "Hoekstra", java.sql.Date.valueOf("1976-04-20"));
        adao.getRdao().save(wopke);

        //Maak een adres aan en save deze in de database
        Adres adr1 = new Adres(6, "8834IK", "34", "Hogeweg", "Hoevelaken", 88);
        System.out.println("[Test] Eerst " + adao.findAll().size() + " adressen");
        adao.save(adr1);
        System.out.println("na adao.save: " + adao.findAll().size() + " adressen\n");

        //Test updaten van adres en test methode findByReiziger()
        Adres adr2 = new Adres(6, "2315IK", "32", "huhuhu", "Rotterdeam", 88);
        System.out.println("[TEST] update, adres: " + adao.findByReiziger(wopke));
        adao.update(adr2);
        System.out.println("na update: " + adao.findByReiziger(wopke));

        //Test delete van adres
        System.out.println("\n[TEST] delete, eerst " + adao.findAll().size() + " adressen" );
        adao.delete(adr1);
        //Delete ook de reiziger die is aangemaakt voor het adres
        adao.getRdao().delete(wopke);
        System.out.println("Na adao.delete: " + adao.findAll().size() + " adressen\n");

        //Tot slot een overzicht van alle reizigers met de adressen.
        System.out.println("overzicht van alle reizigers met adressen na de tests:");
        for (Adres adres : adao.findAll()) {
            System.out.println(adao.getRdao().findById(adres.getReizigerid()));
        }
    }

    private static void testOVChipkaarten(OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        // Voorbeeld reiziger om de ovchipkaarten aan te koppelen
        Reiziger reiziger1 = new Reiziger(10, "P", "", "Lopemdam", java.sql.Date.valueOf("1986-03-14"));
        odao.getRdao().save(reiziger1);

        OVChipkaart ov1 = new OVChipkaart(12345, java.sql.Date.valueOf("2021-01-01"), 1, 25.00, 10);
        OVChipkaart ov2 = new OVChipkaart(48945, java.sql.Date.valueOf("2022-01-01"), 1, 30.00, 10);

        // Test save van ovchipkaart
        System.out.println("[TEST] eerst " + odao.findAll().size() + " ovchipkaarten");
        odao.save(ov1);
        System.out.println("na odao.save: " + odao.findAll().size() + " ovchipkaarten\n");

        // Koppel de ovchipkaarten aan de reiziger in java
        List<OVChipkaart> listOV = List.of(ov1, ov2);
        reiziger1.setOvChipkaarten(listOV);

        // Test update van ovchipkaart
        System.out.println("[TEST] update, ovchipkaart:\n " + odao.findbyKaartNummer(12345));
        OVChipkaart ov3 = new OVChipkaart(12345, java.sql.Date.valueOf("2022-01-01"), 2, 50, 10);
        odao.update(ov3);
        System.out.println("na update: " + odao.findbyKaartNummer(12345));

        // Test findAll van ovchipkaart
        System.out.println("\n[TEST] findAll() geeft de volgende OVChipkaarten:\n");
        for (OVChipkaart ov : odao.findAll()) {
            System.out.println(ov.toString());
        }
        System.out.println();

        // Test findByReiziger van ovchipkaart
        System.out.println("[TEST] findByReiziger() geeft de volgende OVChipkaarten:");
        for (OVChipkaart ov : odao.findByReiziger(reiziger1)) {
            System.out.println(ov.toString());
        }

        // Test delete van ovchipkaart
        System.out.println("\n[TEST] delete, eerst " + odao.findAll().size() + " ovchipkaarten" );
        odao.getRdao().findById(ov1.getReizigerid()).getOvChipkaarten().remove(ov1);
        odao.delete(ov1);
        System.out.println("Na odao.delete: " + odao.findAll().size() + " ovchipkaarten\n");

        // delete de aangemaakte reiziger
        odao.getRdao().delete(reiziger1);
    }

    private static void testProductDAO(ProductDAO pdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");


        // Slaat een ovchipkaart aan om mee te testen
        OVChipkaart ov7 = new OVChipkaart(77777, java.sql.Date.valueOf("2021-01-01"), 1, 50.00, 5);
        pdao.getOdao().save(ov7);

        // Slaat een nieuw product op in de database en koppelt deze aan de ovchipkaart
        Product product1 = new Product(7, "Weekend Vrij", "Gratis reizen in het weekend", 0.00);
        Product product2 = new Product(8, "Alleen staan", "Alleen staan in het ov", 0.00);
        ov7.addProduct(product1);
        ov7.addProduct(product2);
        System.out.println("[TEST] eerst " + pdao.findAll().size() + " producten");
        pdao.save(product1);
        pdao.save(product2);
        System.out.println("na twee keer pdao.save: " + pdao.findAll().size() + " producten\n");

        // Test findByOVChipkaart van product
        System.out.println("[TEST] findByOVChipkaart() geeft de volgende producten:");
        for (Product product : pdao.findByOVChipkaart(ov7)) {
            System.out.println(product.toString());
        }

        // Test update van product
        System.out.println("[TEST] update, product:\n " + product1);
        Product product3 = new Product(7, "Doordeweeks vrij", "Gratis reizen doordeweeks", 199.00);
        pdao.update(product3);
        System.out.println("na update: " + pdao.findByOVChipkaart(ov7).get(1));

        // Test findAll van product
        System.out.println("\n[TEST] findAll() geeft de volgende producten:\n");
        for (Product product : pdao.findAll()) {
            System.out.println(product.toString());
        }

        // Test delete van product
        System.out.println("\n[TEST] delete, eerst " + pdao.findAll().size() + " producten" );
        pdao.delete(product1);
        pdao.delete(product2);
        System.out.println("Na 2 keer pdao.delete: " + pdao.findAll().size() + " producten\n");

        // delete de aangemaakte ovchipkaart
        pdao.getOdao().delete(ov7);
    }


    public static void main(String[] args) {
        try {
            getConnection();
        } catch(SQLException noCon) {
            System.out.println("Something went wrong establishing the connection with the database: " + noCon);
        }

        ReizigerDAOPsql rdao = new ReizigerDAOPsql(connection);
        AdresDAOPsql adao = new AdresDAOPsql(connection);
        OVChipkaartDAOPsql odao = new OVChipkaartDAOPsql(connection);
        ProductDAOsql pdao = new ProductDAOsql(connection);
        rdao.setAdao(adao);
        adao.setRdao(rdao);

        odao.setRdao(rdao);
        rdao.setOdao(odao);

        odao.setPdao(pdao);
        pdao.setOdao(odao);

        try {

            testReizigerDAO(rdao);
            testAdres(adao);
            testOVChipkaarten(odao);
            testProductDAO(pdao);

        }
        catch(NullPointerException | SQLException e) {
            System.out.println("Something went wrong: " + e);
        }
    }
}
