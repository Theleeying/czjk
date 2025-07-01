package manager.pojo;

import java.sql.Date;

public class Order {
    private Integer id;
    private Integer userId;
    private String gid;
    private String name;
    private String sex;
    private String telephone;
    private Date orderDate;
    private String type; // 0微信1电脑
    private String status; // 0未到诊1到诊

    // getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getGid() { return gid; }
    public void setGid(String gid) { this.gid = gid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
