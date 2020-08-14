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
    private int hasBorrow=0;//已借书数，关联与borrow_id,一旦往borrow_id里面插入一条记录，对应的hasBorrow+1;
    private String life_motto="读万卷书，行万里路！";//人生格言


    public int getHasBorrow() {
        return hasBorrow;
    }

    public void setHasBorrow(int hasBorrow) {
        this.hasBorrow = hasBorrow;
    }

    public String getLife_motto() {
        return life_motto;
    }

    public void setLife_motto(String life_motto) {
        this.life_motto = life_motto;
    }

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
                "lifemotto:'"+life_motto+'\''+
                '}';
    }
}
