package DatabasesOperation.DAOImplSpring;

import DatabasesOperation.JDBCUtils.JDBCUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import javax.sql.DataSource;

/**
 * 使用SpringTemplate实现各个表ORM对象的CRUD；
 * 这个类主要返回一个注册了数据源的命名参数Spring-template 即：nJdbc；
 */
public class DAOImplSpringTemplate {
    private static NamedParameterJdbcTemplate nJdbc;

    public static NamedParameterJdbcTemplate getJDBC() {
        if (nJdbc == null)//多线程安全问题
            synchronized (DAOImplSpringTemplate.class) {
                if (nJdbc == null)
                    nJdbc = new NamedParameterJdbcTemplate(JDBCUtils.getDatasource());

            }
        return nJdbc;
    }

}
