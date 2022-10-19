package com.example.salesys.repository;

import com.example.salesys.models.Employee;
import com.example.salesys.models.PersonalSubscribtion;
import com.example.salesys.models.Provider;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SubscribtionRepository {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JdbcTemplate db;

    private Logger logger = LoggerFactory.getLogger(SubscribtionRepository.class);

    public boolean saveVisitor(PersonalSubscribtion visitor){
        String sql = "INSERT INTO PersonalSubscribtions (first_name, last_name, phone_number, provider, price, dataAmount) VALUES(?,?,?,?,?,?)";
        try{
            db.update(sql, visitor.getFirst_name(), visitor.getLast_name(), visitor.getPhone_number(), visitor.getProvider(), visitor.getPrice(), visitor.getDataAmount());
            return true;
        }catch(Exception e){
            logger.error("Error in saveVisitor : " + e);
            return false;
        }
    }

    public List<PersonalSubscribtion> fetchEveryVisitor(){
        String sql = "SELECT * FROM PersonalSubscribtions";
        try{
            return db.query(sql, new BeanPropertyRowMapper<>(PersonalSubscribtion.class));
        }catch (Exception e){
            logger.error("Error in fetchEveryVisitor : " + e);
            return null;
        }
    }

    public boolean deleteOneVisitor(int id){
        String sql = "DELETE FROM PersonalSubscribtions WHERE id=?";
        try{
            db.update(sql, id);
            return true;
        }catch(Exception e){
            logger.error("Error in deleOneVisitor : " + e);
            return false;
        }
    }

    public List<Employee> fetchEveryEmployee(){
        String sql = "SELECT * FROM employees";
        try{
            return db.query(sql, new BeanPropertyRowMapper<>(Employee.class));
        }catch(Exception e){
            logger.error("Error in fetchEveryEmployee : " + e);
            return null;
        }
    }

    private String encryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(15));
    }
    private boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

    public List<Provider> fetchProviders(){
        String sql = "SELECT name FROM providers";
        try{
            return db.query(sql, new BeanPropertyRowMapper<>(Provider.class));
        }catch (Exception e){
            logger.error("Error in getProviders : " + e);
            return null;
        }
    }


    public boolean checkForAdmin(String email){
        String sql = "SELECT id FROM employees WHERE email = ?";
        String adminCheck = "SELECT id FROM admins WHERE employeeID = ?";
        int adminID = 0;
        try{
            int employeeID = db.queryForObject(sql, Integer.class, email);
            adminID = db.queryForObject(adminCheck, Integer.class, employeeID);
            return adminID != 0;
        }catch (Exception e){
            return false;
        }
    }

    public List<PersonalSubscribtion> search(String keyword, String target){
        String sql = "";
        switch (target.toLowerCase()){
            case("id"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE id=?";
                break;
            case("fornavn"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE first_name=?";
                break;
            case("etternavn"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE last_name=?";
                break;
            case("mobilnummer"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE phone_number=?";
                break;
            case("isp"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE provider=?";
                break;
            case("pris"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE price=?";
                break;
            case("gb"):
                sql = "SELECT * FROM PersonalSubscribtions WHERE dataAmount=?";
                break;
        }
        try{
            return db.query(sql, new BeanPropertyRowMapper<>(PersonalSubscribtion.class), keyword);
        }catch(Exception e){
            logger.error("Error in search : " + e);
            return null;
        }
    }

    public boolean logIn(String email, String password){
        String sql = "SELECT * FROM employees WHERE email = ?";
        try{
            List<Employee> employees = db.query(sql, new BeanPropertyRowMapper(Employee.class), email);
            if(employees!=null){
                if(checkPassword(password, employees.get(0).getPassword())){
                    return true;
                }
            }
            return false;
        }catch(Exception e){
            logger.error("Error in logIn : " + e);
            return false;
        }

    }

    int factor=0;
    public boolean encryptFirstPassword(){
        if(factor==0){
            factor++;
            String sqlEmployee ="SELECT * FROM employees";

            String encryptedPassword = "";
            try{
                List<Employee> employees = db.query(sqlEmployee, new BeanPropertyRowMapper(Employee.class));

                for(Employee e : employees){


                    encryptedPassword = encryptPassword(e.getPassword());
                    sqlEmployee = "UPDATE employees SET password=? WHERE id=?";
                    db.update(sqlEmployee, encryptedPassword, e.getId());
                }
                return true;
            }catch(Exception e){
                logger.error("Error in updateFirstPassword" + e);
                return false;
            }
        }return false;
    }

    //chart functions

    public List<Integer> fetchProviderSet(){
        List<Integer> amountList = new ArrayList<>();
        List<Provider> providerList = fetchProviders();
        String sqlProvider = "SELECT count(*) FROM PersonalSubscribtions WHERE provider = ?";
        try {
            for (Provider currentProvider : providerList) {
                int amount = db.queryForObject(sqlProvider, Integer.class, currentProvider.getName().toUpperCase());
                amountList.add(amount);
            }
        } catch (Exception e) {
            logger.error("Error in amountInDB : " + e);
            return null;
        }
        return amountList;
    }

}
