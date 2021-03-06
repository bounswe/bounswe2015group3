package cmpe451.group3.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;



@Repository
@Scope("request")
public class EventDAO {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getEventsForUser(long id_user) {
        String sql = "SELECT * FROM event WHERE id_user = ? ";

        return this.jdbcTemplate.queryForList(sql, id_user);
    }

    public List<Map<String, Object>> getEvents() {
        String sql = "SELECT * FROM event";

        return this.jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getEvent(Long id) {
        String sql = "SELECT * FROM event WHERE id = ? ";

        return this.jdbcTemplate.queryForMap(sql, id);
    }

    public Map<String, Object> getEventForName(String name)
    {
        String sql = "SELECT * FROM event WHERE name = ?";
        return  this.jdbcTemplate.queryForMap(sql,name);
    }


    public void createEvent(String name, String date, long userid, String location, String description) {
        String sql = "INSERT INTO event(name, date, id_user, location, description) VALUES(?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, name, date, userid, location, description);
    }

    public void updateEvent(Long id, String name, String date, long userid, String location, String description) {
        String sql = "UPDATE event SET name = ?, date = ?, id_user = ?, location = ?, description = ? WHERE id = ?";

        this.jdbcTemplate.update(sql, name, date, userid, location, description, id);
    }

    public void deleteEvent(Long id) {
        String sql = "DELETE FROM event WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }


    public Integer getIdFromMail(String email) {
        String sql = "SELECT id FROM user WHERE email = ? ";

        Map<String, Object> user = this.jdbcTemplate.queryForMap(sql, email);

        Integer id = (Integer) user.get("id");

        return id;
    }

    public List<Map<String,Object>> getEventAttendants() {

        String sql = "SELECT user.* FROM user,user_event WHERE user.id = user_event.id_user AND  user_event.id_event = 1 AND  user_event.status = 1";
        System.out.print("SQL :" + sql);

        List<Map<String,Object>> result = this.jdbcTemplate.queryForList(sql);

        System.out.print("List :"  + result);
        return  result;
    }


}