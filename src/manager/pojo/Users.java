package manager.pojo;

import java.sql.Date;

public class Users {

    //id
    private Integer userId;
    //账号
    private String username;
    //密码
    private String password;
    //姓名
    private String uname;
    private String tel;
    //性别
    private String sex;
    //出生日期  sql.Date
    private Date bir;
    //身份证
    private String idcard;
    //家庭住址
    private String address;
    //科室
    private String dep;
    //职位
    private String lev;
    //头像
    private String avatar;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBir() {
        return bir;
    }

    public void setBir(Date bir) {
        this.bir = bir;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getLev() {
        return lev;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Users() {
    }

    public Users(Integer userId, String username, String password, String uname, String tel) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.uname = uname;
        this.tel = tel;
    }

    public Users(Integer userId, String username, String password, String uname, String tel, String sex, Date bir, String idcard, String address, String dep, String lev, String avatar) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.uname = uname;
        this.tel = tel;
        this.sex = sex;
        this.bir = bir;
        this.idcard = idcard;
        this.address = address;
        this.dep = dep;
        this.lev = lev;
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", uname='" + uname + '\'' +
                ", tel='" + tel + '\'' +
                ", sex='" + sex + '\'' +
                ", bir=" + bir +
                ", idcard='" + idcard + '\'' +
                ", address='" + address + '\'' +
                ", dep='" + dep + '\'' +
                ", lev='" + lev + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
