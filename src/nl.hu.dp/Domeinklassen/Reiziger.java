package Domeinklassen;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.text.SimpleDateFormat;

public class Reiziger {
    private int reiziger_id;
    private String voorletters;
    private String tussenvoegsel = null;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();

    public Reiziger(){
    }

    public Reiziger(int reiziger_id, String voorletters,
                     String achternaam, Date geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.achternaam = achternaam;
        this.voorletters = voorletters;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger(int reiziger_id, String voorletters,
                    String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.reiziger_id = reiziger_id;
        this.achternaam = achternaam;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.geboortedatum = geboortedatum;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public Adres getAdres() {return adres;}

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
    }

    public void addOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.add(ovChipkaart);
    }

    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Choose the desired date format
        String formattedDate = dateFormat.format(geboortedatum);


        String voorletters_metpunt = "";
        for (int i = 0; i < voorletters.length(); i++) {
            voorletters_metpunt += voorletters.charAt(i);

            if (i < voorletters.length() - 1) {
                voorletters_metpunt += ".";
            }
        }
        if (voorletters.length() > 1) {
            voorletters_metpunt += ".";
        }
        if (voorletters.length() == 1) {
            voorletters_metpunt += ".";
        }

        String resultaat = String.format("Reiziger #%d %s%s %s (%s), Adres {#%d %s %s}",
                reiziger_id,
                voorletters_metpunt,
                (tussenvoegsel != null) ? " " + tussenvoegsel : "",
                achternaam,
                formattedDate,
                adres.getAdres_id(),
                adres.getPostcode(),
                adres.getHuisnummer());
        return resultaat;
    }
}
