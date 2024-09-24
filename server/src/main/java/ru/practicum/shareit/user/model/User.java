package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name", "email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "USERS", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "NAME", nullable = false)
    String name;
    @Email
    @Column(name = "EMAIL", nullable = false, unique = true)
    String email;
    @Column(name = "LOGIN", nullable = false, unique = true)
    String login;
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BIRTHDAY", nullable = false)
    LocalDate birthday;
}
