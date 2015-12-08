package cmpe451.group3.MobileAPI;

/**
 * Created by umut on 12/9/15.
 */


import cmpe451.group3.model.*;
import com.google.gson.Gson;
import com.sun.net.httpserver.Authenticator;
import com.sun.org.apache.xml.internal.serialize.ElementState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.acl.Group;
import java.util.*;

@Controller
@RequestMapping("api")
@Scope("request")
public class SearchAPI {
    @Qualifier("searchModel")
    @Autowired
    private SearchModel searchModel;


    @RequestMapping( value = "/search/users" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String searchUsers(@RequestBody SearchRequestModel requestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","Success");
        result.put("Users",searchModel.getUsers(requestModel.text));
        return gson.toJson(result);
    }

    @RequestMapping( value = "/search/events" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String searchEvents(@RequestBody SearchRequestModel requestModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("Result","Success");
        result.put("Events", searchModel.getEvents(requestModel.text));
        return gson.toJson(result);
    }

}
