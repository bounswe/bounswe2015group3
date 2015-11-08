package cmpe451.group3.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public  Map<String, Object> getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        return  this.jdbcTemplate.queryForMap(sql,email);

    }

    public Map<String, Object> getUserWithEmail(String email)
    {
        String sql = "SELECT * FROM user WHERE email = ?";

        return this.jdbcTemplate.queryForMap(sql,email);
    }
    
    public boolean checkEmail(String email){
    	String sql = "SELECT password FROM user WHERE email = ? ";
    	try{
    		Map<String, Object> user = this.jdbcTemplate.queryForMap(sql, email);
    		if(user.isEmpty()){
    			return false;
    		}
    	}
    	catch(Exception e){
    		return false;
    	}
    	return true;
    }
    
    public String getPassword(String email) {
        String sql = "SELECT password FROM user WHERE email = ? ";
        Map<String, Object> user = this.jdbcTemplate.queryForMap(sql, email);
        String password = (String) user.get("password");
        return password;
    }

    public void addUser(String name, String surname, String email, String password) {
        String sql = "INSERT INTO user(name, surname, email, password) VALUES(?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, name, surname, email, password);
    }

    public void updateUser(Long id, String name, String surname, String email, String password) {
        String sql = "UPDATE user SET name = ?, surname = ?, email = ?, password = ? WHERE id = ?";

        this.jdbcTemplate.update(sql, name, surname, email, password, id);
    }

    public void deleteUser(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

}