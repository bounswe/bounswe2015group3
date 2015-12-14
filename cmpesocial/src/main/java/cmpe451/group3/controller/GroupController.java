package cmpe451.group3.controller;

import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.GroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@Controller
@Scope("request")
public class GroupController {

    @Autowired
    private GroupDAO groupDAO = null;

    @Autowired
    private CmpeSocialUserModel userModel = null;
    
    @RequestMapping(value = "/groups")
    public String events(ModelMap model) {

        List<Map<String, Object>> groups = groupDAO.getAllGroups();

        model.put("groups", groups);

        return "groups";
    }

    @RequestMapping(value = "/group/edit")
    public String editEvent(@RequestParam(required = false) long id, ModelMap model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mail = auth.getName();
		int userid = groupDAO.getIdFromMail(mail);
        Map<String, Object> group = groupDAO.getGroup(id);

        if((int)group.get("id_admin") == userid){
        	model.put("group", group);
        	return "updateGroup";
		}

        return "redirect:/groups";
    }

    @RequestMapping(value = "/group/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) long id, ModelMap model) {
        Map<String, Object> group = groupDAO.getGroup(id);
        List<Map<String,Object>> members = groupDAO.getMembers(id);
        
        List<Map<String,Object>> posts = groupDAO.getGroupPosts(id);
        for(Map<String,Object> post: posts){
        	long pid = (int)post.get("id");
        	List<Map<String,Object>> comments = groupDAO.getAllComments(pid);
        	
        	for(Map<String,Object> comment: comments){
        		long authorId = (int)comment.get("id_user");
            	Map<String,Object> author = userModel.getUser(authorId);
            	comment.put("author", author);
        	}
        	long authorId = (int)post.get("id_user");
        	Map<String,Object> author = userModel.getUser(authorId);
        	post.put("author", author);
        	
        	post.put("comments", comments);
        }
        
        model.put("group", group);
        model.put("members", members);
        model.put("posts",posts);

        return "viewGroup";
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.GET)
    public String joinGroup(@RequestParam(required = false) long id, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);

        groupDAO.joinGroup(userid,id);

        return "redirect:/group/view?id="+id;
    }

    @RequestMapping(value = "/group/leave", method = RequestMethod.GET)
    public String leaveGroup(@RequestParam(required = false) long id, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);

        groupDAO.leaveGroup(userid,id);

        return "redirect:/group/view?id="+id;
    }
    
    @RequestMapping(value = "group/update")
    public String updateGroup(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String group_url) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
        if (id != null)
            groupDAO.updateGroup(id, name,userid ,type,description,group_url);
        else
            groupDAO.createGroup(name,userid, type, description, group_url);

        return "redirect:/groups";
    }

    @RequestMapping(value = "group/create")
    public String createEvent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated())
            return "redirect:/user/login";

        return "createGroup";
    }

    @RequestMapping(value = "group/delete")
    public String deleteGroup(@RequestParam(required = false) Long id) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mail = auth.getName();
		int userid = groupDAO.getIdFromMail(mail);
		Map<String, Object> group = groupDAO.getGroup(id);
    	
		if((int)group.get("id_admin") == userid)
			groupDAO.deleteGroup(id);

        return "redirect:/groups";
    }

    @RequestMapping( value = "/groups/invite" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    public String groupInvite(@RequestParam Long id_user, @RequestParam Long id_group) {

        groupDAO.invite(id_user, id_group);

        return "redirect:/groups";
    }

    @RequestMapping( value = "/group/create/post" , method = RequestMethod.POST)
    public String createPost(@RequestParam long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
    	groupDAO.createPost(userid, id_group, post_text, post_url);

    	return "redirect:/group/view?id="+id_group;
    }

    @RequestMapping( value = "/groups/update/post" , method = RequestMethod.POST)
    public String updatePost(@RequestParam Long id, @RequestParam Long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
        int control =groupDAO.updatePost(userid, id_group, post_text, post_url, id);

        if (control != 0)
        	return "redirect:/group/view?id="+id_group;
        else
            return "redirect:/group/updatePost" ;

    }
}

