package cmpe451.group3.MobileAPI;

import java.util.Locale;
import java.util.Map;

/**
 * <h2>Event Model for API </h2>
 * @author Umut Afacan
 */
public class EventBaseModel {

    public Long id;
    public String name;
    public String date;
    public String end_date;
    public int periodic;
    public String date_of_creation;
    public Long id_user;
    public String location;
    public String description;
    public String type;
    public Long id_group;
    public String url;

/**
 * Event Base Model
 * @param event
 * @return
 */
    public EventBaseModel mapModel(Map<String, Object> event)
    {
        EventBaseModel eventModel = new EventBaseModel();

        eventModel.id = Long.parseLong(event.get("id").toString());
        eventModel.name = event.get("name").toString();
        eventModel.date = event.get("date").toString();
        eventModel.end_date = event.get("end_date").toString();
        eventModel.periodic = Integer.parseInt( event.get("periodic").toString() );
        eventModel.date_of_creation = event.get("date_of_creation").toString();
        eventModel.id_user = Long.parseLong(event.get("id_user").toString());
        eventModel.location = event.get("location").toString();
        eventModel.description = event.get("description").toString();
        eventModel.type = event.get("type").toString();
        eventModel.id_group = Long.parseLong(event.get("id_group").toString());
        eventModel.url = event.get("url").toString();
        return eventModel;

    }

}
