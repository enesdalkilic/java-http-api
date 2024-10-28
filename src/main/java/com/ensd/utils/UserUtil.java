package com.ensd.utils;

import com.ensd.schemaEnums.User;

public class UserUtil {
    public void validateUserSchema() {
        User[] enums = User.values();
        for (User userType : User.values()) {
           System.out.println(userType);
        }
    }
}
