package cmpe451.group3.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

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

    public List<Map<String, Object>> getEventsForUser(long id_user) {
        String sql = "SELECT * FROM event WHERE id_user = ? ORDER  BY event.date ASC";

        return this.jdbcTemplate.queryForList(sql, id_user);
    }


    public List<Map<String, Object>> getEvents() {
        String sql = "SELECT * FROM event ORDER BY event.date ASC";

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


    public void createEvent(String name, String date,String end_date,int periodic, long userid, String location, String description,String type) {
        String sql = "INSERT INTO event(name, date,end_date,periodic, id_user, location, description,type) VALUES(?, ?, ?, ?, ?, ?, ?,?)";
        
        this.jdbcTemplate.update(sql, name, date,end_date,periodic, userid, location, description,type);
    }

    public void updateEvent(Long id, String name, String date,String end_date,int periodic, long userid, String location, String description,String type) {
        String sql = "UPDATE event SET name = ?, date = ?,end_date = ?,periodic = ? , id_user = ?, location = ?, description = ? , `type` = ? WHERE id = ?";

        this.jdbcTemplate.update(sql, name, date,end_date,periodic, userid, location, description,type ,id);
    }

    public void deleteEvent(Long id) {
        String sql = "DELETE FROM event WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }
    
    public void joinEvent(Long userid, Long eventid) {
        String sql = "INSERT INTO user_event(id_user,id_event,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 1";
        this.jdbcTemplate.update(sql, userid, eventid, 1);
    }

    public void leaveEvent(Long id_user, Long id_event){
        String sql = "INSERT INTO user_event(id_user,id_event,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 0";
        this.jdbcTemplate.update(sql,id_user,id_event,0);

    }
    
    public void invite(Long userid, Long eventid) {
        String sql = "INSERT INTO user_event(id_user,id_event,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = IF((status = 1), 1, 2)";
        this.jdbcTemplate.update(sql, userid, eventid, 2);
    }


    
    public Integer getIdFromMail(String email) {
        String sql = "SELECT id FROM user WHERE email = ? ";
        
        Map<String, Object> user = this.jdbcTemplate.queryForMap(sql, email);
        
        Integer id = (Integer) user.get("id");
        
        return id;
    }
    
    public List<Map<String, Object>> getParticipants(Long id) {
        String sql = "SELECT user.* from user, user_event WHERE user.id = user_event.id_user AND user_event.id_event = ? AND user_event.status = 1";
        
        List<Map<String, Object>> participants = this.jdbcTemplate.queryForList(sql, id);
        
        return participants;
    }
    
    /**
     * Gets the all events from database that user is joined.
     * 
     * @param id user's id
     * @return list of user's events
     */
    public List<Map<String, Object>> getEventsOfUser(Long id){
    	
    	String sql = "SELECT event.* from event, user_event WHERE event.id = user_event.id_event AND user_event.id_user = ? AND user_event.status = 1";   	
    	List<Map<String, Object>> events = this.jdbcTemplate.queryForList(sql, id);
    	
    	return events;
    }

    public void createPost(Long id_event, Long id_user, String content, String content_url )
    {
        String sql = "INSERT INTO post_event(id_event,id_user,content,content_url) VALUES(?,?,?,?)";
        this.jdbcTemplate.update(sql,id_event,id_user,content,content_url);
    }

    public void updatePost(Long id, Long id_event, Long id_user, String content, String content_url){
        String sql= "UPDATE post_event SET id_event = ? , id_user = ?, content= ?, content_url = ? WHERE id= ?";
        this.jdbcTemplate.update(sql,id_event,id_user,content,content_url,id);
    }

    public void deletePost(Long id)
    {
        String sql = "DELETE FROM post_event WHERE id = ?";
        this.jdbcTemplate.update(sql,id);
    }
    public List<Map<String,Object>> getAllPosts(Long id_event)
    {
        String sql = "SELECT * FROM post_event WHERE id_event = ?";

       return this.jdbcTemplate.queryForList(sql,id_event);
    }

    public Map<String,Object> getPost(Long id)
    {
        String sql = "SELECT * FROM post_event WHERE id= ?";
        return  this.jdbcTemplate.queryForMap(sql, id);
    }
    public void createComment(Long id_post,Long id_event, Long id_user, String content)
    {
        String sql = "INSERT INTO comment_event(id_post,id_event,id_user,content) VALUES(?,?,?,?)";
        this.jdbcTemplate.update(sql,id_event,id_user,content);
    }

    public void updateComment(Long id,Long id_post, Long id_event, Long id_user, String content){
        String sql= "UPDATE  post_event SET id_post=?,id_event = ? , id_user = ?, content= ? WHERE id= ?";
        this.jdbcTemplate.update(sql,id_post,id_event,id_user,content,id);
    }

    public void deleteComment(Long id)
    {
        String sql = "DELETE FROM comment_event WHERE id = ?";
        this.jdbcTemplate.update(sql,id);
    }
    public List<Map<String,Object>> getAllComments(Long id_post)
    {
        String sql = "SELECT * FROM comment_event WHERE id_post = ?";

        return this.jdbcTemplate.queryForList(sql,id_post);
    }

    public Map<String,Object> getComment(Long id)
    {
        String sql = "SELECT * FROM comment_event WHERE id= ?";
        return  this.jdbcTemplate.queryForMap(sql, id);
    }


    public Boolean isAvailableForEvent(Long id_event, Long id_user)
    {
        String sql2 = "SELECT * FROM `user` WHERE id = ?";

        String sql = "SELECT * FROM `event` WHERE id = ? ";

        Map<String,Object> group =  this.jdbcTemplate.queryForMap(sql, id_event);
        Map<String,Object> user = this.jdbcTemplate.queryForMap(sql2,id_user);

        List<String> list_event = new ArrayList<String>(Arrays.asList(group.get("type").toString().split(",")));
        List<String> list_user = new ArrayList<String>(Arrays.asList(user.get("type").toString().split(",")));

        for (String type_group : list_event){
            for (String type_user : list_user)
            {
                if (type_group.equalsIgnoreCase(type_user))
                    return Boolean.TRUE;
            }
        }
        return  Boolean.FALSE;
    }

    public Boolean isGoingToEvent(Long id_user,Long id_event)
    {
        String sql = "SELECT user_event.* FROM user_event WHERE user_event.id_user = ? AND user_event.id_event = ? ";

        List<Map<String,Object>> membership = this.jdbcTemplate.queryForList(sql,id_user, id_event);


        if (membership.isEmpty())
            return   Boolean.FALSE;
        else
            return   Boolean.TRUE;

    }


}