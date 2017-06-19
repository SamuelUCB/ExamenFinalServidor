package com.balance.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

/**
 * Created by da_20 on 31/5/2017.
 */
@Entity
public class Band {
    private Long id;

    private Integer steps;
    private Integer bpm;
    private Long distance;
    private Date fecha_registro;
    private Float latitude;
    private Float longitude;
    private Integer calories;
    private Integer user;
    private Date fecha_evento;
    private String intensidad;

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Date getFecha_registro() {
        if (fecha_registro == null) {
            return new Date();
        }
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        if (fecha_registro != null) {
            this.fecha_registro = fecha_registro;
        }
    }

    public Date getFecha_evento() {
        return fecha_evento;
    }

    public void setFecha_evento(Date fecha_evento) {
        this.fecha_evento = fecha_evento;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public void asignar(Long id, Integer steps, Integer bpm, Long distance, Date fecha_registro, Float latitude, Float longitude, Integer calories, Integer user, Date fecha_evento, String intensidad) {
        this.id = id;
        this.steps = steps;
        this.bpm = bpm;
        this.distance = distance;
        this.fecha_registro = fecha_registro;
        this.latitude = latitude;
        this.longitude = longitude;
        this.calories = calories;
        this.user = user;
        this.fecha_evento = fecha_evento;
        this.intensidad = intensidad;
    }
}
