package cmpe451.group3.model;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
/**
 * <h2>Search Model</h2>
 * @author Can Kurtan
 * @author Umut Afacan
 *
 */
@Repository
@Scope("request")
public class SearchModel {
	private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;
/**
 * Setting up the data source
 * @param dataSource
 */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    /**
     * Method for getting events if their name, description or location contain the searched term
     * @param query
     * @return
     */
    public List<Map<String, Object>> getEvents(String query) {
    	query = "%" +query.toLowerCase()+ "%";
        String sql = "SELECT * FROM event WHERE (LOWER(name) LIKE ? OR LOWER(location) LIKE ? OR LOWER(description) LIKE ?)";

        return this.jdbcTemplate.queryForList(sql, query, query, query);
    }
    /**
     * Method for getting groups if their name or description contain the searched term
     * @param query
     * @return
     */

    public List<Map<String, Object>> getGroups(String query) {
        query = "%" +query.toLowerCase()+ "%";
        String sql = "SELECT * FROM `group` WHERE (LOWER(name) LIKE ? OR LOWER(description) LIKE ?)";

        return this.jdbcTemplate.queryForList(sql, query, query);
    }
    /**
     * Method for getting users if their name or surname contain the searched term
     * @param query
     * @return
     */
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
