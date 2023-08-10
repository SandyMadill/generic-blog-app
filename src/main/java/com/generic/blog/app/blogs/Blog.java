package com.generic.blog.app.blogs;


import com.generic.blog.app.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name="Blog"
)
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @Column(name="title")
    private String title;

    @Column(name="blog_text", length=15000)
    private String blogText;

    @Column(name = "posted_at")
    @CreationTimestamp
    private Date postedAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}
