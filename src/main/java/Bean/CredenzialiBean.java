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


    public void setEmail(String email) {

        this.email = email;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail(){
        return  email;
    }
    public String getPassword(){
        return password;
    }
}

