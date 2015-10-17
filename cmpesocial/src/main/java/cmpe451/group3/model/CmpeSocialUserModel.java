package cmpe451.group3.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
@Scope("request")
public class CmpeSocialUserModel {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getUsers() {
        String sql = "SELECT * FROM user";

        return this.jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getUser(Long id) {
        String sql = "SELECT * FROM user WHERE id = ? ";

        return this.jdbcTemplate.queryForMap(sql, id);
    }

    public void addUser(String email, String password) {
        String sql = "INSERT INTO user(email, password) VALUES(?, ?)";

        this.jdbcTemplate.update(sql, email, password);
    }

    public void updateUser(Long id, String email, String password) {
        String sql = "UPDATE user SET email = ?, password = ? WHERE id = ?";

        this.jdbcTemplate.update(sql, email, id);
    }

    public void deleteUser(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }
}