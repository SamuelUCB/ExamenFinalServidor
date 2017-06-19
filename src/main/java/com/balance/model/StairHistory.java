package com.balance.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stair_history")
public class StairHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "stair_id")
    private int id;

    @Column(name="cantidad")
    private Integer cantidad;

    @Column(name="tipo")
    private Integer tipo;

    @Column(name="user_id")
    private Integer user;

    @Column(name="date")
    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}