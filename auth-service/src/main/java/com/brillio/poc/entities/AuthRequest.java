package com.brillio.poc.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
}
