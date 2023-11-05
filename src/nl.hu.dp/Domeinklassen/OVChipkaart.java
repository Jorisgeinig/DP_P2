package Domeinklassen;

import java.sql.Date;
import java.util.List;

public class OVChipkaart {
    private int kaart_nummer;
    private Date geldig_tot;
    private int klasse;
    private double saldo;
    private int reizigerid;;

    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, double saldo, int reizigerid) {
        this.kaart_nummer = kaart_nummer;
        this.geldig_tot = geldig_tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerid = reizigerid;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }
    public int getKlasse() {
        return klasse;
    }
    public double getSaldo() {
        return saldo;
    }
    public int getReizigerid() {
        return reizigerid;
    }
    public void setReizigerid(int reizigerid) {
        this.reizigerid = reizigerid;
    }

    public String toString() {
        return "OVChipkaart{" +
                "kaart_nummer=" + kaart_nummer +
                ", geldig_tot=" + geldig_tot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger_id=" + reizigerid +
                '}';
    }
}
