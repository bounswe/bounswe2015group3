package cmpe451.group3.controller;

import cmpe451.group3.MobileAPI.SearchAPIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.EventModel;
import cmpe451.group3.model.TagDAO;

import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class EventController {

    @Autowired
    private EventModel eventModel = null;
    
    @Autowired
    private CmpeSocialUserModel userModel = null;
    
    @Autowired
    private TagDAO tagModel = null;

    @Autowired
    private SearchAPIController searchAPIController= null;


    @RequestMapping(value = "/events")
    public String events(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        long userid = userModel.getIDUserByEmail(mail);

        List<Map<String, Object>> events = eventModel.getEvents();

        List<Map<String, Object>> events_recommended = searchAPIController.getRecommendEvents(userid);
        model.put("events", events);
        model.put("events_recommended",events_recommended);

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
        	
        	for(Map<String,Object> comment: comments){
        		long authorId = (int)comment.get("id_user");
            	Map<String,Object> author = userModel.getUser(authorId);
            	comment.put("author", author);
        	}
        	long authorId = (int)post.get("id_user");
        	Map<String,Object> author = userModel.getUser(authorId);
        	post.put("author", author);
        	
        	post.put("comments", comments);
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String mail = auth.getName();
    	long userid = eventModel.getIdFromMail(mail);
    	boolean going = eventModel.isGoingToEvent(userid, id);
    	boolean isOwner = false;
    	if((int)userid == (int)event.get("id_user")){
    		isOwner = true;
    	}
    	
    	List<Map<String,Object>> tags = tagModel.getTagsForEvent(id);
    	model.put("tags", tags);
    	
    	model.put("isOwner", isOwner);
        model.put("going", going);
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
            @RequestParam(required = false) Integer periodic,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String photo_url) {

    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	String mail = auth.getName();
    	long userid = eventModel.getIdFromMail(mail);
		
        if (id != null)
        	eventModel.updateEvent(id, name, date, end_date, periodic, userid, location, description, type, null, photo_url);
        else
        	eventModel.createEvent(name, date, end_date, periodic, userid, location, description, type, null, photo_url);

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
    @RequestMapping( value = "/event/create/post" , method = RequestMethod.POST)
    public String createPost(@RequestParam long id_event, @RequestParam String post_text, @RequestParam(required = false) String post_url) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String mail = auth.getName();
        Long userid = userModel.getIDUserByEmail(mail);
        eventModel.createPost(id_event, userid, post_text, post_url);

    	return "redirect:/event/view?id="+id_event;
    }

    @RequestMapping( value = "/event/tag/add" , method = RequestMethod.POST)
    public String addTag(@RequestParam long id_event, @RequestParam String tag) {
    	
    	if(tag == null){
    		return "redirect:/event/view?id="+id_event;
    	}
        tagModel.addTagToEvent(id_event, tag);
    	return "redirect:/event/view?id="+id_event;
    }
    
}