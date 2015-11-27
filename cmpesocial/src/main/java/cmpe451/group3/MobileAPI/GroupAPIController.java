package cmpe451.group3.MobileAPI;

/**
 * Created by umut on 11/24/15.
 */

import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.EventIDRequestModel;
import cmpe451.group3.model.EventModel;
import cmpe451.group3.model.GroupDAO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        result.put("result","Success");
        result.put("group", groupDAO.getGroupByName(groupModel.name));

        return gson.toJson(result);
    }
    @RequestMapping( value = "/groups/all" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String events() {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",groupDAO.getAllGroups());


        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/view" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEvents(@RequestBody EventIDRequestModel idRequestModelodel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",groupDAO.getGroup(idRequestModelodel.id));


        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/viewOwned" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEvents(@RequestBody EventIDRequestModel eventIDModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result = groupDAO.getGroupOwned(eventIDModel.id);

        return gson.toJson(result);
    }


    @RequestMapping( value = "/groups/update" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventUpdate(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        groupDAO.updateEvent(eventBaseModel.id, eventBaseModel.name, eventBaseModel.date,eventBaseModel.end_date,eventBaseModel.periodic ,eventBaseModel.id_user, eventBaseModel.location, eventBaseModel.description);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/delete" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventDelete(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        groupDAO.deleteEvent(eventBaseModel.id);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/getMembers" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventAttendants(@RequestBody EventIDRequestModel eventID) {
        Gson gson = new Gson();
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

        result = groupDAO.getParticipants(eventID.id);

        return gson.toJson(result);
    }

    @RequestMapping( value = "/groups/join" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventAttendants(@RequestBody EventParticipateModel partModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        groupDAO.joinEvent(partModel.id_user, partModel.id_event);

        result.put("Success","Status");
        return gson.toJson(result);
    }


}
