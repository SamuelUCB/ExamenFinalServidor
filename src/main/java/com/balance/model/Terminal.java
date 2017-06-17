package com.balance.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by KEVIN on 26/05/2017.
 */
@Entity
@Table(name = "terminal")
public class Terminal {
    @Id
    @Column(name="terminal_id")
    private int serial;

    @Column(name="active")
    private boolean active=false;

    @OneToOne(mappedBy = "terminal")
    private User user;

    @ManyToOne()
    @JoinColumn(name="band_model_id")
    private BandModel bandModel;

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BandModel getBandModel() {
        return bandModel;
    }

    public void setBandModel(BandModel bandModel) {
        this.bandModel = bandModel;
    }
}
