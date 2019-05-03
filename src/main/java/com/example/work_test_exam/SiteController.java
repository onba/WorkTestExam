package com.example.work_test_exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SiteController {
    @Autowired PasswordEncoder passwordEncoder;

    @Autowired MyUserDetailsService myUserDetailsService;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String getLoginPage(@RequestParam(value="message", required=false) final String message, Model model){
        if (message!=null) model.addAttribute("message",message);
        return "login";
    }

    @RequestMapping(value="/home", method= RequestMethod.POST)
    public String getHomePage(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
        if (userDetails==null || !passwordEncoder.matches(password,userDetails.getPassword())){
            redirectAttributes.addAttribute("message","Not correct username or password");
            return "redirect:login";
        }
        else{
            //generate token und so weiter...
            System.out.println("LOGIN SUCCESFUL");
            return "home";
        }
    }
}
