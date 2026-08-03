package fedoseev.tasks.system.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotNull
    @Column(name = "password_hash",nullable = false)
    private String passwordHash;

    @NotNull
    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


}
