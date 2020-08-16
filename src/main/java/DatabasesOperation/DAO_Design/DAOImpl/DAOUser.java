package DatabasesOperation.DAO_Design.DAOImpl;

import DatabasesOperation.DAO_Design.ORM.ORM_User;
import DatabasesOperation.JDBCUtils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对User表的ORM对象进行CRUD操作
 * <p>
 * //这里只有管理员才能修改
 * 要么管理员可以修改，要么就是Reader修改密码和用户名
 */

public class DAOUser {
    private NamedParameterJdbcTemplate nJDBC;


    public DAOUser() {
        nJDBC = new NamedParameterJdbcTemplate(JDBCUtils.getDatasource());
    }

    public boolean addUser(ORM_User user) {//注册的时候调用,无法设置为管理员！user的isAdmin默认为0
        String sql = "insert into user(id,name,password,isAdmin) values(:id,:name,:password,:isAdmin)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("isAdmin", user.getIsAdmin());
        int i = nJDBC.update(sql, params);
        return i != 0;//插入成功

    }

    public int deleteUser(ORM_User user) {
        String sql = "delete from user where id=:id and name=:name";
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("name", user.getName());
        return nJDBC.update(sql, params);

    }

    public ORM_User findUser(String name) {
        String sql = "select * from user where name=:name";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        List<ORM_User> list = nJDBC.query(sql, params, new BeanPropertyRowMapper<>(ORM_User.class));
        if (list.size() != 0)
            return list.get(0);//返回一个
        else return null;

    }


    public int updateUser(ORM_User user, String name, String password) {
        String sql = "update user set name=:name1,password=:password1 where id=:id and name=:name and password=:password";
        Map<String, Object> params = new HashMap<>();
        params.put("name1", name);
        params.put("password1", password);
        params.put("id", user.getId());
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        return nJDBC.update(sql, params);

    }
    /**
     * 返回最大的id
     */
    public static int getMaxId() {
        String sql = "select max(id) from user";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int id=0;
        try {

            conn = JDBCUtils.getConnect();//从连接池里面拿连接，不浪费资源
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
               id = rs.getInt("max(id)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(conn, st, rs);
        }
        return id;

    }

    /**
     * 根据用户名和密码，查询是否存在，存在则登陆成功,同时判断是否为管理员
     * @param name 用户名
     * @param password 密码
     * @return  int[2],返回值，id和isAdmin
     */

    public static int[] register(String name,String password){
        String sql = "select id,isAdmin from user where name=?and password=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id=0;
        int isAdmin=0;//管理员
        int[] useArry = new int[2];
        try {

            conn = JDBCUtils.getConnect();//从连接池里面拿连接，不浪费资源
            ps = conn.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,password);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
                isAdmin = rs.getInt("isAdmin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(conn, ps, rs);
        }
        useArry[0]=id;
        useArry[1]=isAdmin;
        return useArry;

    }
    public static List<ORM_User> readUser(){
        String sql = "select * from user where isAdmin!=1";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<ORM_User> list=new ArrayList<>();
        try {
            conn = JDBCUtils.getConnect();//从连接池里面拿连接，不浪费资源
            st = conn.createStatement();

            rs = st.executeQuery(sql);
            ORM_User user;
            while(rs.next()) {
                user = new ORM_User();
                user.setId( rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.free(conn, st, rs);
        }
        return list;
    }


}
