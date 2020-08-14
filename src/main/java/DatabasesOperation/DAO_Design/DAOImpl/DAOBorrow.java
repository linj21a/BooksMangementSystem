package DatabasesOperation.DAO_Design.DAOImpl;

import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import DatabasesOperation.JDBCUtils.JDBCUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.*;
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

        //在借书之前需要判断该书是否已经借过。
        String sql = "insert into borrow(reader_id,book_id,borrow_date,wether_return,return_date) values(:reader_id,:book_id,now(),'no', date_add(now(), interval 3 month))";
        Map<String, Integer> params = new HashMap<>();
        params.put("reader_id", reader.getId());
        params.put("book_id", book_id);
        //params.put("return_date",)还书期限为3个月
        if(nJDBC.update(sql, params)!=0){//
            reader.setHasBorrow(reader.getHasBorrow()+1);//已经借书加1
            new DAOReader().updateReader(reader);
        }//插入，这里可能会触发两个触发器，第一个书的余量为0报错，书成功插入，则该书的余量-1；
        //将reader的已借书数加1
    }

    /**
     * 判断该读者是否已经借过该书
     * @param reader 读者
     * @param book_id 要借书id
     * @return 返回结果
     */
    public boolean hasBorrow(ORM_Reader reader, int book_id){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result =false;
        try{
            String sql = "select reader_id from borrow where reader_id=? and book_id=?";
            conn = JDBCUtils.getConnect();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,reader.getId());
            ps.setInt(2,book_id);
             rs = ps.executeQuery();
             if(rs.next()){
                 result = true;
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.free(conn,ps,rs);
        }
        return result;
    }


    public void returnBook(ORM_Reader reader, int books_id) {//要还书必须得借了该书。则先执行查询操作
        Integer[] res = findMyBorrow(reader);
        if (res != null&&res.length>0)
            for (int i : res) {
                if (i == books_id) {//该读者借了该书
                    String sql = "delete from borrow where reader_id=:id1 && book_id=:id2";
                    Map<String, Integer> params = new HashMap<>();
                    params.put("id1", reader.getId());
                    params.put("id2", books_id);
                   if(nJDBC.update(sql, params)!=0){
                       //还书了以后，对应的books的表上该书的剩余量加1
                       sql = "update books set surples=surples+1 where id=:id";
                       Map<String, Integer> params1 = new HashMap<>();
                       params1.put("id", books_id);
                       nJDBC.update(sql, params1);
                       //同时已经借书数也要减1；
                       reader.setHasBorrow(reader.getHasBorrow()-1);
                       new DAOReader().updateReader(reader);
                   }


                }
            }
        else System.out.println("您没有借这本书喔！");
    }
    public void returnBatchBooks(ORM_Reader reader){
        //批量还书
        Integer[] res = findMyBorrow(reader);//所借的书
        if(res==null||res.length==0){
            return;
        }
        String sql = "delete from borrow where reader_id=:id";
        Map<String,Integer> param = new HashMap<>();
        param.put("id",reader.getId());
        int i = nJDBC.update(sql,param);
        //这里需要将借书记录更新
        reader.setHasBorrow(reader.getHasBorrow()-i);
        new DAOReader().updateReader(reader);
        //对应的书的id加1
        String sql2 = "update books set surples=surples+1 where id=:id1";
        Map<String,Integer>[] maps =new Map[res.length];
        for(int k=0;k<res.length;k++){
            //同时对应的书的数量加1
            maps[k] = new HashMap<>();
            (maps[k]).put("id1",res[k]);
        }
        nJDBC.batchUpdate(sql2,maps);

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
