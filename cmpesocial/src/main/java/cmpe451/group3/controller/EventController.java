package cmpe451.group3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.model.EventModel;

import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class EventController {

    @Autowired
    private EventModel eventModel = null;

    @RequestMapping(value = "/events")
    public String events(ModelMap model) {
        
        List<Map<String, Object>> events = eventModel.getEvents();

        model.put("events", events);

        return "events";
    }

    @RequestMapping(value = "/event/edit")
    public String editEvent(@RequestParam(required = false) Long id, ModelMap model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mail = auth.getName();
		int userid = eventModel.getIdFromMail(mail);
        Map<String, Object> event = eventModel.getEvent(id);
    	
		if((int)event.get("id_user") == userid){
	        model.put("event", event);
	        return "updateEvent";
		}
		return "redirect:/events";
    }
    
    @RequestMapping(value = "/event/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> event = eventModel.getEvent(id);
        List<Map<String,Object>> participants = eventModel.getParticipants(id);
        
        List<Map<String,Object>> posts = eventModel.getAllPosts(id);
        for(Map<String,Object> post: posts){
        	long pid = (int)post.get("id");
        	List<Map<String,Object>> comments = eventModel.getAllComments(pid);
        	post.put("comments", comments);
        }
        
        model.put("event", event);
        model.put("participants", participants);
        model.put("posts", posts);
        
        
        return "eventView";
    }
    
    @RequestMapping(value = "/event/join", method = RequestMethod.GET)
    public String joinEvent(@RequestParam(required = false) Long id, ModelMap model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String mail = auth.getName();
    	long userid = eventModel.getIdFromMail(mail);
    	
    	eventModel.joinEvent(userid,id);
        
        return "redirect:/event/view?id="+id;
    }
    
    @RequestMapping(value = "/event/leave", method = RequestMethod.GET)
    public String leaveEvent(@RequestParam(required = false) Long id, ModelMap model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String mail = auth.getName();
    	long userid = eventModel.getIdFromMail(mail);
    	eventModel.leaveEvent(userid,id);
        
        return "redirect:/event/view?id="+id;
    }

    @RequestMapping(value = "events/update")
    public String updateEvent(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String end_date,
            @RequestParam(required = false) int periodic,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String mail = auth.getName();
    	long userid = eventModel.getIdFromMail(mail);
		
        if (id != null)
        	eventModel.updateEvent(id, name, date,end_date, periodic, userid, location, description);
        else
        	eventModel.createEvent(name, date, end_date,periodic, userid, location, description);

        return "redirect:/events";
    }

    @RequestMapping(value = "event/create")
    public String createEvent() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(auth == null || !auth.isAuthenticated())
    		return "redirect:/user/login";
    	
        return "createEvent";
    }

    @RequestMapping(value = "event/delete")
    public String deleteEvent(@RequestParam(required = false) Long id) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mail = auth.getName();
		int userid = eventModel.getIdFromMail(mail);
		Map<String, Object> event = eventModel.getEvent(id);
    	
		if((int)event.get("id_user") == userid)
    		eventModel.deleteEvent(id);

        return "redirect:/events";
    }
    
}