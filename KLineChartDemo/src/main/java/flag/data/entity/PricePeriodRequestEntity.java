package flag.data.entity;

public class PricePeriodRequestEntity extends BaseRequestEntity {
    private String method = "get_price_period";
    private String code = "600000.XSHG";
    private String unit = "1d";
    private String date = "2018-12-18";
    private String end_date = "2020-07-18";
    private String fq_ref_date = "2020-07-18";

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getFq_ref_date() {
        return fq_ref_date;
    }

    public void setFq_ref_date(String fq_ref_date) {
        this.fq_ref_date = fq_ref_date;
    }
}
