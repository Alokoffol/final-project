package com.example.models.unit;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean active;
    private LocalDateTime createdAt; //дата создания
    private LocalDateTime updatedAt; // дата обновления

    public User() {}

    public User(String email, String password, String firstName, String lastName, String phone, boolean active) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.active = active;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public boolean isActive() {return active;}
    public void setActive(boolean active) {this.active = active;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(LocalDateTime updateAt) {this.updatedAt = updateAt;}

    // Выводит false или true, если user есть или нет в системе
    public boolean isNew() {
        return getId() == 0;
    }

    // Конкатенация имени и фамилии для вывода
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    // Устанавливает текущее время на время обновления
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", updateAt=" + updatedAt +
                '}';
    }
}
