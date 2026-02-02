package Bean;

public class CredenzialiBean {
    private String email;
    private String password;

    public CredenzialiBean(String email, String password){
        this.email = email;
        this.password = password;
    }

    public CredenzialiBean(String email){
        this.email = email;
        this.password = null;
    }


    public void setEmail(String Email) {

        this.email = Email;
    }
    public void setPassword(String Password){
        this.password = Password;
    }

    public String getEmail(){
        return  email;
    }
    public String getPassword(){
        return password;
    }
}

