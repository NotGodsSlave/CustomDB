import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import org.json.*;

public class Table{
    public String name;
    public ArrayList<Column> columns;
    public ArrayList<Row> rows;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<Column>();
        this.rows = new ArrayList<Row>();
    }
    public void addColumn(String name, String type) {
        for (Column col : columns) {
            if (col.name == name) return;
        }
        Column col = new Column(name, type);
        columns.add(col);
        for (Row r : rows) {
            r.addCell(type);
        }
    }
    public void removeColumn(int i) {
        if (i >= 0 && i < columns.size()) {
            for (Row r : rows) {
                r.removeCell(i);
            }
            columns.remove(i);
        }
    }
    public void addRow() {
        Row r = new Row(columns);
        rows.add(r);
    }
    public void removeRow(int i) {
        if (i < 0 || i >= rows.size()) return;
        rows.remove(i);
    }
    public void sortByValue(int column_index) {
        Collections.sort(rows, new Comparator<Row>() {
            @Override
            public int compare(Row r1, Row r2) {
                if (r1.cells.get(column_index).getContent() == null) return -1;
                else if (r2.cells.get(column_index).getContent() == null) return 1;
                if (r1.cells.get(column_index).getContent().equals(r2.cells.get(column_index).getContent()))
                    return 0;
                else {
                    String fieldType = columns.get(column_index).type;
                    return compareFields(r1.cells.get(column_index), r2.cells.get(column_index), fieldType);
                }
            }
        });
    }

    private int compareFields(Cell cell1, Cell cell2, String fieldType){
        switch (fieldType) {
            case "String":
                return cell1.getContent().toString().compareTo(cell2.getContent().toString());
            case "Integer":
                return Integer.parseInt(cell1.getContent().toString()) > Integer.parseInt(cell2.getContent().toString()) ? 1 : -1;
            case "Character":
                return cell1.getContent().toString().charAt(0) > cell2.getContent().toString().charAt(0) ? 1 : -1;
            case "Real":
                return Double.parseDouble(cell1.getContent().toString()) > Double.parseDouble(cell2.getContent().toString()) ? 1 : -1;
            case "Money":
                return Integer.parseInt(cell1.getContent().toString().replace("$","")) > Integer.parseInt(cell2.getContent().toString().replace("$","")) ? 1 : -1;
            default:
                return 0;
        }
    }
    public boolean updateCellContent(int row_index, int column_index, Object content) {
        Row updated_row = rows.get(row_index);
        if (!updated_row.updateCellContent(column_index, content)) return false;
        rows.set(row_index, updated_row);
        return true;
    }
    public JSONObject convertToJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        JSONArray columns_arr = new JSONArray();
        for (Column col : columns) {
            JSONObject temp_column = new JSONObject();
            temp_column.put("name", col.name);
            temp_column.put("type", col.type);
            columns_arr.put(temp_column);
        }
        json.put("columns", columns_arr);
        JSONArray rows_arr = new JSONArray();
        for (Row r : rows) {
            rows_arr.put(r.convertToJSON());
        }
        json.put("rows", rows_arr);
        return json;
    }
    public void exportFromJSON(JSONObject json) {
        this.name = json.optString("name");
        JSONArray columns_arr = json.getJSONArray("columns");
        for (int i = 0; i < columns_arr.length(); ++i) {
            JSONObject json_column = columns_arr.getJSONObject(i);
            Column col = new Column(json_column.optString("name"),json_column.optString("type"));
            this.columns.add(col);
        }
        JSONArray rows_arr = json.getJSONArray("rows");
        for (int i = 0; i < rows_arr.length(); ++i) {
            JSONObject json_row = rows_arr.getJSONObject(i);
            Row r = new Row(columns);
            r.exportFromJSON(json_row);
            this.rows.add(r);
        }
    }
}