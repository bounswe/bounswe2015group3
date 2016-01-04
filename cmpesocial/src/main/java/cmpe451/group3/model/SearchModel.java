package cmpe451.group3.model;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Scope("request")
public class SearchModel {
	private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public List<Map<String, Object>> getEvents(String query) {
    	query = "%" +query.toLowerCase()+ "%";
        String sql = "SELECT * FROM event WHERE (LOWER(name) LIKE ? OR LOWER(location) LIKE ? OR LOWER(description) LIKE ?)";

        return this.jdbcTemplate.queryForList(sql, query, query, query);
    }

    public List<Map<String, Object>> getGroups(String query) {
        query = "%" +query.toLowerCase()+ "%";
        String sql = "SELECT * FROM `group` WHERE (LOWER(name) LIKE ? OR LOWER(description) LIKE ?)";

        return this.jdbcTemplate.queryForList(sql, query, query, query);
    }
    
    public List<Map<String, Object>> getUsers(String query) {
    	String sql = "SELECT * FROM user WHERE (name = ? OR surname = ?)";
        String sqlNameSurname = "SELECT * FROM user WHERE (name = ? AND surname = ?)";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(sql, query, query);
        List<Map<String, Object>> result2;
        String[] names = query.split(" ");
        //exact search for name and surname
        if(names.length > 1){
        	//get the  surname
        	String surname = names[names.length-1];
        	//get the first name part
        	String name = query.replace(" " + surname, "");
        	result2 = this.jdbcTemplate.queryForList(sqlNameSurname, name, surname);
        	if(result != null)
        		result.addAll(result2);
        	else
        		result = result2;
        }
        return result;
    }

}
