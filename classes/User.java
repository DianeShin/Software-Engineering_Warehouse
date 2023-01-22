package classes;

public class User {
    private String email;
    private String password;
    
    // Constructor
    public User(String email, String password){
        this.email=email;
        this.password=password;
    }

    // get email
    public String getEmail(){
        return email;
    }

    @Override
    public String toString() {
        return email;
    }

    // authenticate password
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}