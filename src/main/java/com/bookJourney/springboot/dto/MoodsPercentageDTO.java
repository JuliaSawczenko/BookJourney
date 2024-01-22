package com.bookJourney.springboot.dto;

import com.bookJourney.springboot.entity.EnumMood;

import java.util.Map;

public record MoodsPercentageDTO(Map<EnumMood, Double> moodsPercentages) {
}
