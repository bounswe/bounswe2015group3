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
import cmpe451.group3.utils.SecurityUtils;

import java.util.List;
import java.util.Map;

@Controller
@Scope("request")
public class CmpeSocialController {

    @Autowired
    private CmpeSocialUserModel cmpeSocialUserModel = null;

    @RequestMapping(value = "/index")
    public String index(ModelMap model) {

        // gets users from model.
        List<Map<String, Object>> users = cmpeSocialUserModel.getUsers();

        // sends users List to view.
        model.put("users", users);

        // Spring uses InternalResourceViewResolver and return back index.jsp
        return "redirect:/events";
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
            @RequestParam(required = false) String password) {

        if (id != null)
        	cmpeSocialUserModel.updateUser(id, name, surname, email, SecurityUtils.getHashed(password));
        else
        	cmpeSocialUserModel.addUser(name, surname, email, SecurityUtils.getHashed(password));

        return "redirect:/user/login";
    }

    @RequestMapping(value = "user/home")
    public String home() {
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
    	else
    		return "index";
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
        	return "redirect:/events";
        }
        else
        	return "login";
    }
}