package ca.uoit.csci4100.samples.databasedemo;

public class Contact {
    private long id = -1L;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private String phone = null;

    public Contact(long id,
                   String firstName,
                   String lastName,
                   String email,
                   String phone) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPhone(phone);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
