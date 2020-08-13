package DatabasesOperation.DAO_Design.DAOImpl;

import DatabasesOperation.DAOImplSpring.DAOImplSpringTemplate;
import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import DatabasesOperation.JDBCUtils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 实现CRUD_BOOKS
 */
public class DAOBooks {
    private NamedParameterJdbcTemplate nJDBC;
    private static String[] book = new String[]{//book的映射，不好维护，应该使用文件读取更新操作。
            "id",
            "name",
            "price",
            "surples",
            "publisher",
            "author",
            "publish",
            "type"
    };

    public DAOBooks() {
        nJDBC = DAOImplSpringTemplate.getJDBC();
    }

    public void addBooks(ORM_Books book) {//添加书
        if (!isExistId(book.getId())) {//id不存在才允许插入
            String sql =
                    "insert into books(id,name,price,surples,publisher,author,publish,type) Values(:id,:name,:price,:surples,:publisher,:author,:publish,:type)";
            SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(book);
            nJDBC.update(sql, parameterSource);
        } else System.out.println("id=" + book.getId() + "的书已经存在！插入无法完成！");//可以利用弹窗
    }

    /**
     * 最终的方法：将文件里面的books信息插入到表中。
     *
     * @param file 含有books内容的文件
     */
    public void addBooksByFile(File file) {
        //文件一行代表一本书，属性之间使用#分割,且用对应的字段名问号值
        //如price:400#name:大英###surples:3。。
        //通过文件，将文件里面的关于书的信息导入。属于批量导入
        BufferedReader buff = null;
        try {
            buff = new BufferedReader(new FileReader(file));
            String s;
            List<ORM_Books> books = new ArrayList<>();
            while ((s = buff.readLine()) != null) {
                String[] res = s.split("#+");
                ORM_Books books1 = setBooks(res);
                if (!isExistId(books1.getId()))//不存在才添加
                {
                    books.add(setBooks(res));
                }
                for (String s1 : res)
                    System.out.print(s1 + "+++");
                System.out.println();
            }
            SqlParameterSource[] params = new BeanPropertySqlParameterSource[books.size()];
            for (int i = 0; i < books.size(); i++) {
                params[i] = new BeanPropertySqlParameterSource(books.get(i));
            }
            addBatchBooks(params);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buff != null)
                try {
                    buff.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 批量操作，将传入的参数源多个，批量插入到表中。
     *
     * @param batchArgs 要插入的参数，book数组
     */
    private void addBatchBooks(SqlParameterSource[] batchArgs) {
        String sql =
                "insert into books(id,name,price,surples,publisher,author,publish,type) Values(:id,:name,:price,:surples,:publisher,:author,:publish,:type)";
        nJDBC.batchUpdate(sql, batchArgs);
    }

    /**
     * 根据读出来的字符集构造book类返回
     *
     * @param res 文件里面读出来的字符集，一次代表一个book
     * @return book
     */
    private ORM_Books setBooks(String[] res) {
        ORM_Books books;
        if (res.length <= book.length)//必须与books里的字段长度一致
        {
            books = new ORM_Books();
            String s;
            int i = 0;
            while (i < res.length && res[i].contains(book[i])) {
                while (res[i].equals(""))//去除空行
                    i = i + 1;
                s = res[i].substring(book[i].length() + 1);//将值取出来
                s = s.replace(" ", "");//去除空格
                switch (i) {
                    case 0:
                        int id = Integer.parseInt(s);
                        books.setId(id);
                        break;
                    case 1:
                        books.setName(s);
                        break;
                    case 2:
                        float price = Float.parseFloat(s);
                        books.setPrice(price);
                        break;
                    case 3:
                        int surples = Integer.parseInt(s);
                        books.setSurples(surples);
                        break;
                    case 4:
                        books.setPublisher(s);
                        break;
                    case 5:
                        books.setAuthor(s);
                        break;
                    case 6:
                        java.util.Date publish = java.sql.Date.valueOf(s);
                        books.setPublish(publish);
                        break;
                    case 7:
                        books.setType(s);
                    default:
                        break;

                }
                i++;
            }
            return books;


        } else throw new RuntimeException("文件的格式不对！");


    }

    public void updateBooks(ORM_Books book) {//根据id找到该书，将其更新
        String sql =
                "update books set name=:name,price=:price,surples=:surples,publisher=:publisher,author=:author,publish=:publish,type=:type where id=:id";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(book);
        nJDBC.update(sql, parameterSource);
    }

    public List<Map<String, Object>> findBooks(String keyword) {//根据关键字查看对应的书
        String sql = "select * from books where name Like :keyword";
        Map<String, String> params = new HashMap<>();
        params.put("keyword", "%" + keyword + "%");
        return nJDBC.queryForList(sql, params);
    }

    public static List<ORM_Books> displayBooks() {//展示书本,返回list
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<ORM_Books> list = new ArrayList<>();
        try {
            String sql = "select * from books";
            conn = JDBCUtils.getConnect();//从连接池里面拿连接，不浪费资源
            st = conn.createStatement();

            rs = st.executeQuery(sql);
            ORM_Books books;
            while (rs.next()) {
                books = new ORM_Books();
                books.setId(rs.getInt("id"));
                books.setName(rs.getString("name"));
                books.setSurples(rs.getInt("surples"));
                books.setPrice(rs.getFloat("price"));
                books.setAuthor(rs.getString("author"));
                books.setPublisher(rs.getString("publisher"));
                books.setPublish(rs.getDate("publish"));
                books.setType(rs.getString("type"));
                list.add(books);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(conn, st, rs);
        }
        return list;
    }

    public ORM_Books findBooksByName(String book_name) {//根据书的名字查看对应的书
        String sql = "select * from books where name=:name";
        Map<String, String> params = new HashMap<>();
        params.put("name", book_name);
        /*Incorrect column count: expected 1, actual 8
        同时不仅是jdbcTemplate.queryForList不能这么使用，
        queryForObject同样也不能这么使用，而是应该添加new RowMapper接口才能返回自定义的实体类对象。
         */
        List<ORM_Books> books = nJDBC.query(sql, params, new BeanPropertyRowMapper<>(ORM_Books.class));
        if (books.size() == 0)//不能判断null，因为及时返回的元素个数为0，这个list也不是null
            throw new RuntimeException("不存在这样的书名！");
        return books.remove(0);//返回第一个

    }

    private boolean isExistId(int id) {//判断id是否存在
        String sql = "select id from books where id=:id";
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        List<ORM_Books> list = nJDBC.query(sql, params, new BeanPropertyRowMapper<>(ORM_Books.class));
        return list.size() != 0;
    }

    public void deleteBooks(int id) {//根据书的id来删除书本
        String sql = "delete from books where id=:id";
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        nJDBC.update(sql, params);

    }
}
