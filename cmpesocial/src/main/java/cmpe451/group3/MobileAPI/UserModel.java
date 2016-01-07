package cmpe451.group3.MobileAPI;


import org.springframework.security.core.userdetails.User;

import java.util.Map;

/**
 * <h2>User Model</h2>
 * @author Umut Afacan
 * 
 * @param id,name,surname,password,email,name,type
 */
public class UserModel {

    public long id;
    public String surname;
    public String password;
    public String email;
    public String name;
    public String type;


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

    public  String getName() {
        return name;
    }

    public  void setName(String name){
        this.name = name;
    }

    public UserModel MapModelFromDAO (Map<String, Object> UserMap)
    {
        UserModel userModel = new UserModel();
        userModel.id = Long.parseLong(UserMap.get("id").toString());
        userModel.name = UserMap.get("name").toString();
        userModel.email = UserMap.get("email").toString();
        userModel.password = UserMap.get("password").toString();
        userModel.surname = UserMap.get("surname").toString();
        userModel.type = UserMap.get("type").toString();

        return  userModel;
    }



}