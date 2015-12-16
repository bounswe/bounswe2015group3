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
import org.springframework.security.core.userdetails.User;
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
    private SearchModel searchModel =null;

    @Qualifier("tagDAO")
    @Autowired
    private TagDAO tagDAO = null;

    @Qualifier("eventModel")
    @Autowired
    private EventModel eventModel = null;

    @Qualifier("groupDAO")
    @Autowired
    private GroupDAO groupDAO = null;

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


    @RequestMapping( value = "/search/recommendEvents" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String recommendEvents(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String,Object>> tagsOfUser = tagDAO.getTagsForUserWithHidden(idModel.id);

        List<List<Map<String ,Object>>> listOfListofEvents  = new ArrayList<>();

        for (Map<String,Object> tagmap : tagsOfUser)
        {
            listOfListofEvents.add(tagDAO.getTaggedFromEventsNotJoinedUpdate(tagmap.get("tag").toString(),idModel.id));
        }

        Map<Long,Integer> weightList = new HashMap<>();

        for(List<Map<String,Object>> list : listOfListofEvents)
            for(Map<String,Object> map : list)
            {
                Long id_event = Long.parseLong(map.get("id").toString());
                if ( weightList.get(id_event) != null){

                   Integer weight = weightList.get(id_event);
                    weight++;
                    weightList.put(id_event,weight);

                }else
                {
                    weightList.put(id_event,1);

                }

            }
            weightList = sortByComparator(weightList);
            weightList = putFirstEntries(5, weightList);
            List<Map<String,Object>> events = new ArrayList<>();
            for(Long id :  weightList.keySet())
            {
              events.add(eventModel.getEvent(id));
            }
            result.put("Events",events);
        return gson.toJson(result);
    }


    //method can event keywords but ignore them this code evaluated from above
    @RequestMapping( value = "/search/recommendGroups" , method = RequestMethod.POST ,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String recommendGroups(@RequestBody EventIDRequestModel idModel) {
        Gson gson = new Gson();
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String,Object>> tagsOfUser = tagDAO.getTagsForUserWithHidden(idModel.id);

        List<List<Map<String ,Object>>> listOfListofEvents  = new ArrayList<>();

        for (Map<String,Object> tagmap : tagsOfUser)
        {
            listOfListofEvents.add(tagDAO.getTaggedFromGroupsNotMembership(tagmap.get("tag").toString(),idModel.id));
        }

        Map<Long,Integer> weightList = new HashMap<>();

        for(List<Map<String,Object>> list : listOfListofEvents)
            for(Map<String,Object> map : list)
            {
                Long id_event = Long.parseLong(map.get("id").toString());
                if ( weightList.get(id_event) != null){
                    Integer weight = weightList.get(id_event);
                    weight++;
                    weightList.put(id_event,weight);
                }else
                {
                    weightList.put(id_event,1);
                }
            }
        weightList = sortByComparator(weightList);
        weightList = putFirstEntries(5, weightList);
        List<Map<String,Object>> events = new ArrayList<>();
        for(Long id :  weightList.keySet())
        {
            events.add(groupDAO.getGroup(id));
        }
        result.put("Groups",events);
        return gson.toJson(result);
    }


    public List<UserTagModel> makeTagModelArray(List<Map<String,Object>> mapList)
    {
        List<UserTagModel> tagList = new ArrayList<>();
        for (Map<String ,Object> map :mapList) {
            UserTagModel tagModel = new UserTagModel();
            tagModel.mapToObject(map);
            tagList.add(tagModel);
        }
        return  tagList;
    }

    private static Map<Long, Integer> sortByComparator(Map<Long, Integer> unsortMap) {

        // Convert Map to List
        List<Map.Entry<Long, Integer>> list =
                new LinkedList<Map.Entry<Long, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<Long, Integer>>() {
            public int compare(Map.Entry<Long, Integer> o1,
                               Map.Entry<Long, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<Long, Integer> sortedMap = new LinkedHashMap<Long, Integer>();
        for (Iterator<Map.Entry<Long, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Long, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static Map<Long,Integer> putFirstEntries(int max, Map<Long,Integer> source) {
        int count = 0;
        TreeMap<Long,Integer> target = new TreeMap<Long,Integer>();
        for (Map.Entry<Long,Integer> entry:source.entrySet()) {
            if (count >= max) break;

            target.put(entry.getKey(), entry.getValue());
            count++;
        }
        return target;
    }

    //get groups
    public List<Map<String,Object>> getRecommendGroups(long id_user) {



        List<Map<String,Object>> tagsOfUser = tagDAO.getTagsForUserWithHidden(id_user);

        List<List<Map<String ,Object>>> listOfListofEvents  = new ArrayList<>();

        for (Map<String,Object> tagmap : tagsOfUser)
        {
            listOfListofEvents.add(tagDAO.getTaggedFromGroupsNotMembership(tagmap.get("tag").toString(),id_user));
        }

        Map<Long,Integer> weightList = new HashMap<>();

        for(List<Map<String,Object>> list : listOfListofEvents)
            for(Map<String,Object> map : list)
            {
                Long id_event = Long.parseLong(map.get("id").toString());
                if ( weightList.get(id_event) != null){
                    Integer weight = weightList.get(id_event);
                    weight++;
                    weightList.put(id_event,weight);
                }else
                {
                    weightList.put(id_event,1);
                }
            }
        weightList = sortByComparator(weightList);
        weightList = putFirstEntries(5, weightList);
        List<Map<String,Object>> groups = new ArrayList<>();
        for(Long id :  weightList.keySet())
        {
            groups.add(groupDAO.getGroup(id));
        }

        return groups;
    }

    //recommended events
    public List<Map<String,Object>> getRecommendEvents(long id_user) {


        List<Map<String,Object>> tagsOfUser = tagDAO.getTagsForUserWithHidden(id_user);

        List<List<Map<String ,Object>>> listOfListofEvents  = new ArrayList<>();

        for (Map<String,Object> tagmap : tagsOfUser)
        {
            listOfListofEvents.add(tagDAO.getTaggedFromEventsNotJoinedUpdate(tagmap.get("tag").toString(),id_user));
        }

        Map<Long,Integer> weightList = new HashMap<>();

        for(List<Map<String,Object>> list : listOfListofEvents)
            for(Map<String,Object> map : list)
            {
                Long id_event = Long.parseLong(map.get("id").toString());
                if ( weightList.get(id_event) != null){

                    Integer weight = weightList.get(id_event);
                    weight++;
                    weightList.put(id_event,weight);

                }else
                {
                    weightList.put(id_event,1);

                }

            }
        weightList = sortByComparator(weightList);
        weightList = putFirstEntries(5, weightList);
        List<Map<String,Object>> events = new ArrayList<>();
        for(Long id :  weightList.keySet())
        {
            events.add(eventModel.getEvent(id));
        }

        return events;
    }



}
