package com.generic.blog.app.auth.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthRequest {
    private String username;
    private String password;
}
