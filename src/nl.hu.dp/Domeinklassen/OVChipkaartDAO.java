package Domeinklassen;

import java.sql.SQLException;
import java.util.List;
public interface OVChipkaartDAO {
    public boolean save(OVChipkaart ovChipkaart) throws SQLException;
    public boolean update(OVChipkaart ovChipkaart) throws SQLException;
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    public OVChipkaart findbyKaartNummer(int kaart_nummer) throws SQLException;
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    public List<OVChipkaart> findAll() throws SQLException;
    public ReizigerDAO getRdao();
}
