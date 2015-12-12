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
        Map<String, Object> group = groupDAO.getGroup(id);

        model.put("group", group);

        return "updateGroup";
    }

    @RequestMapping(value = "/group/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) long id, ModelMap model) {
        Map<String, Object> group = groupDAO.getGroup(id);
        List<Map<String,Object>> members = groupDAO.getMembers(id);
        List<Map<String,Object>> posts = groupDAO.getGroupPosts(id);

        model.put("group", group);
        model.put("members", members);
        model.put("posts",posts);

        return "viewGroup";
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.GET)
    public String joinEvent(@RequestParam(required = false) long id, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);

        groupDAO.joinGroup(userid,id);

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
        groupDAO.deleteGroup(id);

        return "redirect:/groups";
    }

    @RequestMapping( value = "/groups/invite" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    public String groupInvite(@RequestParam Long id_user, @RequestParam Long id_group) {

        groupDAO.invite(id_user, id_group);

        return "redirect:/groups";
    }


    /*
       public Long id_user;
    public Long id_group;
    public String post_text;
    public String post_url;

     */

    @RequestMapping( value = "/group/create/post" , method = RequestMethod.POST)
    public String createPost(@RequestParam long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url) {

    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
    	groupDAO.createPost(userid, id_group, post_text, "þimdilik boþ geç");

    	return "redirect:/group/view?id="+id_group;
    }

    @RequestMapping( value = "/groups/update/post" , method = RequestMethod.POST)
    public String updatePost(@RequestParam Long id, @RequestParam Long id_group, @RequestParam String post_text, @RequestParam(required = false) String post_url) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
        int control =groupDAO.updatePost(userid, id_group, post_text, "þimdilik boþ geç", id);

        if (control != 0)
        	return "redirect:/group/view?id="+id_group;
        else
            return "redirect:/group/updatePost" ;

    }
}

