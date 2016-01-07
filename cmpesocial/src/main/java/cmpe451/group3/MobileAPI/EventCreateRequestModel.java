package cmpe451.group3.MobileAPI;

import java.util.Locale;

/**
 * <h2> Request Model for Event Creation</h2>
 * @author Umut Afacan
 */
public class EventCreateRequestModel {

    public String name;
    public String date;
    public String end_date;
    public int periodic;
    public Long id_user;
    public String location;
    public String description;
    public String type;
    public String url;
    public long id_group;


}
