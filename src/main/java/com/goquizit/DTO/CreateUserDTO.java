package com.goquizit.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateUserDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @java.beans.ConstructorProperties({"email", "username", "password"})
    public CreateUserDTO(@NotBlank @Email String email, @NotBlank String username, @NotBlank @Size(min = 8) String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public CreateUserDTO() {
    }

    public @NotBlank @Email String getEmail() {
        return this.email;
    }

    public @NotBlank String getUsername() {
        return this.username;
    }

    public @NotBlank @Size(min = 8) String getPassword() {
        return this.password;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public void setPassword(@NotBlank @Size(min = 8) String password) {
        this.password = password;
    }
}
