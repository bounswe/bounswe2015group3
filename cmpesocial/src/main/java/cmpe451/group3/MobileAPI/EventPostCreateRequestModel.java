package cmpe451.group3.MobileAPI;

/**
 * <h2>Request Model for Create Post in Events</h2>
 * @author Umut Afacan
 * @param id_event
 * @param id_user
 * @param content
 * @param content_url
 *
 */
public class EventPostCreateRequestModel {
    public Long id_event;
    public Long id_user;
    public String content;
    public String content_url;
}
