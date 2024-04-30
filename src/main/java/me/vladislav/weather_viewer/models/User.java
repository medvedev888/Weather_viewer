package me.vladislav.weather_viewer.models;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    @Column(name = "password", nullable = false)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}