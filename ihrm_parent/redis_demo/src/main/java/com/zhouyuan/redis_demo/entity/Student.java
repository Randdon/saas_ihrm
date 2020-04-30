package com.zhouyuan.redis_demo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description: 学生实体类
 * @author: yuand
 * @create: 2020-04-29 17:40
 * @modifiedBy: yuand
 **/
@Entity
@Table(name = "student")
public class Student implements Serializable {

    private static final long serialVersionUID = 3040953374813499315L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "card_num")
    private String cardNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
