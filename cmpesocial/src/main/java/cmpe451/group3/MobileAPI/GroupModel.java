package cmpe451.group3.MobileAPI;

import java.util.Map;

/**
 * Created by umut on 11/24/15.
 */
public class GroupModel {
    public int id;
    public String name;
    public String date_of_creation;
    public int id_admin;
    public int type;
    public String description;
    public String group_url;

    public GroupModel MapGroup (Map<String, Object> groupMap)
    {
        GroupModel groupModel = new GroupModel();
        groupModel.id = Integer.parseInt(groupMap.get("id").toString());
        groupModel.name = groupMap.get("name").toString();
        groupModel.date_of_creation = groupMap.get("date_of_creation").toString();
        groupModel.id_admin =Integer.parseInt(groupMap.get("id_admin").toString());
        groupModel.type =Integer.parseInt(groupMap.get("type").toString());
        groupModel.description = groupMap.get("description").toString();
        groupModel.group_url = groupMap.get("group_url").toString();
        return  groupModel;
    }

}
