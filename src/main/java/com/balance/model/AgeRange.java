package com.balance.model;

import javax.persistence.*;

/**
 * Created by KEVIN on 17/06/2017.
 */
@Entity
@Table(name = "age_range")
public class AgeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="age_range_id")
    private int id;

    @Column(name = "age")
    private int age;

    @Column(name = "meta_start")
    private int metaStart;

    @Column(name = "meta_finish")
    private int metaFinish;

    @Column(name = "maximum")
    private int maximum;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMetaStart() {
        return metaStart;
    }

    public void setMetaStart(int metaStart) {
        this.metaStart = metaStart;
    }

    public int getMetaFinish() {
        return metaFinish;
    }

    public void setMetaFinish(int metaFinish) {
        this.metaFinish = metaFinish;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}
