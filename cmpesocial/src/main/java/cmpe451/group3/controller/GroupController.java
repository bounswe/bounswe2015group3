package cmpe451.group3.controller;

/**
 * Created by umut on 11/24/15.
 */


import cmpe451.group3.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;



@Controller
@Scope("request")
public class GroupController {

    @Autowired
    private EventModel eventModel = null;

    @RequestMapping(value = "/group")
    public String events(ModelMap model) {

        List<Map<String, Object>> events = eventModel.getEvents();

        model.put("events", events);

        return "index";
    }

    @RequestMapping(value = "/group/edit")
    public String editEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> event = eventModel.getEvent(id);

        model.put("event", event);

        return "updateEvent";
    }

    @RequestMapping(value = "/group/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> event = eventModel.getEvent(id);
        List<Map<String,Object>> participants = eventModel.getParticipants(id);
        model.put("event", event);
        model.put("participants", participants);

        return "eventView";
    }

    @RequestMapping(value = "/group/join", method = RequestMethod.GET)
    public String joinEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Integer userid = eventModel.getIdFromMail(mail);

        eventModel.joinEvent((long)userid,id);

        return "redirect:/event/view?id="+id;
    }

    @RequestMapping(value = "group/update")
    public String updateEvent(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String end_date,
            @RequestParam(required = false) int periodic,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Integer userid = eventModel.getIdFromMail(mail);
        if (id != null)
            eventModel.updateEvent(id, name, date,end_date ,periodic, userid, location, description);
        else
            eventModel.createEvent(name, date, end_date,periodic, userid, location, description);

        return "redirect:/events";
    }

    @RequestMapping(value = "group/create")
    public String createEvent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || !auth.isAuthenticated())
            return "redirect:/user/login";

        return "createEvent";
    }

    @RequestMapping(value = "group/delete")
    public String deleteUser(@RequestParam(required = false) Long id) {
        eventModel.deleteEvent(id);

        return "redirect:/events";
    }

}

