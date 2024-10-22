package com.ensayo3.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

    String userName;
    String password;


}
