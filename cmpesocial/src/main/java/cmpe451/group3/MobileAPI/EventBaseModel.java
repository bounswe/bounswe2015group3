package cmpe451.group3.MobileAPI;

import java.util.Locale;
import java.util.Map;

/**
 * Created by umut on 11/3/15.
 */
public class EventBaseModel {

    public Long id;
    public String name;
    public String date;
    public String date_of_creation;
    public Long userid;
    public String location;
    public String description;


    public EventBaseModel mapModel(Map<String, Object> event)
    {
        EventBaseModel eventModel = new EventBaseModel();

        eventModel.id = Long.parseLong(event.get("id").toString());
        eventModel.name = event.get("name").toString();
        eventModel.date = event.get("date").toString();
        eventModel.date_of_creation = event.get("date_of_creation").toString();
        eventModel.userid = Long.parseLong(event.get("userid").toString());
        eventModel.location = event.get("location").toString();
        eventModel.description = event.get("description").toString();

        return eventModel;

    }

}
