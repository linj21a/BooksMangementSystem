package DatabasesOperation.DAO_Design.ORM;

/**
 * ORM映射reader表
 */
public class ORM_Reader {
    private int id;//读者id
    private String name;//姓名
    private int borrow_day=90;//可借天数
    private int borrow_num=30;//可借书本数
    private String sex;//性别，男女，只能是两个值，man\woman

    public ORM_Reader() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBorrow_day() {
        return borrow_day;
    }

    public void setBorrow_day(int borrow_day) {
        this.borrow_day = borrow_day;
    }

    public int getBorrow_num() {
        return borrow_num;
    }

    public void setBorrow_num(int borrow_num) {
        this.borrow_num = borrow_num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", borrow_day=" + borrow_day +
                ", borrow_num=" + borrow_num +
                ", sex='" + sex + '\'' +
                '}';
    }
}
