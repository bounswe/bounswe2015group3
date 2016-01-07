package cmpe451.group3.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
/**
* <h2>Event Model</h2>
* @author Can Kurtan
* @author Umut Afacan
* @author Cem Ozen
* @author Tuba Topaloğlu
*/
@Repository
@Scope("request")
public class EventModel {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;
/**
 * Setting up the data source
 */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
/**
 * Script for getting events for specific user 
 */
    public List<Map<String, Object>> getEventsForUser(long id_user) {
        String sql = "SELECT * FROM event WHERE id_user = ? ORDER  BY event.date ASC";

        return this.jdbcTemplate.queryForList(sql, id_user);
    }
/**
 * Script for getting events which the user attending
 */
    public List<Map<String,Object>> getEventsJoined(long id_user){
        String sql = "SELECT `event`.* FROM `event`, user_event WHERE event.id = user_event.id_event AND user_event.id_user = ? AND user_event.status = 1 ORDER BY event.date ASC";
        return this.jdbcTemplate.queryForList(sql,id_user);
    }

/**
 * Script for getting all events
 */
    public List<Map<String, Object>> getEvents() {
        String sql = "SELECT * FROM event ORDER BY event.date ASC";

        return this.jdbcTemplate.queryForList(sql);
    }
    /**
     * Script for get specific event
     */
    public Map<String, Object> getEvent(Long id) {
        String sql = "SELECT * FROM event WHERE id = ? ";

        return this.jdbcTemplate.queryForMap(sql, id);
    }
    /**
     * Script for get specific event according to its name
     */
    public Map<String, Object> getEventForName(String name)
    {
        String sql = "SELECT * FROM event WHERE name = ?";
        return  this.jdbcTemplate.queryForMap(sql,name);
    }
    /**
     * Script for create event
     * @param name, date, end_date, periodic option, id_user(Owner of the event), lcoation, description, type, id_group(Group of the event), url(for cover photo) 
     */

