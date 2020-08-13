package DatabasesOperation.DAO_Design.DAOImpl;

import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import DatabasesOperation.JDBCUtils.JDBCUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对借还表的ORM borrow实现CRUD
 * <p>
 * 我在borrow表添加了两个外键约束：关于books的id和reader的id
 * reader_id--reader的id
 * book_id--book的id
 * <p>
 * 功能：借书，则加一条记录
 * 还书，则减一条记录
 */

public class DAOBorrow {
    private NamedParameterJdbcTemplate nJDBC;

    public DAOBorrow() {
        nJDBC = new NamedParameterJdbcTemplate(JDBCUtils.getDatasource());
    }

    public void borrowBook(ORM_Reader reader, int book_id) {//当存量为0借不了，就插入不了，抛出异常；是运行时异常，采取了打印处理
        String sql = "insert into borrow(reader_id,book_id,borrow_date,wether_return,return_date) values(:reader_id,:book_id,now(),'no',null)";
        Map<String, Integer> params = new HashMap<>();
        params.put("reader_id", reader.getId());
        params.put("book_id", book_id);

        nJDBC.update(sql, params);//插入，这里可能会触发两个触发器，第一个书的余量为0报错，书成功插入，则该书的余量-1；


    }

    public void returnBook(ORM_Reader reader, int books_id) {//要还书必须得借了该书。则先执行查询操作
        Integer[] res = findMyBorrow(reader);
        if (res != null)
            for (int i : res) {
                if (i == books_id) {//该读者借了该书
                    String sql = "delete from borrow where reader_id=:id1 && book_id=:id2";
                    Map<String, Integer> params = new HashMap<>();
                    params.put("id1", reader.getId());
                    params.put("id2", books_id);
                    nJDBC.update(sql, params);

                    //还书了以后，对应的books的表上该书的剩余量加1
                    sql = "update books set surples=surples+1 where id=:id";
                    Map<String, Integer> params1 = new HashMap<>();
                    params1.put("id", books_id);
                    nJDBC.update(sql, params1);
                }
            }
        else System.out.println("您没有借这本书喔！");


    }

    public Integer[] findMyBorrow(ORM_Reader reader)//查找Reader所借的书，返回书本的id
    {
        String sql = "select book_id from borrow where reader_id=:id";
        Map<String, Integer> params = new HashMap<>();
        params.put("id", reader.getId());
        List<Map<String, Object>> list = nJDBC.queryForList(sql, params);

        if (list.size() != 0) {
            Integer[] a = new Integer[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                a[i] = Integer.parseInt(map.get("book_id").toString());
            }
            return a;
        } else {

            //System.out.println("查找不到！");
            return null;
        }
    }


}
