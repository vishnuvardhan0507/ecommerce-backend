package com.example.ecommerce.dto;

import com.example.ecommerce.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;
  //  private Role role;
}