package cmpe451.group3.MobileAPI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmpe451.group3.auth.CmpeSocialAuthentication;
import cmpe451.group3.model.EventIDRequestModel;
import cmpe451.group3.model.TagDAO;
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

/**
 * API Controller Class
 * This class includes following functions
 * @author Umut Afacan
 */

@Controller
@RequestMapping("api")
@Scope("request")
public class UserAPIController {

    @Qualifier("userModel")
    @Autowired
    private UserModel userModel = null;

    @Qualifier("cmpeSocialUserModel")
    @Autowired
    private CmpeSocialUserModel cmpeSocialUserModel = null;


    @Qualifier("tagDAO")
    @Autowired
    private TagDAO tagDAO = null;
/**
 * Method for login action 
 * @param userLoginRequestModel
 * @return
 */
    @RequestMapping( value = "/login" , method = RequestMethod.POST,produces ={ "text/plain;charset=UTF-8"} )
    @ResponseBody
    public String login(@RequestBody UserLoginRequestModel userLoginRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        if(userLoginRequestModel.email != null && userLoginRequestModel.password != null){
            String hashedPassword = cmpeSocialUserModel.getPassword(userLoginRequestModel.email);

            if(SecurityUtils.checkPassword(hashedPassword, userLoginRequestModel.password)){
                CmpeSocialAuthentication.getAuthentication(userLoginRequestModel.email, userLoginRequestModel.password);
                result.put("type", "SUCCESS");

                result.put("user", userModel.MapModelFromDAO(cmpeSocialUserModel.getUserByEmail(userLoginRequestModel.email)));
            }
            else
            {
                result.put("type","WRONG_PASSWORD");
                result.put("password" , userLoginRequestModel.password);
            }
        }
        else
        {
            result.put("type","MISSING_CREDENTIAL");
        }
            return gson.toJson(result);
    }

    /**
     * Method for sign up action
     * @param userSignupRequestModel
     * @return
     */
    @RequestMapping( value = "/signup" , method = RequestMethod.POST,produces ={ "text/plain;charset=UTF-8"} )
    @ResponseBody
    public String signup(@RequestBody UserSignupRequestModel userSignupRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        cmpeSocialUserModel.addUser(userSignupRequestModel.name, userSignupRequestModel.surname, userSignupRequestModel.email,SecurityUtils.getHashed(userSignupRequestModel.password),"",userSignupRequestModel.type );

        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }

/**
 * Method for update action for user information
 * @param userRequestModel
 * @return
 */
    @RequestMapping( value = "/updateUser" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String updateUser(@RequestBody UserModel userRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        cmpeSocialUserModel.updateUser(userRequestModel.id, userRequestModel.name, userRequestModel.surname, userRequestModel.email, SecurityUtils.getHashed(userRequestModel.password),"",userRequestModel.type);

        result.put("Result","SUCCESS");

        return gson.toJson(result);
    }
/**
 * Method for getting specific user
 * @param userRequestModel
 * @return
 */
    @RequestMapping( value = "/getUser" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String updateUser(@RequestBody EventIDRequestModel userRequestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        result = cmpeSocialUserModel.getUser(userRequestModel.id);
        return gson.toJson(result);
    }
/**
 * Method for adding tag to a specific user
 * @param tagModel
 * @return
 */
    @RequestMapping( value = "/user/tag/add" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String addTagToUser(@RequestBody UserTagModel tagModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Result","Success");
        tagDAO.addTagToUser(tagModel.id_user,tagModel.tag);
        return gson.toJson(result);
    }
/**
 * Method for deleting a tag belongs to an specific user
 * @param tagModel
 * @return
 */
    @RequestMapping( value = "/user/tag/delete" , method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"} )
    @ResponseBody
    public String deleteTagToUser(@RequestBody UserTagModel tagModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Result","Success");
        tagDAO.deleteTagFromUser(tagModel.id_user,tagModel.tag);
        return gson.toJson(result);
    }

    /**
     * Method for getting users having specific tag
     * @param tagModel
     * @return
     */
    @RequestMapping(value = "/user/tag/getUsers" ,method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getUsersForTag(@RequestBody SimpleTagModel tagModel)
    {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();
        result.put("users", tagDAO.getTaggedFromUsers(tagModel.tag));
        result.put("Result","Success");
        return  gson.toJson(result);
    }
/**
 * Method for getting all tags belong to an user
 * @param tagModel
 * @return
 */
    @RequestMapping(value = "/user/tag/getTags" ,method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getTagsForUser(@RequestBody EventIDRequestModel tagModel)
    {
        Gson gson = new Gson();
        Map<String,Object> result = new HashMap<>();
        result.put("tags", tagDAO.getTagsForUser(tagModel.id));
        result.put("Result","Success");
        return  gson.toJson(result);

    }

}