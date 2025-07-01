package manager.pojo;

import java.sql.Timestamp;

public class CheckResult {
    private Integer resultId;
    private Integer orderId;
    private Integer userId;
    private String gid;
    private Integer cid;
    private String value;
    private Timestamp resultDate;
    private String inputUser;

    public Integer getResultId() { return resultId; }
    public void setResultId(Integer resultId) { this.resultId = resultId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getGid() { return gid; }
    public void setGid(String gid) { this.gid = gid; }
    public Integer getCid() { return cid; }
    public void setCid(Integer cid) { this.cid = cid; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public Timestamp getResultDate() { return resultDate; }
    public void setResultDate(Timestamp resultDate) { this.resultDate = resultDate; }
    public String getInputUser() { return inputUser; }
    public void setInputUser(String inputUser) { this.inputUser = inputUser; }
}
