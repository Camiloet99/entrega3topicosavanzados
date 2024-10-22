package com.ensayo3.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RegisterUserResponse {

    private String userID;
    private String username;
    private List<BookResponse> books;

}

