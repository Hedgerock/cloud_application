package com.hedgerock.app_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="t_users", schema = "develop_schema")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = { "files", "authorities" })
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name="disk_space")
    private Long diskSpace;

    @Column(name="used_space")
    private Long usedSpace;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FileEntity> files;

    @ManyToMany
    @JoinTable(
            schema = "develop_schema",
            name = "t_user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<AuthoritiesEntity> authorities;
}
