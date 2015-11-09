package cmpe451.group3.MobileAPI;

/**
 * Created by umut on 11/3/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmpe451.group3.auth.CmpeSocialAuthentication;
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


    @RequestMapping( value = "/events/create" , method = RequestMethod.POST )
    @ResponseBody
    public String createEvent(@RequestBody EventCreateRequestModel eventCreateRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        eventModel.createEvent(eventCreateRequestModel.name,eventCreateRequestModel.date , eventCreateRequestModel.id_user, eventCreateRequestModel.location, eventCreateRequestModel.description);
        result.put("Result", "SUCCESS");
        result.put("event",eventModel.getEventForName(eventCreateRequestModel.name));
        return gson.toJson(result);
    }
    @RequestMapping( value = "/events/all" , method = RequestMethod.POST )
    @ResponseBody
    public String events() {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",eventModel.getEvents());


        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/view" , method = RequestMethod.POST )
    @ResponseBody
    public String viewEvents(@RequestBody UserModel userModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","SUCCESS");
        result.put("events",eventModel.getEventsForUser(userModel.id));


        return gson.toJson(result);
    }


    @RequestMapping( value = "/events/update" , method = RequestMethod.POST )
    @ResponseBody
    public String eventUpdate(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        eventModel.updateEvent(eventBaseModel.id, eventBaseModel.name,  eventBaseModel.date, eventBaseModel.id_user, eventBaseModel.location, eventBaseModel.description);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

    @RequestMapping( value = "/events/delete" , method = RequestMethod.POST )
    @ResponseBody
    public String eventDelete(@RequestBody EventBaseModel eventBaseModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        eventModel.deleteEvent(eventBaseModel.id);
        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }


}