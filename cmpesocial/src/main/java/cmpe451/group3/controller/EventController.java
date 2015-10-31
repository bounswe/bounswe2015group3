package cmpe451.group3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.model.EventModel;

import java.util.List;
import java.util.Map;
import java.util.Date;

@Controller
@Scope("request")
public class EventController {

    @Autowired
    private EventModel eventModel = null;

    @RequestMapping(value = "/events")
    public String events(@RequestParam(required = false) Long id, ModelMap model) {

        // gets users from model.
        List<Map<String, Object>> events = eventModel.getEventsForUser(id);

        // sends users List to view.
        model.put("events", events);

        // Spring uses InternalResourceViewResolver and return back index.jsp
        return "events";
    }

    @RequestMapping(value = "/events/edit")
    public String editEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> event = eventModel.getEvent(id);

        model.put("event", event);

        return "event_edit";
    }

    @RequestMapping(value = "events/update")
    public String updateEvent(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date date,
            @RequestParam(required = false) long userid,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description) {

        if (id != null)
        	eventModel.updateEvent(id, name, date, userid, location, description);
        else
        	eventModel.createEvent(name, date, userid, location, description);

        return "redirect:/events";
    }

    @RequestMapping(value = "events/create")
    public String createEvent() {
        return "event_edit";
    }

    @RequestMapping(value = "events/delete")
    public String deleteUser(@RequestParam(required = false) Long id) {
    	eventModel.deleteEvent(id);

        return "redirect:/events";
    }
    
}