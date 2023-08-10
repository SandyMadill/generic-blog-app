package com.generic.blog.app.user;

import com.generic.blog.app.auth.EnumRole;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "\"User\"",
        uniqueConstraints=@UniqueConstraint(columnNames = {"username"})
)
public class UserEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name="display_name", nullable = false)
    private String displayName;

    @Column(name="role", nullable = false)
    private EnumRole role;

    @Column(name = "active", nullable = false)
    private Boolean active;
}