    public void createEvent(String name, String date,String end_date,int periodic, long userid, String location, String description,String type, Long id_group, String url) {
        String sql = "INSERT INTO event(name, date,end_date,periodic, id_user, location, description,type,id_group,url) VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?)";
        //bug is here
        this.jdbcTemplate.update(sql, name, date,end_date,periodic, userid, location, description,type,id_group,url);
    }
    /**
     * Script for update event
     * @param name, date, end_date, periodic option, id_user(Owner of the event), lcoation, description, type, id_group(Group of the event), url(for cover photo) 
     */
    public void updateEvent(Long id, String name, String date,String end_date,int periodic, long userid, String location, String description, String type, Long id_group, String url) {
        String sql = "UPDATE event SET name = ?, date = ?, end_date = ?, periodic = ?, id_user = ?, location = ?, description = ?, type = ?, id_group= ?, url = ? WHERE id = ?";
        //bug is here
        this.jdbcTemplate.update(sql, name, date, end_date, periodic, userid, location, description, type, id_group, url, id);
    }
/**
 * Script for get events for a group
 */
    public List<Map<String,Object>> getEventsOfGroup(long id_group)
    {
        String sql = "SELECT event.* FROM event WHERE event.id_group = ?";
       return this.jdbcTemplate.queryForList(sql,id_group);
    }
    /**
     * Script for delete an event
     */
    public void deleteEvent(Long id) {
        String sql = "DELETE FROM event WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }
    /**
     * Script for join an event
     */
    
    public void joinEvent(Long userid, Long eventid) {
        String sql = "INSERT INTO user_event(id_user,id_event,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 1";
        this.jdbcTemplate.update(sql, userid, eventid, 1);

        addTagFromEventToUser(userid, eventid);

    }
    /**
     * Script for leave an event
     */
    public void leaveEvent(Long id_user, Long id_event){
        String sql = "INSERT INTO user_event(id_user,id_event,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 0";
        this.jdbcTemplate.update(sql, id_user, id_event, 0);

    }
    /**
     * Script for invite an user to an event
     */
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
    /**
     * Script for get participants of an event
     */
    public List<Map<String, Object>> getParticipants(Long id) {
        String sql = "SELECT user.* from user, user_event WHERE user.id = user_event.id_user AND user_event.id_event = ? AND user_event.status = 1";
        
        List<Map<String, Object>> participants = this.jdbcTemplate.queryForList(sql, id);
        
        return participants;
    }
    /**
     * Script for get events for invited user
     */
    public List<Map<String, Object>> getEventsInvited(Long id) {
        String sql = "SELECT event.* from event, user_event WHERE event.id = user_event.id_event AND user_event.id_user = ? AND user_event.status = 2";

        List<Map<String, Object>> events = this.jdbcTemplate.queryForList(sql, id);

        return events;
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
/**
 * create a post for event
 * @param id_event
 * @param id_user
 * @param content
 * @param content_url
 */
    public void createPost(Long id_event, Long id_user, String content, String content_url )
    {
        String sql = "INSERT INTO post_event(id_event, id_user, content, content_url) VALUES(?,?,?,?)";
        this.jdbcTemplate.update(sql, id_event, id_user, content, content_url);
    }
    /**
     * update post of an event
     * @param id
     * @param id_event
     * @param id_user
     * @param content
     * @param content_url
     */

    public void updatePost(Long id, Long id_event, Long id_user, String content, String content_url){
        String sql= "UPDATE post_event SET id_event = ? , id_user = ?, content= ?, content_url = ? WHERE id= ?";
        this.jdbcTemplate.update(sql,id_event,id_user,content,content_url,id);
    }
/**
 * delete a post of en event
 * @param id
 */
    public void deletePost(Long id)
    {
        String sql = "DELETE FROM post_event WHERE id = ?";
        this.jdbcTemplate.update(sql,id);
    }
    /**
     * Script for get all posts belong to an event
     * @param id_event
     * @return
     */
    public List<Map<String,Object>> getAllPosts(Long id_event)
    {
        String sql = "SELECT * FROM post_event WHERE id_event = ?";

       return this.jdbcTemplate.queryForList(sql,id_event);
    }
/**
 * Script for get a specific post of an event
 * @param id
 * @return
 */
    public Map<String,Object> getPost(Long id)
    {
        String sql = "SELECT * FROM post_event WHERE id= ?";
        return  this.jdbcTemplate.queryForMap(sql, id);
    }
    public void createComment(Long id_post,Long id_event, Long id_user, String content)
    {
        String sql = "INSERT INTO comment_event(id_post,id_event,id_user,content) VALUES(?,?,?,?)";
        this.jdbcTemplate.update(sql,id_post,id_event,id_user,content);
    }

    public void updateComment(Long id,Long id_post, Long id_event, Long id_user, String content){
        String sql= "UPDATE  comment_event SET id_post=?,id_event = ? , id_user = ?, content= ? WHERE id= ?";
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

/**
 * Method for checking an user role is acceptable for the event
 * @param id_event
 * @param id_user
 * @return
 */
    public Boolean isAvailableForEvent(Long id_event, Long id_user)
    {
        String sql2 = "SELECT * FROM `user` WHERE id = ? " ;

        String sql =  "SELECT * FROM `event` WHERE id = ? " ;

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
/**
 * Method for checking a user is attending the event or not 
 * @param id_user
 * @param id_event
 * @return
 */
    public Boolean isGoingToEvent(Long id_user,Long id_event)
    {
        String sql = "SELECT user_event.* FROM user_event WHERE user_event.id_user = ? AND user_event.id_event = ? ";

        List<Map<String,Object>> membership = this.jdbcTemplate.queryForList(sql,id_user, id_event);


        if (!membership.isEmpty() && (int)membership.get(0).get("status") == 1){
        	return   Boolean.TRUE;
        }
        else	{
        	return   Boolean.FALSE;
        }
    }


/**
 * Method for adding tag from users
 * @param id_user
 * @param id_event
 */
    public void addTagFromEventToUser(long id_user,long id_event)
    {
        String sql = "SELECT tag_event.tag FROM tag_event WHERE tag_event.id_event = ?";
        List<Map<String,Object>> tag_list = this.jdbcTemplate.queryForList(sql, id_event);

        for (Map<String,Object> tag_map :tag_list)
        {
            String tag = tag_map.get("tag").toString();
            String sqlUser = "INSERT INTO tag_user(id_user,tag,hidden) VALUES(?,?,TRUE)";
            this.jdbcTemplate.update(sqlUser,id_user,tag);
        }

    }

}