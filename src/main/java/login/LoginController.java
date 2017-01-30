package login;

import login.LoginDTO;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class LoginController {

    @RequestMapping("/login/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value="/login/do",method=RequestMethod.PUT, consumes="application/json")
public void register(@RequestBody LoginDTO loginElement)
{

System.out.println("Name : "+loginElement.getName()+" Password : "+loginElement.getPassword());
}

}


