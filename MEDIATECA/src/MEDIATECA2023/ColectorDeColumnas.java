package MEDIATECA2023;
import java.util.HashMap;
import java.util.Map;


//ESTO ES PARA BUSCAR COMO SE LLAMA CADA COLUMNA EN ESPECÍFICO
public class ColectorDeColumnas {
    private Map<String, String> tableNameToDisp;
    private Map<String, String> tableNameToPrimaryKey;
    private Map<String, String> tableNameToJoin;

    public ColectorDeColumnas() {
        tableNameToDisp = new HashMap<>();
        tableNameToDisp.put("cd", "cdDisp");
        tableNameToDisp.put("dvds", "dvdDisp");
        tableNameToDisp.put("revistas", "revistasDisp");
        tableNameToDisp.put("libro", "librosDisp");

        tableNameToPrimaryKey = new HashMap<>();
        tableNameToPrimaryKey.put("cd", "cd_id");
        tableNameToPrimaryKey.put("dvds", "dvd_id");
        tableNameToPrimaryKey.put("revistas", "revistas_id");
        tableNameToPrimaryKey.put("libro", "libro_id");

        tableNameToJoin = new HashMap<>();
        tableNameToJoin.put("cd", "cd");
        tableNameToJoin.put("dvds", "dvd");
        tableNameToJoin.put("revistas", "rev");
        tableNameToJoin.put("libro", "lib");
    }
    public String getDispColumn(String tableName) {
        return tableNameToDisp.get(tableName);
    }
    public String getPrimaryKey(String tableName) {
        return tableNameToPrimaryKey.get(tableName);
    }
    public String getJoin(String tableName) {
        return tableNameToJoin.get(tableName);
    }

}
