package cmpe451.group3.controller;

import cmpe451.group3.MobileAPI.SearchAPIController;
import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.GroupDAO;
import cmpe451.group3.model.TagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import javax.servlet.http.HttpServletResponse;


@Controller
@Scope("request")
public class GroupController {

    @Autowired
    private GroupDAO groupDAO = null;

    @Autowired
    private CmpeSocialUserModel userModel = null;
    
    @Autowired
    private TagDAO tagModel = null;

    @Autowired
    private SearchAPIController searchAPIController = null;
    
    @RequestMapping(value = "/groups")
    public String events(ModelMap model,
                         @CookieValue(value="id_user", defaultValue = "") String id_user) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
        if(!id_user.equals("")) {
            long userid = new Long(id_user);

            List<Map<String, Object>> groups = groupDAO.getAllGroups();
            List<Map<String, Object>> groups_recommended = searchAPIController.getRecommendGroups(userid);
            model.put("groups", groups);
            model.put("groups_recommended", groups_recommended);
            return "groups";
        }else
            return "redirect:/";
    }

    @RequestMapping(value = "/group/edit")
    public String editEvent(@RequestParam(required = false) long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String mail = auth.getName();


        int userid = new Integer(id_user);

        Map<String, Object> group = groupDAO.getGroup(id);

        if((int)group.get("id_admin") == userid){
        	model.put("group", group);
        	return "editGroup";
		}

        return "redirect:/groups";
    }

    @RequestMapping(value = "/group/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {
      //  if(groupDAO.isAvailableForGroup(new Long(id_user),id))
        //    return "redirect:/groups";

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
        
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	//String mail = auth.getName();
    	long userid = new Long(id_user);

        boolean isMember = groupDAO.isMemberOfGroup(userid, id);
    	boolean isOwner = false;
    	if((int)userid == (int)group.get("id_admin")){
    		isOwner = true;
    	}
    	
    	List<Map<String,Object>> tags = tagModel.getTagsForGroup(id);
    	
    	Map<String,Object> creator = userModel.getUser(new Long((int)group.get("id_admin")));
    	model.put("creator", creator);
    	model.put("tags", tags);
    	
    	model.put("isOwner", isOwner);
    	model.put("isMember", isMember);
        model.put("group", group);
        model.put("members", members);
        model.put("posts",posts);

        return "viewGroup";
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.GET)
    public String joinGroup(@RequestParam(required = false) long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
        Long userid = new Long(id_user);


         groupDAO.joinGroup(userid,id);

         return "redirect:/group/view?id="+id;

    }

    @RequestMapping(value = "/group/leave", method = RequestMethod.GET)
    public String leaveGroup(@RequestParam(required = false) long id, ModelMap model,
                             @CookieValue(value="id_user", defaultValue = "") String id_user) {
      //  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       // String mail = auth.getName();
        Long userid = new Long(id_user);

        groupDAO.leaveGroup(userid,id);

        return "redirect:/group/view?id="+id;
    }
    
    @RequestMapping(value = "group/update")
    public String updateGroup(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String group_url,
            @CookieValue(value="id_user", defaultValue = "") String id_user) {

      //  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       // String mail = auth.getName();
        Long userid = new Long(id_user);
        if (id != null)
            groupDAO.updateGroup(id, name, userid, type, description, group_url);
        else
            groupDAO.createGroup(name, userid, type, description, group_url);

        return "redirect:/groups";
    }

    @RequestMapping(value = "group/create")
    public String createEvent( @CookieValue(value="id_user", defaultValue = "") String id_user) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(id_user == null || id_user.equals(""))
            return "redirect:/user/login";

        return "createGroup";
    }

    @RequestMapping(value = "group/delete")
    public String deleteGroup(@RequestParam(required = false) Long id,
                              @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String mail = auth.getName();
		int userid = new Integer(id_user);
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
    public String createPost(@RequestParam long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url,
                             @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
        Long userid = new Long(id_user);
    	groupDAO.createPost(userid, id_group, post_text, post_url);

    	return "redirect:/group/view?id="+id_group;
    }

    @RequestMapping( value = "/groups/update/post" , method = RequestMethod.POST)
    public String updatePost(@RequestParam Long id, @RequestParam Long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url,
                             @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
        Long userid = new Long(id_user);
        int control =groupDAO.updatePost(userid, id_group, post_text, post_url, id);

        if (control != 0)
        	return "redirect:/group/view?id="+id_group;
        else
            return "redirect:/group/updatePost" ;

    }
    
    @RequestMapping( value = "/group/tag/add" , method = RequestMethod.POST)
    public String addTag(@RequestParam long id_group, @RequestParam String tag) {
    	
    	if(tag == null){
    		return "redirect:/group/view?id="+id_group;
    	}
        tagModel.addTagToGroup(id_group, tag);
    	return "redirect:/group/view?id="+id_group;
    }
    
}

