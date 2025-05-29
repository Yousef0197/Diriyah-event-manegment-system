public class User {
    private String type;
    private String username;
    private String password;
    private String gender;
    private int age;

    public User(String type, String username, String password, String gender, int age) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public String getType() { return type; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
}
