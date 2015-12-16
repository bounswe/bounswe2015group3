package cmpe451.group3.model;

/**
 * Created by umut on 11/24/15.
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
public class GroupDAO {

    private DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> getAllGroups() {
        String sql = "SELECT * FROM `group`";

        return this.jdbcTemplate.queryForList(sql);
    }


    public List<Map<String, Object>> getGroupOwned(Long id_user) {
        String sql = "SELECT * FROM `group` WHERE  id_admin = ? ";

        return this.jdbcTemplate.queryForList(sql, id_user);
    }

    public List<Map<String,Object>>  getGroupWithMembership(Long id_user){
        String sql = "SELECT `group`.* FROM `group`, user_group WHERE group.id = user_group.id_group AND user_group.id_user = ? ";
        return this.jdbcTemplate.queryForList(sql,id_user);
    }

    public Map<String, Object> getGroup(Long id) {
        String sql = "SELECT * FROM `group` WHERE id = ? ";

        return this.jdbcTemplate.queryForMap(sql, id);
    }

    public Map<String, Object> getGroupByName(String name)
    {
        String sql = "SELECT * FROM `group` WHERE name = ?";
        return  this.jdbcTemplate.queryForMap(sql,name);
    }

    public void createGroup(String name ,Long id_admin, String type, String description, String group_url) {
        String sql = "INSERT INTO `group`(name, id_admin,type,description,group_url) VALUES(?, ?, ?, ?, ?)";

        this.jdbcTemplate.update(sql, name, id_admin,type, description,group_url);
    }

    public void updateGroup(Long id, String name ,Long id_admin, String type, String description, String group_url) {
        String sql = "UPDATE `group` SET name = ? id_admin = ?,type= ? ,description = ?, group_url= ? WHERE id = ?";

        this.jdbcTemplate.update(sql, name, id_admin,type ,description,group_url, id);
    }

    public void deleteGroup(Long id) {
        String sql = "DELETE FROM `group` WHERE id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    public void joinGroup(Long userid, Long groupid) {
        String sql = "INSERT INTO user_group(id_user,id_group,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 1";
        this.jdbcTemplate.update(sql, userid, groupid,1);
    }

    public void leaveGroup(Long id_user, Long id_group)
    {
        String sql = "INSERT INTO user_group(id_user,id_group,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = 0";
        this.jdbcTemplate.update(sql,id_user,id_group,0);
    }

    public void invite(Long userid, Long groupid) {
        String sql = "INSERT INTO user_group(id_user,id_group,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status = IF((status = 1), 1, 2)";
        this.jdbcTemplate.update(sql, userid, groupid, 2);
    }

    public List<Map<String, Object>> getMembers(Long id) {
        String sql = "SELECT user.* FROM user, user_group WHERE user.id = user_group.id_user AND user_group.id_group = ? AND user_group.status = 1";

        List<Map<String, Object>> participants = this.jdbcTemplate.queryForList(sql, id);

        return participants;
    }

    public Boolean isMemberOfGroup(Long id_user,Long id_group)
    {
        String sql = "SELECT user_group.* FROM user_group WHERE user_group.id_user = ? AND user_group.id_group = ? ";

       List<Map<String,Object>> membership = this.jdbcTemplate.queryForList(sql,id_user, id_group);


        if (!membership.isEmpty() && (int)membership.get(0).get("status") == 1)
            return   Boolean.TRUE;
        else
            return   Boolean.FALSE;

    }

    public int createPost(long id_user,long id_group, String text, String content_url)
    {
        String sql = "INSERT INTO post_group(id_group, id_user,post_text,post_url) VALUES(?,?,?,?)";

       return this.jdbcTemplate.update(sql, id_group, id_user, text, content_url);

    }
    public int updatePost(long id_user,long id_group, String text, String content_url,long id)
    {
        String sql = "UPDATE post_group SET id_group = ?, id_user=?,post_text=?,post_url=?) WHERE id= ?";

        return this.jdbcTemplate.update(sql, id_group, id_user, text, content_url, id);

    }
    public List<Map<String,Object>> getGroupPosts(long id_group)
    {
        String sql ="SELECT * FROM post_group WHERE post_group.id_group = ?";
        return this.jdbcTemplate.queryForList(sql,id_group);
    }

    public int createComment(long id_post,long id_group, long id_user, String content)
    {
        String sql = "INSERT INTO comment_group(id_post,id_group,id_user,content) VALUES(?,?,?,?)";
       return this.jdbcTemplate.update(sql,id_post,id_group,id_user,content);
    }

    public int updateComment(long id,long id_post, long id_group, long id_user, String content){
        String sql= "UPDATE  comment_group SET id_post=?,id_group = ? , id_user = ?, content= ? WHERE id= ?";
        return this.jdbcTemplate.update(sql,id_post,id_group,id_user,content,id);
    }

    public int deleteComment(Long id)
    {
        String sql = "DELETE FROM comment_group WHERE id = ?";
       return this.jdbcTemplate.update(sql,id);
    }
    public List<Map<String,Object>> getAllComments(Long id_post)
    {
        String sql = "SELECT * FROM comment_group WHERE id_post = ?";

        return this.jdbcTemplate.queryForList(sql,id_post);
    }

    public Map<String,Object> getComment(Long id)
    {
        String sql = "SELECT * FROM comment_event WHERE id= ?";
        return  this.jdbcTemplate.queryForMap(sql, id);
    }

    
    public Boolean isAvailableForGroup(long id_user,long id_group){

        String sql2 = "SELECT * FROM `user` WHERE id = ?";

        String sql = "SELECT * FROM `group` WHERE id = ? ";

        Map<String,Object> group =  this.jdbcTemplate.queryForMap(sql, id_group);
        Map<String,Object> user = this.jdbcTemplate.queryForMap(sql2,id_user);

        List<String> list_group = new ArrayList<String>(Arrays.asList(group.get("type").toString().split(",")));
        List<String> list_user = new ArrayList<String>(Arrays.asList(user.get("type").toString().split(",")));

        for (String type_group : list_group){
            for (String type_user : list_user)
            {
                if (type_group.equalsIgnoreCase(type_user))
                    return Boolean.TRUE;
            }
        }
        return  Boolean.FALSE;
    }

    public Integer getIdFromMail(String email) {
        String sql = "SELECT id FROM user WHERE email = ? ";
        
        Map<String, Object> user = this.jdbcTemplate.queryForMap(sql, email);
        
        Integer id = (Integer) user.get("id");
        
        return id;
    }



    public void addTagFromGroupToUser(long id_user,long id_group)
    {
        String sql = "SELECT tag_group.tag FROM tag_group WHERE tag_group.id_group = ?";
        List<Map<String,Object>> tag_list = this.jdbcTemplate.queryForList(sql, id_group);

        for (Map<String,Object> tag_map :tag_list)
        {
            String tag = tag_map.get("tag").toString();
            String sqlUser = "INSERT INTO tag_user(id_user,tag,hidden) VALUES(?,?,TRUE)";
            this.jdbcTemplate.update(sqlUser,id_user,tag);
        }

    }
}