package manager.pojo;

import java.sql.Date;

public class CheckItem {
    //主键
    private Integer cid;
    //代号
    private String ccode;
    //检查项名称
    private String cname;
    //参考值
    private String referVal;
    //单位
    private String unit;
    private Date createDate;
    private Date updDate;
    private Date deleteDate;
    private String optionUser;
    //状态
    private String status;


    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getReferVal() {
        return referVal;
    }

    public void setReferVal(String referVal) {
        this.referVal = referVal;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getOptionUser() {
        return optionUser;
    }

    public void setOptionUser(String optionUser) {
        this.optionUser = optionUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CheckItem() {
    }

    public CheckItem(String text) {
        this.ccode = text;
    }

    public CheckItem(Integer id, String text, String text1, String text2, String text3, String uname) {
        this.cid = id;
        this.cname = text;
        this.ccode = text1;
        this.referVal = text2;
        this.unit = text3;
        this.optionUser = uname;

    }

    public CheckItem(Integer cid, String ccode, String cname, String referVal, String unit, Date createDate, Date updDate, Date deleteDate, String optionUser, String status) {
        this.cid = cid;
        this.ccode = ccode;
        this.cname = cname;
        this.referVal = referVal;
        this.unit = unit;
        this.createDate = createDate;
        this.updDate = updDate;
        this.deleteDate = deleteDate;
        this.optionUser = optionUser;
        this.status = status;
    }

    @Override
    public String toString() {
        return "CheckItem{" +
                "cid=" + cid +
                ", ccode='" + ccode + '\'' +
                ", cname='" + cname + '\'' +
                ", referVal='" + referVal + '\'' +
                ", unit='" + unit + '\'' +
                ", createDate=" + createDate +
                ", updDate=" + updDate +
                ", deleteDate=" + deleteDate +
                ", optionUser='" + optionUser + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}