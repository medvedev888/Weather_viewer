package me.vladislav.weather_viewer.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

@Entity
@Table(name = "locations", indexes = {
        @Index(name = "locations_id", columnList = "id", unique = true),
        @Index(name = "locations_name", columnList = "name"),
        @Index(name = "locations_user_id", columnList = "user_id"),
        @Index(name = "locations_name_user_id", columnList = "name, user_id", unique = true)
})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;
    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    public Location(String name, User user, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
