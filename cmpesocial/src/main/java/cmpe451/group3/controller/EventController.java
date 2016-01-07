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

import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import javax.servlet.http.HttpServletResponse;
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
    public String events(ModelMap model,
                         @CookieValue(value="id_user", defaultValue = "") String id_user) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
       if (!id_user.equalsIgnoreCase("")) {
           long userid = new Long(id_user);

           List<Map<String, Object>> events = eventModel.getEvents();

           List<Map<String, Object>> events_recommended = searchAPIController.getRecommendEvents(userid);
           model.put("events", events);
           model.put("events_recommended", events_recommended);

           return "events";
       }
        else
           return "redirect:/";

    }

    @RequestMapping(value = "/event/edit")
    public String editEvent(@RequestParam(required = false) Long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String mail = auth.getName();
		int userid = new Integer(id_user);
        Map<String, Object> event = eventModel.getEvent(id);
    	
		if((int)event.get("id_user") == userid){
	        model.put("event", event);
	        return "updateEvent";
		}
		return "redirect:/events";
    }
    
    @RequestMapping(value = "/event/view", method = RequestMethod.GET)
    public String viewEvent(@RequestParam(required = false) Long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {

        if (!eventModel.isAvailableForEvent(id,new Long(id_user)))
         return "redirect:/events";

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
        
      //  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //	String mail = auth.getName();
    	long userid = new Long(id_user);


    	boolean going = eventModel.isGoingToEvent(userid, id);
    	boolean isOwner = false;
    	if((int)userid == (int)event.get("id_user")){
    		isOwner = true;
    	}
    	Map<String,Object> creator = userModel.getUser(new Long((int)event.get("id_user")));
    	model.put("creator", creator);
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
    public String joinEvent(@RequestParam(required = false) Long id, ModelMap model,
                            @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	//String mail = auth.getName();
    	long userid = new Long(id_user);
    	if(!id_user.equalsIgnoreCase("")) {

                eventModel.joinEvent(userid, id);
                return "redirect:/event/view?id=" + id;


        }else
        {
            return  "redirect:/";
        }
    }
    
    @RequestMapping(value = "/event/leave", method = RequestMethod.GET)
    public String leaveEvent(@RequestParam(required = false) Long id, ModelMap model,
                             @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	//String mail = auth.getName();
    	//long userid = eventModel.getIdFromMail(mail);
        if(!id_user.equalsIgnoreCase("")) {
            eventModel.leaveEvent(new Long(id_user), id);

            return "redirect:/event/view?id=" + id;

        }
        else
            return "redirect:/";
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
            @RequestParam(required = false) String photo_url,
            HttpServletResponse response,
            @CookieValue(value="id_user", defaultValue = "") String id_user) {

    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	//String mail = auth.getName();
    	//long userid = eventModel.getIdFromMail(mail);



        if (id != null)
        	eventModel.updateEvent(id, name, date, end_date, periodic, new Long(id_user), location, description, type, null, photo_url);
        else
        	eventModel.createEvent(name, date, end_date, periodic,new Long(id_user), location, description, type, null, photo_url);

        return "redirect:/events";
    }

    @RequestMapping(value = "event/create")
    public String createEvent(@CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(id_user == null || id_user.equalsIgnoreCase(""))
    		return "redirect:/user/login";
    	
        return "createEvent";
    }

    @RequestMapping(value = "event/delete")
    public String deleteEvent(@RequestParam(required = false) Long id,
                              @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//String mail = auth.getName();


		int userid = new Integer(id_user);

		Map<String, Object> event = eventModel.getEvent(id);
    	
		if((int)event.get("id_user") == userid)
    		eventModel.deleteEvent(id);

        return "redirect:/events";
    }
    @RequestMapping( value = "/event/create/post" , method = RequestMethod.POST)
    public String createPost(@RequestParam long id_event, @RequestParam String post_text, @RequestParam(required = false) String post_url,
                             @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	
    	if(post_url == null){
    		post_url="";
    	}
    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //String mail = auth.getName();
        Long userid = new Long(id_user);
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