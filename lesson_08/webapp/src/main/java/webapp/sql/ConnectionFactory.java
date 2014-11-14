package webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: gkislin
 * Date: 06.11.13
 */
public interface ConnectionFactory {
    Connection getConnection() throws SQLException;
}
