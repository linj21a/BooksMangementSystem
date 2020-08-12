package DatabasesOperation.DAO_Design.ORM;

/**
 * 建立对象关系映射
 * 映射User
 */

public class ORM_User {
    private int id;//使用者id
    private String name;//姓名
    private String password;//密码
    private int isAdmin=0;//是否为管理员0表示不是，非0则表示是，其没有set方法

    public ORM_User() {
        //使用RowMapper接口需要无参构造器反射属性。
    }

    public ORM_User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getIsAdmin() {
        return isAdmin;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
