package cmpe451.group3.MobileAPI;

import java.util.Map;

/**
 * <h2>Base Model for Post in Event</h2>
 * @author Umut Afacan
 * @param id
 * @param id_event
 * @param id_user
 * @param content
 * @param content_url
 * 
 *
 */
public class EventPostBaseModel {
    public Long id;
    public Long id_event;
    public Long id_user;
    public String content;
    public String content_url;

    public EventPostBaseModel mapModel(Map<String, Object> post)
    {

        EventPostBaseModel postModel = new EventPostBaseModel();

        postModel.id  =Long.parseLong( post.get("id").toString());
        postModel.id_event =Long.parseLong( post.get("id_event").toString());
        postModel.id_user = Long.parseLong( post.get("id_user").toString());
        postModel.content = post.get("content").toString();
        postModel.content_url = post.get("content_url").toString();

        return postModel;

    }
}
