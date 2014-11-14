package webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: gkislin
 * Date: 05.03.14
 */
public interface SqlTransaction<T> {
    T execute(Connection conn) throws SQLException;
}
