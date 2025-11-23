package com.uts.shopper.Controllers;


import android.util.Log;
import android.widget.Toast;

public class MainController {

}
/*
import com.uts.app.Models.User;
import com.uts.app.helpers.Fetch;

import java.util.function.Consumer;

public class MainController {

    private static class RequestLogin{
        String email;
        String password;
        public RequestLogin(String email, String password){
            this.email = email;
            this.password = password;
        }
    }

    private static class ResponseLogin{
        String success;
        String token;
        User user;
    }
    public static class ResultTypes {
        final public static String admin = "admin";
        final public static String employee = "employee";
        final public static String notLogin = "notLogin";
        final public static String error = "error";
    };

    public static void Login(String email, String password, Consumer<String> result, Consumer<String> errorApp){
        Fetch.POST(new RequestLogin(email, password), "/api/session.php?action=login", (apiResult)->{
            try {
                result.accept(apiResult.user.rol);
                Fetch.setAuthToken(apiResult.token);
            } catch (Exception e) {
                errorApp.accept("Usuario o contraseña invalidos");
            }
        }, ResponseLogin.class);
    }
}
*/