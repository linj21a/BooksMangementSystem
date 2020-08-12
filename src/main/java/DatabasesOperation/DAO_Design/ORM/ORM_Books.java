package DatabasesOperation.DAO_Design.ORM;

import java.util.Date;

/**
 * ORM映射books表
 */
public class ORM_Books {
    private int id;//书的编号
    private String name;//书名
    private float price;//价格
    private int surples;//剩余量
    private String publisher;//出版商
    private String author;//作者
    private Date publish;//出版日期
    private String type;//类型

    public ORM_Books() {
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSurples() {
        return surples;
    }

    public void setSurples(int surples) {
        this.surples = surples;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublish() {
        return publish;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Books[id:" + getId() + " name:" + getName() +
                " author:" + getAuthor() + " surples:" + getSurples()
                + "\npublisher:" + getPublisher() + " publish:" + getPublish() + " price:" +
                getPrice() + "type" + getType() + "]";
    }

    /**
     * 只要传入的string数组的属性与books的属性一一对应
     *
     * @param res 传入的属性数组
     */
    public void setBooks(String[] res) {
        String s;
        int i = 0;
        while (i < res.length) {
            s = res[i];
            switch (i) {
                case 0:
                    int id = Integer.parseInt(s);
                    this.setId(id);
                    break;
                case 1:
                    this.setName(s);
                    break;
                case 2:
                    float price = Float.parseFloat(s);
                    this.setPrice(price);
                    break;
                case 3:
                    int surples = Integer.parseInt(s);
                    this.setSurples(surples);
                    break;
                case 4:
                    this.setPublisher(s);
                    break;
                case 5:
                    this.setAuthor(s);
                    break;
                case 6:
                    java.util.Date publish = java.sql.Date.valueOf(s);
                    this.setPublish(publish);
                    break;
                case 7:
                    this.setType(s);
                    break;
                default:
                    break;

            }
            i++;
        }
    }
}
