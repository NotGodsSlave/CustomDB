import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TableTest {
    private Table table;

    @Before
    public void setUp() throws Exception {
        table = new Table("FineTable");
        table.addColumn("FirstColumn", "String");
        table.addColumn("GoodColumn","Money");
        table.addColumn("BadColumn","Real");
        table.addColumn("Year","Integer");
        Object[][] content = {
                {"Some literal", "15$", 8.5, 1500},
                {"Another literal", "5$", 4.25, 2020},
                {"Nonsense", "500$", 120, 0}
        };
        for (int i = 0; i < 3; ++i) {
            table.addRow();
            for (int j = 0; j < table.columns.size(); ++j){
                table.updateCellContent(i,j,content[i][j]);
            }
        }
    }

    @Test
    public void addRow() {
        table.addRow();
        Assert.assertEquals(4,table.rows.size());
    }

    @Test
    public void removeColumn() {
        table.removeColumn(2);
        ArrayList<Column> expected_columns = new ArrayList<>();
        Assert.assertEquals(3,table.columns.size());
        Assert.assertNotEquals("BadColumn",table.columns.get(2).name);
    }

    @Test
    public void sortByValue() {
        table.sortByValue(1);
        Object[][] expectedContent = {
                {"Another literal", "5$", 4.25, 2020},
                {"Some literal", "15$", 8.5, 1500},
                {"Nonsense", "500$", 120, 0}
        };
        Object[][] actualContent = new Object[3][4];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < table.columns.size(); ++j){
                actualContent[i][j] = table.rows.get(i).cells.get(j).getContent();
            }
        }

        Assert.assertArrayEquals(expectedContent, actualContent);
    }
}