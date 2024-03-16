package Users;

public class User {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String phoneNumber;
    private final String role;

    public User(int id, String firstName, String lastName, String username, String email, String phoneNumber, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
}