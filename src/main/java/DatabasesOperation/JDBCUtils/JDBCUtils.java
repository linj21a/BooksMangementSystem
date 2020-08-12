package DatabasesOperation.JDBCUtils;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 创建一个工具类，用于创建jdbc加载驱动，释放资源等，优化Test_Driver代码
 */

public final class JDBCUtils {
    private static DataSource datasource;

    public static Connection getConnect() throws SQLException {//异常应该抛出
        return datasource.getConnection();
    }

    static {
        try {
            InputStream inputs = JDBCUtils.class.getClassLoader().getResourceAsStream("DBCP_Config.properties");
            Properties properties = new Properties();
            assert inputs != null;
            properties.load(inputs);
            datasource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void free(Connection conn, Statement st, ResultSet res) {
        try {
            if (res != null) //原则1：晚点连接早点释放。原则2：先创建的后释放
                res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }


    }

    public static DataSource getDatasource() {
        return datasource;
    }
}
