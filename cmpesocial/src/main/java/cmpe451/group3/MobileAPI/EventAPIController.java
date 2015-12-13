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
import com.sun.org.apache.xpath.internal.operations.Bool;
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
        eventModel.createEvent(eventCreateRequestModel.name, eventCreateRequestModel.date,eventCreateRequestModel.end_date,eventCreateRequestModel.periodic, eventCreateRequestModel.id_user, eventCreateRequestModel.location, eventCreateRequestModel.description,eventCreateRequestModel.type);
        result.put("Result", "SUCCESS");
        result.put("event", eventModel.getEventForName(eventCreateRequestModel.name));

        return gson.toJson(result);
    }
    @RequestMapping( value = "/events/all" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String events(@RequestBody EventIDRequestModel requestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> events = new ArrayList<>();
        result.put("Result","SUCCESS");
        events = eventModel.getEvents();

        for(Map<String, Object> event : events){
            Long id_event = Long.parseLong(event.get("id").toString());
            if (eventModel.isGoingToEvent(requestModel.id,id_event))
            {
                event.put("hasJoined", Boolean.TRUE);
            }else
            {
                event.put("hasJoined",Boolean.FALSE);
            }
        }
        result.put("events",events);
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/viewByOwned" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEventsOwned(@RequestBody EventIDRequestModel userModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();


        result.put("Result","SUCCESS");

        List<Map<String, Object>> events = new ArrayList<>();

        events = eventModel.getEventsForUser(userModel.id);

        for(Map<String, Object> event : events){
            Long id_event = Long.parseLong(event.get("id").toString());
            if (eventModel.isGoingToEvent(userModel.id,id_event))
            {
                event.put("hasJoined", Boolean.TRUE);
            }else
            {
                event.put("hasJoined",Boolean.FALSE);
            }
        }
        result.put("events",events);

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/viewJoined" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEventsJoined(@RequestBody EventIDRequestModel eventIDModel) {
        Gson gson = new Gson();
        List<Map<String, Object>> result = eventModel.getEventsJoined(eventIDModel.id);

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/viewDetail" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String viewEventDetail(@RequestBody EventParticipateModel eventParticipateModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result = eventModel.getEvent(eventParticipateModel.id_event);

        if (eventModel.isGoingToEvent(eventParticipateModel.id_user,eventParticipateModel.id_event))
        {
            result.put("hasJoined", Boolean.TRUE);
        }else
        {
            result.put("hasJoined",Boolean.FALSE);
        }


        return gson.toJson(result);
    }


    @RequestMapping( value = "/events/update" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventUpdate(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        eventModel.updateEvent(eventBaseModel.id, eventBaseModel.name, eventBaseModel.date,eventBaseModel.end_date,eventBaseModel.periodic ,eventBaseModel.id_user, eventBaseModel.location, eventBaseModel.description,eventBaseModel.type);
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

        if(eventModel.isAvailableForEvent(partModel.id_event,partModel.id_user)) {
            eventModel.joinEvent(partModel.id_user, partModel.id_event);
            result.put("Result","Success");
        }else
        {
            result.put("Result","Failure");
            result.put("Message","User has not access to join.");

        }

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/leave" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventLeave(@RequestBody EventParticipateModel partModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();


            eventModel.leaveEvent(partModel.id_user, partModel.id_event);
            result.put("Result","Success");


        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/createPost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostCreate (@RequestBody EventPostCreateRequestModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.createPost(postModel.id_event,postModel.id_user,postModel.content,postModel.content_url);

        result.put("Result","Success");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/updatePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostUpdate(@RequestBody EventPostBaseModel postModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.updatePost(postModel.id,postModel.id_event,postModel.id_user,postModel.content,postModel.content_url);

        result.put("Result","Success");
        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/deletePost" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventPostDelete(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.deletePost(idModel.id);

        result.put("Result","Success");
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

        result.put("posts",posts);
        result.put("Result","Success");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/createComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentCreate (@RequestBody EventCreateCommentModel commentModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.createComment(commentModel.id_post,commentModel.id_event,commentModel.id_user,commentModel.content);
        result.put("Result","Success");
        return gson.toJson(result);
    }


    @RequestMapping( value = "/events/deleteComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentDelete (@RequestBody EventIDRequestModel idModel) {

        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();

        eventModel.deleteComment(idModel.id);

        result.put("Result","Success");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/updateComment" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String eventCommentUpdate (@RequestBody EventCommentBaseModel commentModel) {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();
        eventModel.updateComment(commentModel.id,commentModel.id_post,commentModel.id_event,commentModel.id_user,commentModel.content);
        result.put("Result","Success");
        return gson.toJson(result);
    }



}
