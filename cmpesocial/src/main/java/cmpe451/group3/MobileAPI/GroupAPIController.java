package cmpe451.group3.MobileAPI;

/**
 * Created by umut on 11/24/15.
 */

import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.EventIDRequestModel;
import cmpe451.group3.model.EventModel;
import cmpe451.group3.model.GroupDAO;
import com.google.gson.Gson;
import com.sun.net.httpserver.Authenticator;
import com.sun.org.apache.xml.internal.serialize.ElementState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.acl.Group;
import java.util.*;


/**
 * API Controller Class
 * This class includes following functions
 */
@Controller
@RequestMapping("api")
@Scope("request")
public class GroupAPIController {

    @Qualifier("groupDAO")
    @Autowired
    private GroupDAO groupDAO = null;


    @RequestMapping( value = "/groups/create" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String createEvent(@RequestBody GroupModel groupModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        groupDAO.createGroup(groupModel.name,groupModel.id_admin,groupModel.type,groupModel.description,groupModel.group_url);
        result.put("Result","Success");
        result.put("group", groupDAO.getGroupByName(groupModel.name));

        return gson.toJson(result);
    }
    @RequestMapping( value = "/groups/all" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String events(@RequestBody EventIDRequestModel groupIDRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String,Object>> groups = groupDAO.getAllGroups();
        for (Map<String,Object> group : groups)
        {
            Long id_group = Long.parseLong(group.get("id").toString());
            if (groupDAO.isMemberOfGroup(groupIDRequestModel.id,id_group))
            {
                    group.put("isMember","yes");
            }else
            {
                group.put("isMember","no");
            }
        }

        result.put("Result","SUCCESS");
        result.put("groups",groups);


        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/view" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEvents(@RequestBody GroupViewRequestModel groupModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        if (groupDAO.isAvailableForGroup(groupModel.id_user,groupModel.id_group))
        {
            result.put("Result", "SUCCESS");
            result.put("group", groupDAO.getGroup(groupModel.id_group));
        }
        else
        {
            result.put("Result", "FAILURE");
            result.put("Reason", "This group is not allowed to the user");
        }

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/viewOwned" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEventsOwned(@RequestBody EventIDRequestModel eventIDModel) {
        Gson gson = new Gson();
        List<Map<String, Object>> result = groupDAO.getGroupOwned(eventIDModel.id);

        return gson.toJson(result);
    }


    @RequestMapping( value = "/groups/update" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventUpdate(@RequestBody GroupModel groupModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        groupDAO.updateGroup(groupModel.id, groupModel.name,groupModel.id_admin, groupModel.type, groupModel.description,groupModel.group_url);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/delete" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String groupDelete(@RequestBody EventIDRequestModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        groupDAO.deleteGroup(eventBaseModel.id);
        result.put("Result","Success");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/getMembers" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String groupMembers(@RequestBody EventIDRequestModel eventID) {
        Gson gson = new Gson();
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

        result = groupDAO.getMembers(eventID.id);

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/join" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String groupJoin(@RequestBody GroupJoinModel partModel) {


        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();
        if (groupDAO.isAvailableForGroup(partModel.id_user,partModel.id_group)) {
            groupDAO.joinGroup(partModel.id_user, partModel.id_group);
            result.put("Result","Success");
        }
        else
        {
            result.put("Result","Failure");
            result.put("Reason","This Group restricted to the user");
        }
        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/invite" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String groupInvite(@RequestBody GroupJoinModel partModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        groupDAO.invite(partModel.id_user, partModel.id_group);

        result.put("Result","Success");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/createPost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String createPost(@RequestBody GroupPostModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        int control =groupDAO.createPost(postModel.id_user, postModel.id_group, postModel.post_text,postModel.post_url);

        if (control != 0)
            result.put("Result","Success");
        else
            result.put("Result","Failure");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/updatePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String updatePost(@RequestBody GroupPostBaseModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        int control =groupDAO.updatePost(postModel.id_user, postModel.id_group, postModel.post_text, postModel.post_url, postModel.id);

        if (control != 0)
            result.put("Result","Success");
        else
            result.put("Result","Failure");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/getPosts" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getPosts(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();

        Map<String,Object> result = new HashMap<>();


        result.put("Result","Success");
        result.put("posts",groupDAO.getGroupPosts(idModel.id));


        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/leave" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getPosts(@RequestBody GroupJoinModel idModel) {
        Gson gson = new Gson();

        Map<String,Object> result = new HashMap<>();

        groupDAO.leaveGroup(idModel.id_user,idModel.id_group);
        result.put("Result","Success");

        return gson.toJson(result);
    }


}
