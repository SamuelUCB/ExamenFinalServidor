package com.balance.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by da_20 on 29/5/2017.
 */
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private Integer id;

    private Boolean active=true;

    private Date creation_date;

    private Date expired_date;

    private String token;

    @OneToMany(mappedBy = "tokenId", cascade = CascadeType.ALL)
    private Set<User> users;

    private Integer user_creator_id;

    public Integer getUser_creator_id() {
        return user_creator_id;
    }

    public void setUser_creator_id(Integer user_creator_id) {
        this.user_creator_id = user_creator_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public Token() {
    }

    public Token(String tokenString) {
        this.token = tokenString;
        creation_date=new Date();
        expired_date=new Date();
        expired_date.setDate(expired_date.getDate()+1);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getExpired_date() {
        return expired_date;
    }

    public void setExpired_date(Date expired_date) {
        this.expired_date = expired_date;
    }

    public String getTokenString() {
        return token;
    }

    public void setTokenString(String tokenString) {
        this.token = tokenString;
    }
}