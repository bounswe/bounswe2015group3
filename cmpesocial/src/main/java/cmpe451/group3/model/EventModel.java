package cmpe451.group3.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Repository
@Scope("request")
public class EventModel {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getEventsForUser(long id) {
        String sql = "SELECT * FROM event WHERE user_id = ? ";

        return this.jdbcTemplate.queryForList(sql, id);
    }
    
    public List<Map<String, Object>> getEvents() {
        String sql = "SELECT * FROM event";

        return this.jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getEvent(Long id) {
        String sql = "SELECT * FROM event WHERE id = ? ";

        return this.jdbcTemplate.queryForMap(sql, id);
    }
    
    public void createEvent(String name, String date, long userid, String location, String description) {
        String sql = "INSERT INTO event(name, date, id_user, location, description) VALUES(?, ?, ?, ?, ?)";
        
        this.jdbcTemplate.update(sql, name, date, userid, location, description);
    }

    public void updateEvent(Long id, String name, String date, long userid, String location, String description) {
        String sql = "UPDATE event SET name = ?, date = ?, userid = ?, location = ?, description = ? WHERE id = ?";

        this.jdbcTemplate.update(sql, name, date, userid, location, description, id);
    }

    public void deleteEvent(Long id) {
        String sql = "DELETE FROM event WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }
}