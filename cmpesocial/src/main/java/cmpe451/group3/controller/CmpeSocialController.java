package cmpe451.group3.controller;

import cmpe451.group3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.auth.CmpeSocialAuthentication;
import cmpe451.group3.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;
import retrofit.http.POST;


import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class CmpeSocialController {

    @Autowired
    private TagDAO tagDAO = null;

    @Autowired
    private CmpeSocialUserModel cmpeSocialUserModel = null;
    
    @Autowired
    private EventModel eventModel = null;
    
    @Autowired
    private SearchModel searchModel = null;
    
    @Autowired
    private GroupDAO groupDAO = null;

    @RequestMapping(value = "/index")
    public String index(ModelMap model) {

        // gets users from model.
        List<Map<String, Object>> users = cmpeSocialUserModel.getUsers();

        // sends users List to view.
        model.put("users", users);

        return "redirect:/";
    }

    @RequestMapping(value = "user/edit")
    public String editUser(@RequestParam(required = false) Long id, ModelMap model) {
        Map<String, Object> user = cmpeSocialUserModel.getUser(id);

        model.put("user", user);

        return "signup";
    }

    @RequestMapping(value = "user/update")
    public String updateUser(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String photo_url,
            @RequestParam String type )   {

        if (id != null)	{
        	cmpeSocialUserModel.updateUser(id, name, surname, email, SecurityUtils.getHashed(password), photo_url, type);
        }
        else	{
        	if(!cmpeSocialUserModel.checkEmail(email)){
            	cmpeSocialUserModel.addUser(name, surname, email, SecurityUtils.getHashed(password), photo_url, type);
        	}	else	{
        		System.out.println("This email address is already being used.");
                return "redirect:/user/new";
        	}
        }

        return "redirect:/user/login";
    }

    @RequestMapping(value = "user/home")
    public String userHome(@RequestParam(required = false) Long id, ModelMap model,
                           @CookieValue(value="id_user", defaultValue = "") String id_user) {
    	if(id == null){


            Long temp = new Long(id_user);
            long userid = temp;

            Map<String, Object> user = cmpeSocialUserModel.getUser((long)userid);
    		List<Map<String, Object>> events = eventModel.getEventsOfUser((long)userid);
    		List<Map<String, Object>> groups = groupDAO.getGroupWithMembership(userid);
    		
    		model.put("user", user);
    		model.put("events", events);
    		model.put("groups", groups);
    	}
    	else{
    		Map<String, Object> user = cmpeSocialUserModel.getUser(id);
    		List<Map<String, Object>> events = eventModel.getEventsOfUser(id);
            List<Map<String, Object>> groups = groupDAO.getGroupWithMembership(id);
            
    		model.put("user", user);
    		model.put("events", events);
            model.put("groups", groups);
    	}
        return "userHomePage";
    }

    @RequestMapping(value = "user/new")
    public String newUser() {
        return "signup";
    }

    @RequestMapping(value = "user/delete")
    public String deleteUser(@RequestParam(required = false) Long id) {
    	cmpeSocialUserModel.deleteUser(id);

        return "redirect:/signup";
    }
    
    @RequestMapping(value = "/welcome")
    public String welcome() {
        return "welcome";
    }
    
    @RequestMapping(value = {"/", "/home"})
    public String home(ModelMap model,
                       @CookieValue(value="id_user", defaultValue = "") String id_user) {
        // return back to home page

    	//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(id_user == null || id_user.equalsIgnoreCase(""))
    		return "redirect:/welcome";

    	else{
    		List<Map<String, Object>> events = eventModel.getEvents();
            model.put("events", events);
            // gets users from model.
            List<Map<String, Object>> users = cmpeSocialUserModel.getUsers();
            // sends users List to view.
            model.put("users", users);
            //gets groups from model
            List<Map<String, Object>> groups = groupDAO.getAllGroups();
            model.put("groups", groups);
            
            return "index";
    	}
    }
    
    @RequestMapping(value = "user/login")
    public String userLogin(
    		@RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            HttpServletResponse response,
            ModelMap model) {

        if(email != null && cmpeSocialUserModel.checkEmail(email) && password != null){
        	String hashedPassword = cmpeSocialUserModel.getPassword(email);
        	if(SecurityUtils.checkPassword(hashedPassword, password)){
//        		CmpeSocialAuthentication.getAuthentication(email, password);
                long user_id = cmpeSocialUserModel.getIdFromMail(email);
                Long id_user = new Long(user_id);

                Cookie cookie = new Cookie("id_user", id_user.toString());

                cookie.setPath("/cmpesocial/");
                response.addCookie(cookie);
            }
        	else{
        		model.put("error", true);
            	return "login";
        	}
        	return "redirect:/";
        }
        else{
        	return "login";
        }
    }
    
    @RequestMapping(value = "user/logout")
    public String logout(HttpServletResponse response) {

    	//CmpeSocialAuthentication.removeAuthentication();

        Cookie cookie = new Cookie("id_user","");
        cookie.setPath("/cmpesocial/");
        response.addCookie(cookie);


        return "redirect:/";
    }
    
    @RequestMapping(value = "/search")
    public String search(@RequestParam(required = false) String query, ModelMap model) {
    	if(query != null){
    		List<Map<String, Object>> users = searchModel.getUsers(query.trim());
    		List<Map<String, Object>> events = searchModel.getEvents(query.trim());
    		List<Map<String, Object>> groups = searchModel.getGroups(query.trim());
    		model.put("query", query);
    		model.put("users", users);
    		model.put("events", events);
    		model.put("groups", groups);
    	}
        return "searchResult";
    }

    @RequestMapping(value = "/search/tag")
    public String searchTag(@RequestParam(required = false) String query, ModelMap model) {
        if(query != null){
            List<Map<String, Object>> users = tagDAO.getTaggedFromUsers(query);
            List<Map<String, Object>> events = tagDAO.getTaggedFromEvents(query);
            List<Map<String, Object>> groups = tagDAO.getTaggedFromGroups(query);
            model.put("query", query);
            model.put("users", users);
            model.put("events", events);
            model.put("groups", groups);
        }
        return "searchResult";
    }

}