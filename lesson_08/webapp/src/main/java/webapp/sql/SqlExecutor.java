package webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: gkislin
 * Date: 05.03.14
 */
public interface SqlExecutor<T> {
    T execute(PreparedStatement st) throws SQLException;
}
