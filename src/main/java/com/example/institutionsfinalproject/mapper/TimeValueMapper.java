package com.example.institutionsfinalproject.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
@Component
public class TimeValueMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalTime asLocalTime(String time){
        if (time == null || time.isEmpty()) return null;
        try{
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (Exception e){
            System.err.println("Parsing Time Error LocalTime: " + time);
            return null;
        }
    }

    public String asString(LocalTime time){
        if (time == null) return null;
        return time.format(TIME_FORMATTER);
    }

    public LocalDate asLocalDate(String date){
        if (date == null || date.isEmpty()) return null;
        try{
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (Exception e){
            System.err.println("Parsing Time Error LocalTime: " + date);
            return null;
        }
    }

    public String asString(LocalDate date){
        if (date == null) return null;
        return date.format(DATE_FORMATTER);
    }
}
