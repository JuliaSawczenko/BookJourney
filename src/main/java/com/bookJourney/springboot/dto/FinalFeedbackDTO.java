package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.EnumMood;

import java.util.HashMap;

public record FinalFeedbackDTO(ReviewDTO review,
                              HashMap<EnumMood, Integer> moods){
}
