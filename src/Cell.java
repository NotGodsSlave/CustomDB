public class Cell {
    public String cell_type;
    private Object content;
    private final static double MAX_MONEY_VALUE = 10000000000000.00;
    public Cell(String cls) {
        this.cell_type = cls;
        this.content = null;
    }
    private boolean checkMoneyType(String val) {
        if (val.charAt(val.length()-1) != '$') return false;
        String v = val.replace("$","");
        try {
            double d = Double.parseDouble(v);
            return d >= 0 && d <= MAX_MONEY_VALUE;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean checkValue(Object val)
    {
        String v = val.toString();
        switch(this.cell_type) {
            case "Integer":
                try {
                    Integer.parseInt(v);
                    return true;
                }
                catch (NumberFormatException e) {
                    return false;
                }
            case "Real":
                try {
                    Double.valueOf(v);
                    return true;
                }
                catch (NumberFormatException e) {
                    return false;
                }
            case "Character":
                return v.length() <= 1;
            case "String":
                if (val.getClass() == String.class) {
                    return true;
                }
                break;
            case "Money":
                if (val.getClass() == String.class && checkMoneyType((String) val)) {
                    return true;
                }
                break;
        }
        return false;
    }
    public boolean updateContent(Object content)
    {
        if (content == null) {
            this.content = null;
            return true;
        }
        if (checkValue(content)){
            this.content = content;
            return true;
        }
        return false;
    }
    public Object getContent() {
        return this.content;
    }
}