package cmpe451.group3.MobileAPI;

/**
 * Created by umut on 11/3/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmpe451.group3.auth.CmpeSocialAuthentication;
import cmpe451.group3.model.EventIDRequestModel;
import cmpe451.group3.utils.SecurityUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import  cmpe451.group3.MobileAPI.UserModel;
import  cmpe451.group3.model.CmpeSocialUserModel;
import  cmpe451.group3.MobileAPI.UserLoginRequestModel;
import  cmpe451.group3.utils.EventDAO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.model.EventModel;



import cmpe451.group3.MobileAPI.EventCreateRequestModel;

import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * API Controller Class
 * This class includes following functions
 */
@Controller
@RequestMapping("api")
@Scope("request")
public class EventAPIController {


    @Qualifier("cmpeSocialUserModel")
    @Autowired
    private CmpeSocialUserModel cmpeSocialUserModel = null;


    @Qualifier("eventModel")
    @Autowired
    private EventModel eventModel = null;


    @RequestMapping( value = "/events/create" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String createEvent(@RequestBody EventCreateRequestModel eventCreateRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        eventModel.createEvent(eventCreateRequestModel.name, eventCreateRequestModel.date,eventCreateRequestModel.end_date,eventCreateRequestModel.periodic, eventCreateRequestModel.id_user, eventCreateRequestModel.location, eventCreateRequestModel.description);
        result.put("Result", "SUCCESS");
        result.put("event", eventModel.getEventForName(eventCreateRequestModel.name));

        return gson.toJson(result);
    }
    @RequestMapping( value = "/events/all" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String events() {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",eventModel.getEvents());


        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/view" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEvents(@RequestBody UserModel userModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",eventModel.getEventsForUser(userModel.id));


        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/viewDetail" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEvents(@RequestBody EventIDRequestModel eventIDModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result = eventModel.getEvent(eventIDModel.id);

        return gson.toJson(result);
    }


    @RequestMapping( value = "/events/update" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventUpdate(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        eventModel.updateEvent(eventBaseModel.id, eventBaseModel.name, eventBaseModel.date,eventBaseModel.end_date,eventBaseModel.periodic ,eventBaseModel.id_user, eventBaseModel.location, eventBaseModel.description);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/delete" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventDelete(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        eventModel.deleteEvent(eventBaseModel.id);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/getParticipants" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventAttendants(@RequestBody EventIDRequestModel eventID) {
        Gson gson = new Gson();
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

        result = eventModel.getParticipants(eventID.id);

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/join" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventJoin(@RequestBody EventParticipateModel partModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.joinEvent(partModel.id_user, partModel.id_event);

        result.put("Success","Status");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/createPost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostCreate (@RequestBody EventPostCreateRequestModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.createPost(postModel.id_event,postModel.id_user,postModel.content,postModel.content_url);

        result.put("Success","Status");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/updatePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostUpdate(@RequestBody EventPostBaseModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.updatePost(postModel.id,postModel.id_event,postModel.id_user,postModel.content,postModel.content_url);

        result.put("Success","Status");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/deletePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostDelete(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.deletePost(idModel.id);

        result.put("Success","Status");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/getAllPosts" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostGetAll(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        List<Map<String,Object>> posts = new ArrayList<>();

        posts = eventModel.getAllPosts(idModel.id);
        for (Map<String ,Object> post : posts){
            List<Map<String,Object>> comments = new ArrayList<>();
            comments = eventModel.getAllComments(Long.parseLong(post.get("id").toString()));
            post.put("comments",comments);
        }

        result.put("Success","Status");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/createComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentCreate (@RequestBody EventCreateCommentModel commentModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.createComment(commentModel.id_post,commentModel.id_event,commentModel.id_user,commentModel.content);
        result.put("Success","Status");
        return gson.toJson(result);
    }


    @RequestMapping( value = "/events/deleteComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentDelete (@RequestBody EventIDRequestModel idModel) {

        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.deleteComment(idModel.id);

        result.put("Success","Status");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/updateComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentUpdate (@RequestBody EventCommentBaseModel commentModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();
        eventModel.updateComment(commentModel.id,commentModel.id_post,commentModel.id_event,commentModel.id_user,commentModel.content);
        result.put("Success","Status");
        return gson.toJson(result);
    }

}
