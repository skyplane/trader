package main.java.skyplane.controller;

import com.google.appengine.repackaged.com.google.gson.stream.JsonWriter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.TimeZone;

@Controller
public class BaseController {

    protected void printSuccessStatus(HttpServletResponse response) {
        try {
            OutputStreamWriter os = new OutputStreamWriter(response.getOutputStream());
            JsonWriter jsonWriter = new JsonWriter(os);
            jsonWriter.beginObject();
            jsonWriter.name("success").value(0);
            jsonWriter.endObject();
            jsonWriter.close();
        } catch (IOException ioe) {

        }
    }

    @RequestMapping(value = {"/", "/welcome**"}, method = RequestMethod.GET)
    public ModelAndView welcomePage() {

        User user = null;
        try {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            ModelAndView model = new ModelAndView();
            model.setViewName("login");
            return model;
        }

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));

        String role = "";
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            role = grantedAuthority.getAuthority();
        }

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is welcome page!");

        if ("ROLE_ADMIN".equals(role)) {
            model.setViewName("admin");
        } else if ("ROLE_USER".equals(role)) {
            model.setViewName("user");
        }
        return model;

    }

    @RequestMapping("/logout")
    public ModelAndView showLoggedout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is protected page - Admin Page!");
        model.setViewName("admin");

        return model;

    }


    @RequestMapping(value = "/dba**", method = RequestMethod.GET)
    public ModelAndView dbaPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is protected page - Database Page!");
        model.setViewName("admin");

        return model;

    }


}