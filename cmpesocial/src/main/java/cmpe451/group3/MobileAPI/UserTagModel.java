package cmpe451.group3.MobileAPI;

import java.util.Map;

/**
 * Created by Umut on 14.12.2015.
 */
public class UserTagModel {
    public long id_user;
    public String tag;


    public UserTagModel mapToObject(Map<String,Object> map)
    {
        UserTagModel tagModel = new UserTagModel();
        tagModel.id_user = Long.parseLong(map.get("id_user").toString());
        tagModel.tag = map.get("tag").toString();

        return tagModel;
    }

}
