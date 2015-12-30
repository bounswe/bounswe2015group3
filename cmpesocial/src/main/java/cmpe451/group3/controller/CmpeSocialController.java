package cmpe451.group3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe451.group3.auth.CmpeSocialAuthentication;
import cmpe451.group3.model.CmpeSocialUserModel;
import cmpe451.group3.model.EventModel;
import cmpe451.group3.model.GroupDAO;
import cmpe451.group3.model.SearchModel;
import cmpe451.group3.utils.SecurityUtils;

import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class CmpeSocialController {

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
            @RequestParam String type )   {

        if (id != null)	{
        	cmpeSocialUserModel.updateUser(id, name, surname, email, SecurityUtils.getHashed(password),type);
        }
        else	{
        	if(!cmpeSocialUserModel.checkEmail(email)){
            	cmpeSocialUserModel.addUser(name, surname, email, SecurityUtils.getHashed(password),type);
        	}	else	{
        		System.out.println("This email address is already being used.");
                return "redirect:/user/new";
        	}
        }

        return "redirect:/user/login";
    }

    @RequestMapping(value = "user/home")
    public String userHome(@RequestParam(required = false) Long id, ModelMap model) {
    	if(id == null){
    		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    		String mail = auth.getName();
    		int userid = cmpeSocialUserModel.getIdFromMail(mail);
    		Map<String, Object> user = cmpeSocialUserModel.getUser((long)userid);
    		List<Map<String, Object>> events = eventModel.getEventsOfUser((long)userid);
    		model.put("user", user);
    		model.put("events", events);
    	}
    	else{
    		Map<String, Object> user = cmpeSocialUserModel.getUser(id);
    		List<Map<String, Object>> events = eventModel.getEventsOfUser(id);
    		model.put("user", user);
    		model.put("events", events);
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
    
    @RequestMapping(value = {"/", "/home"})
    public String home(ModelMap model) {
        // return back to home page
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if(auth == null || !auth.isAuthenticated())
    		return "redirect:/user/login";
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
            @RequestParam(required = false) String password) {
        if(email != null && cmpeSocialUserModel.checkEmail(email) && password != null){
        	String hashedPassword = cmpeSocialUserModel.getPassword(email);
        	if(SecurityUtils.checkPassword(hashedPassword, password)){
        		CmpeSocialAuthentication.getAuthentication(email, password);
        	}
        	return "redirect:/";
        }
        else
        	return "login";
    }
    
    @RequestMapping(value = "/search")
    public String search(@RequestParam(required = false) String query, ModelMap model) {
    	if(query != null){
    		List<Map<String, Object>> users = searchModel.getUsers(query.trim());
    		List<Map<String, Object>> events = searchModel.getEvents(query.trim());
    		model.put("query", query);
    		model.put("users", users);
    		model.put("events", events);
    	}
        return "searchResult";
    }
}