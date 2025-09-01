package com.hedgerock.app_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="t_files", schema = "develop_schema")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString(exclude = { "user", "parent"})
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "access_link")
    private String accessLink;

    @Column(name = "size")
    private Long size;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private FileEntity parent;
}
