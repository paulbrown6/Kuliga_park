package com.app.kuliga.data.entity;

import android.annotation.SuppressLint;
import android.util.Log;

import com.app.kuliga.data.api.entity.ResponseEntity;
import com.orm.SugarRecord;

import java.text.SimpleDateFormat;

public class User extends SugarRecord<User> {

    private static String TAG = "User";

    private String lastName;
    private String firstName;
    private String middleName;
    private String birthday;
    private String phone;
    private String gender;
    private String email;
    private String userId;
    private String userTokenId;
    private String cookie;

    public User() {}

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserTokenId() {
        return userTokenId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserTokenId(String userTokenId) {
        this.userTokenId = userTokenId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @SuppressLint("SimpleDateFormat")
    public static User createFromXML(ResponseEntity response) {
        String lastName = null, firstName = null, middleName = null, birthday = null,
                email = null, id = null, userTokenId = null, phone = null, gender = null;
        try {
            lastName = response.getaPacket().Value.get(1).Structure.getValue("last_name").optional.string;
            firstName = response.getaPacket().Value.get(1).Structure.getValue("first_name").optional.string;
            middleName = response.getaPacket().Value.get(1).Structure.getValue("middle_name").optional.string;
            birthday = new SimpleDateFormat("dd.MM.yyyy").
                    format(response.getaPacket().Value.get(1).Structure.getValue("birthdate").ADate.getDate());
            email = response.getaPacket().Value.get(1).Structure.getValue("email").optional.string;
            id = response.getaPacket().Value.get(1).Structure.getValue("id").optional._int + "";
            userTokenId = response.getaPacket().Value.get(1).Structure.getValue("userTokenID").optional.int64_t + "";
            phone = response.getaPacket().Value.get(1).Structure.getValue("phone").optional.string;
            gender = response.getaPacket().Value.get(1).Structure.getValue("gender").optional.string;
        } catch (Exception e) {
            Log.e(TAG, "createFromXML: " + e.getMessage());
        }
        User user = new User();
        if (lastName != null) user.setLastName(lastName);
        if (firstName != null) user.setFirstName(firstName);
        if (middleName != null) user.setMiddleName(middleName);
        if (birthday != null) user.setBirthday(birthday);
        if (phone != null) user.setPhone(phone);
        if (gender != null) user.setGender(gender);
        if (email != null) user.setEmail(email);
        if (id != null) user.setUserId(id);
        if (userTokenId != null) user.setUserTokenId(userTokenId);
        return user;
    }
}
