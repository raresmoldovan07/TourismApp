package ubb.tourism.data.access.entity;

public class User implements Entity<Integer> {

    private Integer userId;
    private String username;
    private String password;
    private String name;

    public User(Integer userId, String username, String password, String name) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return userId;
    }

    @Override
    public void setId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}