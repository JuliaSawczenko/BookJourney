package com.bookJourney.springboot.dto;

import java.util.List;

public record UserInfoResponse (Integer id,
                                String username,
                                String email,
                                List<String> roles) {
}
