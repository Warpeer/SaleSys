package com.example.salesys.controller;

import com.example.salesys.models.Employee;
import com.example.salesys.models.PersonalSubscribtion;
import com.example.salesys.models.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.salesys.repository.SubscribtionRepository;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@RestController
public class visitorController {

    @Autowired
    private SubscribtionRepository repo;

    @Autowired
    private HttpSession session;

    private Logger logger = LoggerFactory.getLogger(visitorController.class);

    @GetMapping("/authenticate")
    public boolean authenticate(){
        if((session.getAttribute("loggedIn"))==null){
            session.setAttribute("loggedIn", false);
        }
        return (boolean) session.getAttribute("loggedIn");
    }
    @GetMapping("/adminCheck")
    public boolean authenticateAdmin(){
        if((session.getAttribute("loggedInAdmin"))==null){
            session.setAttribute("loggedInAdmin", false);
        }
        return (boolean) session.getAttribute("loggedInAdmin");
    }


    @GetMapping("/fetchEveryVisitor")
    public List<PersonalSubscribtion> fetchAllVisitors(HttpServletResponse response) throws IOException {
        List<PersonalSubscribtion> allVisitors = repo.fetchEveryVisitor();
        if(authenticate()){
            if(allVisitors==null){
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL DATABASE ERROR.");
            }
            return allVisitors;
        }else{
            response.sendError(HttpStatus.FORBIDDEN.value(), "You're not logged in!");
            return null;
        }
    }
    @PostMapping("/saveVisitor")
    public boolean saveVisitor(PersonalSubscribtion visitor, HttpServletResponse response) throws IOException {
        if(validateVisitor(visitor)){
            if(!repo.saveVisitor(visitor)){
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error.");
                return false;
            }
            return true;
        }else{
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Validation error");
            return false;
        }
    }
    @GetMapping("/deleteOneVisitor")
    public boolean deleteOneVisitor(int id, HttpServletResponse response) throws IOException{
        if(authenticateAdmin()){
            if(!repo.deleteOneVisitor(id)){
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error.");
                return false;
            }
            return true;
        }
        return false;
    }
    @GetMapping("/logIn")
    public boolean logIn(String email, String password){
        if(repo.logIn(email, password)){
            session.setAttribute("loggedIn", true);
            if(repo.checkForAdmin(email)){
                session.setAttribute("loggedInAdmin", true);
            }
            return true;
        }else return false;
    }
    @GetMapping("/logOut")
    public void logOut(){
        session.setAttribute("loggedIn", false);
        session.setAttribute("loggedInAdmin", false);
    }
//    @GetMapping("/logInAdmin")
//    public boolean logInAdmin(String email, String password){
//        if(repo.logInAdmin(email, password)){
//            session.setAttribute("loggedInAdmin", true);
//            return true;
//        }
//        return false;
//    }
    @GetMapping("/encryptFirstPassword")
    public boolean encryptFirstPassword(){
        logOut();
        return repo.encryptFirstPassword();
    }
    @GetMapping("/fetchProviders")
    public List<Provider> fetchProviders(){
        return repo.fetchProviders();
    }
    //chart mappings
    @GetMapping("/fetchProviderSet")
    public List<Integer> fetchProviderSet(){
        if(authenticate()){
            return repo.fetchProviderSet();
        }return null;
    }
    @GetMapping("/searchInVisitors")
    public List<PersonalSubscribtion> searchInVisitors(String keyword, String target, HttpServletResponse response) throws IOException{
        if(authenticate()){
//            if(validateTarget(target)){
//                return repo.search(keyword, target);
//            }else response.sendError(HttpStatus.FORBIDDEN.value(), "Kan bare søke i: \"id\", \"fornavn\", \"etternavn\", \"mobilnummer\", \"operatør\", \"pris\", \"gb\"");
            return repo.search(keyword, target);
        }
        return null;
    }

    private boolean validateTarget(String target){
        boolean flag = true;
        String[] targetList = {"id", "fornavn", "etternavn", "mobilnummer", "isp", "pris", "gb"};
        for(String ct : targetList){
            if (!ct.equals(target.toLowerCase())) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    private boolean validateVisitor(PersonalSubscribtion visitor){
        String regexName = "[a-zA-ZæøåÆØÅ. \\-]{2,40}";
        String regexPhoneNumber = "[0-9]{8}";
        String regexProvider = "[a-zA-ZæøåÆØÅ0-9. \\-]{2,40}";
        boolean first_nameOk = visitor.getFirst_name().matches(regexName);
        boolean last_nameOk = visitor.getLast_name().matches(regexName);
        boolean phone_numberOk = visitor.getPhone_number().matches(regexPhoneNumber);
        boolean providerOk = visitor.getProvider().matches(regexProvider);
        if (first_nameOk && last_nameOk && phone_numberOk && providerOk){
            return true;
        }
        logger.error("Validation error");
        return false;
    }

}
