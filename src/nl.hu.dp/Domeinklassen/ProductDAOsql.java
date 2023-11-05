package Domeinklassen;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOsql implements ProductDAO{
    private Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOsql(Connection conn) {
        this.conn = conn;
    }

    public boolean save(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?)");
            pst.setInt(1, product.getProduct_nummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.executeUpdate();
            pst.close();

            // Save into Linktable
            if(this.odao != null && product.getOvChipkaarten() != null) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    pst = conn.prepareStatement("INSERT INTO ov_chipkaart_product(kaart_nummer, product_nummer, last_update) VALUES (?, ?, ?)");
                    pst.setInt(1, ovChipkaart.getKaart_nummer());
                    pst.setInt(2, product.getProduct_nummer());
                    pst.setDate(3, Date.valueOf(LocalDate.now()));
                    pst.executeUpdate();
                    pst.close();
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error in save() ProductDAOPsql" + e);
            return false;
        }
    }

    public boolean update(Product product) {
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            pst.setString(1, product.getNaam());
            pst.setString(2, product.getBeschrijving());
            pst.setDouble(3, product.getPrijs());
            pst.setInt(4, product.getProduct_nummer());
            pst.execute();
            pst.close();
            if (this.odao != null) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    if (odao.findAll().contains(ovChipkaart)) {
                        pst = conn.prepareStatement("UPDATE ov_chipkaart_product SET last_update = ? WHERE kaart_nummer = ? AND product_nummer = ?");
                        pst.setDate(1, Date.valueOf(LocalDate.now()));
                        pst.setInt(2, ovChipkaart.getKaart_nummer());
                        pst.setInt(3, product.getProduct_nummer());
                        pst.execute();
                        pst.close();
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error in update() ProductDAOPsql" + e);
            return false;
        }
    }

    public boolean delete(Product product) {
        try {
            if (odao != null && product.getOvChipkaarten() != null) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    PreparedStatement pst = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ? AND product_nummer = ?");
                    pst.setInt(1, ovChipkaart.getKaart_nummer());
                    pst.setInt(2, product.getProduct_nummer());
                    pst.execute();
                    pst.close();
                }
            }
        PreparedStatement pst = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
        pst.setInt(1, product.getProduct_nummer());
        pst.execute();
        pst.close();
        return true;
        } catch (SQLException e) {
            System.err.println("Error in delete() ProductDAOPsql" + e);
            return false;
        }
    }

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        try {
            List<Product> productenLijst = new ArrayList<>();
            String query = """
                    SELECT product.product_nummer, product.naam, product.beschrijving, product.prijs
                    FROM product
                    JOIN ov_chipkaart_product ovp ON product.product_nummer = ovp.product_nummer
                    Join ov_chipkaart ov on  ov.kaart_nummer = ovp.kaart_nummer
                    WHERE ovp.kaart_nummer = ?""";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"), rs.getDouble("prijs"));
                productenLijst.add(product);
            }
            rs.close();
            pst.close();
            return productenLijst;
        } catch (SQLException e) {
            System.err.println("Error in findByProductnummer() ProductDAOPsql" + e);
            return null;
        }
    }

    public List<Product> findAll() {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT product_nummer, naam, beschrijving, prijs FROM product");
            ResultSet rs = pst.executeQuery();
            List<Product> productenLijst = new ArrayList<>();
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"), rs.getDouble("prijs"));
                productenLijst.add(product);
            }
            rs.close();
            pst.close();
            return productenLijst;
        } catch (SQLException e) {
            System.err.println("Error in findAll() ProductDAOPsql" + e);
            return null;
        }
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    public OVChipkaartDAO getOdao() {
        return odao;
    }


}
