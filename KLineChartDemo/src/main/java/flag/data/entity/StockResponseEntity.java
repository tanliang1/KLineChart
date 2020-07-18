package flag.data.entity;

public class StockResponseEntity {
    private String code;//标的代码
    private String display_name; //中文名称
    private String name;//缩写简称
    private String start_date ;//上市日期
    private String end_date; //退市日期;
    private String type;//类型;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SecuritiesResponseEntity{" +
                "code='" + code + '\'' +
                ", display_name='" + display_name + '\'' +
                ", name='" + name + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
