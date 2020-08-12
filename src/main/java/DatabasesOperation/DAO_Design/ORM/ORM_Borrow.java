package DatabasesOperation.DAO_Design.ORM;

import java.util.Date;

/**
 * ORM borrow表的映射
 */
public class ORM_Borrow {
    private int reader_id;
    private int book_id;
    private String wether_return;
    private Date borrow_date;
    private Date return_date;

    public ORM_Borrow() {
    }

    public int getReader_id() {
        return reader_id;
    }

    public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getWether_return() {
        return wether_return;
    }

    public void setWether_return(String wether_return) {
        this.wether_return = wether_return;
    }

    public Date getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(Date borrow_date) {
        this.borrow_date = borrow_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    @Override
    public String toString() {
        return "borrow{" +
                "reader_id=" + reader_id +
                ", book_id=" + book_id +
                ", wether_return='" + wether_return + '\'' +
                ", borrow_date=" + borrow_date +
                ", return_date=" + return_date +
                '}';
    }
}
