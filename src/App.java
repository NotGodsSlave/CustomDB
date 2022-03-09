import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import org.json.*;

public class App {
    static String DBspath = "/home/ngslave/Random/uni/IT/DB/DBs";

    public static String makeFilePath(String filename) {
        return DBspath + '/' + filename + ".json";
    }

    public static void createDB(String name) {
        try {
            Database DB = new Database(name);
            File newDB = new File(makeFilePath(DB.name));
            newDB.createNewFile();
            saveDB(DB);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDB(String name) {
        try {
            File toDelete = new File(makeFilePath(name));
            if (toDelete.delete())
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String[] getDBList() {
        try {
            File DBDirectory = new File(DBspath);
            String[] temp = DBDirectory.list();
            if (temp != null) {
                for (int i = 0; i < temp.length; ++i) {
                    temp[i] = temp[i].replace(".json","");
                }
            }
            return temp;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Database exportDB(String name)
    {
        Database DB = new Database(name);
        try {
            File DBFile = new File(makeFilePath(name));
            if (DBFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(DBFile));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }
                String jsontxt = sb.toString();
                if (!jsontxt.equals("")) {
                    try {
                        JSONObject DBjson = new JSONObject(jsontxt);
                        DB.exportFromJSON(DBjson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return DB;
    }

    public static void saveDB(Database DB) {
        try {
            JSONObject output = DB.convertToJSON();
            FileWriter writer = new FileWriter(makeFilePath(DB.name));
            writer.write(output.toString());
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        DBForm gui = new DBForm();
        gui.createForm();
    }
}
