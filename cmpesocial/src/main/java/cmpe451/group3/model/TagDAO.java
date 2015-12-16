package cmpe451.group3.model;

/**
 * Created by umut on 12/13/15.
 */


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import  java.util.Arrays;


@Repository
@Scope("request")
public class TagDAO {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addTagToEvent(long id_event,String tag){
        String sql = "INSERT INTO tag_event(id_event,tag) VALUES(?,? )";
        this.jdbcTemplate.update(sql,id_event,tag);
    }
    public void addTagToGroup(long id_group,String tag){
        String sql = "INSERT INTO tag_group(id_group,tag) VALUES(?,?)";
        this.jdbcTemplate.update(sql,id_group,tag);
    }

    public void deleteTagFromGroup(long id_group,String tag){
        String sql = "DELETE FROM tag_group WHERE id_group=? AND tag = ? ";
        this.jdbcTemplate.update(sql,id_group,tag);

    }

    public void deleteTagFromEvent(long id_event,String tag){
        String sql = "DELETE FROM tag_event WHERE id_event=? AND tag = ? ";
        this.jdbcTemplate.update(sql,id_event,tag);
    }

    public List<Map<String,Object>> getTaggedFromEvents(String tag){
        String sql = "SELECT event.* FROM event, tag_event WHERE event.id = tag_event.id_event AND tag_event.tag = ? ";
        return this.jdbcTemplate.queryForList(sql, tag);

    }

/*

SELECT event.*
FROM event,tag_event
WHERE tag_event.id_event = event.id
AND tag_event.tag = "donut"
AND event.date > NOW()
AND NOT EXISTS
    ( SELECT user_event.*
        FROM user_event
         WHERE user_event.id_user = 1 AND user_event.id_event = event.id )

 */
    public List<Map<String,Object>> getTaggedFromEventsNotJoinedUpdate(String tag, long id_user){
        String sql = "SELECT event.* FROM event, tag_event " +
                "WHERE event.id = tag_event.id_event AND tag_event.tag = ?  AND event.date > NOW() AND NOT EXISTS (" +
                " SELECT user_event.* FROM user_event WHERE user_event.id_user = ? AND user_event.id_event = event.id)";
        return this.jdbcTemplate.queryForList(sql,tag,id_user);

    }

    public List<Map<String,Object>> getTaggedFromGroupsNotMembership(String tag, long id_user){
        String sql = "SELECT `group`.* FROM `group`, tag_group " +
                "WHERE `group`.id = tag_group.id_group AND tag_group.tag = ?  AND NOT EXISTS (" +
                " SELECT user_group.* FROM user_group WHERE user_group.id_user = ? AND user_group.id_group = `group`.id)";
        return this.jdbcTemplate.queryForList(sql,tag,id_user);
    }


    public List<Map<String,Object>> getTaggedFromGroups(String tag){
        String sql = "SELECT `group`.* FROM `group`, tag_group WHERE `group`.id = tag_group.id_group AND tag_group.tag = ? ";
        return  this.jdbcTemplate.queryForList(sql,tag);
    }

    public List<Map<String,Object>> getTagsForGroup(long id_group){
        String sql = "SELECT * FROM tag_group WHERE tag_group.id_group = ?";
        return  this.jdbcTemplate.queryForList(sql,id_group);
    }

    public List<Map<String,Object>> getTagsForEvent(long id_event){
        String sql = "SELECT * FROM tag_event WHERE tag_event.id_event = ?";
        return  this.jdbcTemplate.queryForList(sql,id_event);
    }

    public void addTagToUser(long id_user,String tag){
        String sql = "INSERT INTO tag_user(id_user,tag,hidden) VALUES(?,?,FALSE )";
        this.jdbcTemplate.update(sql,id_user,tag);
    }
    public void deleteTagFromUser(long id_user,String tag){
        String sql = "DELETE FROM tag_user WHERE id_user=? AND tag = ? ";
        this.jdbcTemplate.update(sql,id_user,tag);

    }

    public List<Map<String,Object>> getTaggedFromUsers(String tag){
        String sql = "SELECT user.* FROM user, tag_user WHERE user.id = tag_user.id_user AND tag_user.tag = ? ";
        return this.jdbcTemplate.queryForList(sql, tag);

    }
    public List<Map<String,Object>> getTagsForUser(long id_user){
        String sql = "SELECT tag_user.id_user,tag_user.tag FROM tag_user WHERE tag_user.id_user = ? AND tag_user.hidden = FALSE";
        return  this.jdbcTemplate.queryForList(sql,id_user);
    }

    public List<Map<String,Object>> getTagsForUserWithHidden(long id_user)
    {

        String sql = "SELECT * FROM tag_user WHERE tag_user.id_user = ?";
        return  this.jdbcTemplate.queryForList(sql,id_user);

    }

}