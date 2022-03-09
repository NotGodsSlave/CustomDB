import java.util.ArrayList;
import org.json.*;

public class Row {
    public ArrayList<Cell> cells;
    public Row(ArrayList<Column> columns) {
        cells = new ArrayList<Cell>();
        for (Column col : columns) {
            cells.add(new Cell(col.type));
        }
    }
    public boolean addCell(String type) {
        cells.add(new Cell(type));
        return true;
    }
    public boolean removeCell(int i) {
        if (i >= 0 && i < cells.size()) {
            cells.remove(i);
            return true;
        }
        return false;
    }
    public boolean updateCellContent(int i, Object content) {
        Cell newcell = cells.get(i);
        if (!newcell.updateContent(content)) return false;
        cells.set(i, newcell);
        return true;
    }
    public JSONObject convertToJSON() {
        JSONObject json = new JSONObject();
        JSONArray cells_arr = new JSONArray();
        for (Cell cell : cells) {
            JSONObject temp_cell = new JSONObject();
            temp_cell.put("type", cell.cell_type);
            temp_cell.put("content", cell.getContent());
            cells_arr.put(temp_cell);
        }
        json.put("cells", cells_arr);
        return json;
    }
    public void exportFromJSON(JSONObject json) {
        this.cells = new ArrayList<Cell>();
        JSONArray cells_arr = json.getJSONArray("cells");
        for (int i = 0; i < cells_arr.length(); ++i) {
            JSONObject json_cell = cells_arr.getJSONObject(i);
            Cell newcell = new Cell(json_cell.optString("type"));
            newcell.updateContent(json_cell.opt("content"));
            this.cells.add(newcell);
        }
    }
}