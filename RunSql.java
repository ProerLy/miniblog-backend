import java.sql.*;
import java.nio.file.*;

public class RunSql {
    public static void main(String[] args) throws Exception {
        String sql = new String(Files.readAllBytes(Paths.get("D:\\project\\博客项目app\\miniblog-backend\\sql\\init.sql")), "utf-8");
        String url = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        try (Connection conn = DriverManager.getConnection(url, "root", "root")) {
            for (String stmt : sql.split(";")) {
                stmt = stmt.trim();
                if (!stmt.isEmpty()) {
                    try {
                        conn.createStatement().execute(stmt);
                        System.out.println("OK: " + stmt.substring(0, Math.min(60, stmt.length())));
                    } catch (SQLException e) {
                        System.out.println("ERR[" + e.getErrorCode() + "]: " + e.getMessage());
                    }
                }
            }
            System.out.println("DONE");
        }
    }
}
