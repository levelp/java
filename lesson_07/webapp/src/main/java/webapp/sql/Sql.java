package webapp.sql;

import webapp.WebAppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: gkislin
 * Date: 14.07.2014
 */
public class Sql {
    public static ConnectionFactory CONN_FACTORY =
            new DirectConnection();

    public static <T> T execute(String sql, SqlExecutor<T> executor) {
        try (Connection conn = Sql.CONN_FACTORY.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw new WebAppException("SQL failed", e);
        }
    }

    public static <T> T execute(SqlTransaction<T> executor) {
        try (Connection conn = CONN_FACTORY.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new WebAppException("Transaction failed", e);
        }
    }

}
