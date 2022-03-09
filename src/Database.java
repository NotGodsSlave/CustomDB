import java.util.HashMap;
import java.io.File;
import com.google.gson.*;
import org.json.*;

public class Database {
    public String name;
    public HashMap<String, Table> tables;

    public Database(String name) {
        this.name = name;
        this.tables = new HashMap<String, Table>();
    }
    public boolean addTable(String tableName) {
        this.tables.put(tableName, new Table(tableName));
        return true;
    }
    public boolean removeTable(String tableName) {
        if (tables.containsKey(tableName)) {
            this.tables.remove(tableName);
            return true;
        }
        return false;
    }
    public JSONObject convertToJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        JSONArray tables_arr = new JSONArray();
        for (Table table : this.tables.values()) {
            tables_arr.put(table.convertToJSON());
        }
        json.put("tables", tables_arr);
        return json;
    }
    public void exportFromJSON(JSONObject json) {
        this.name = json.optString("name");
        JSONArray tables_arr = json.getJSONArray("tables");
        for (int i = 0; i < tables_arr.length(); ++i) {
            JSONObject json_table = tables_arr.getJSONObject(i);
            Table t = new Table(json_table.optString("name"));
            t.exportFromJSON(json_table);
            this.tables.put(t.name, t);
        }
    }
}