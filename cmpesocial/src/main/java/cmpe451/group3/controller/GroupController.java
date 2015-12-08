package cmpe451.group3.controller;

/**
 * Created by umut on 11/24/15.
 */


import cmpe451.group3.MobileAPI.GroupJoinModel;
import cmpe451.group3.MobileAPI.GroupPostBaseModel;
import cmpe451.group3.MobileAPI.GroupPostModel;
import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.EventIDRequestModel;
import cmpe451.group3.model.EventModel;
import cmpe451.group3.model.GroupDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
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
    public String editEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> group = groupDAO.getGroup(id);

        model.put("group", group);

        return "updateGroup";
    }

    @RequestMapping(value = "/group/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> group = groupDAO.getGroup(id);
        List<Map<String,Object>> members = groupDAO.getMembers(id);
        List<Map<String,Object>> posts = groupDAO.getGroupPosts(id);

        model.put("group", group);
        model.put("members", members);
        model.put("posts",posts);

        return "groupView";
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.GET)
    public String joinEvent(@RequestParam(required = false) Long id, ModelMap model) {
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
            @RequestParam(required = false) Long id_admin,
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
    @ResponseBody
    public String groupInvite(@RequestParam Long id_user, @RequestParam Long id_group) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        groupDAO.invite(id_user, id_group);

        return "redirect:/groups";
    }


    /*
       public Long id_user;
    public Long id_group;
    public String post_text;
    public String post_url;

     */

    @RequestMapping( value = "/groups/createPost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String createPost(@RequestParam Long id_user, @RequestParam Long id_group, @RequestParam String post_text, @RequestParam String post_url) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

       groupDAO.createPost(id_user, id_group,post_text,post_url);

        return  "redirect:/group/view?id="+id_group;
    }

    @RequestMapping( value = "/groups/updatePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String updatePost(@RequestParam Long id,@RequestParam Long id_user, @RequestParam Long id_group, @RequestParam String post_text, @RequestParam String post_url) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        int control =groupDAO.updatePost(id_user,id_group,post_text,post_url,id);

        if (control != 0)
            return  "redirect:/group/view?id="+id_group;
        else
            return  "redirect:/group/updatePost" ;

    }




}

