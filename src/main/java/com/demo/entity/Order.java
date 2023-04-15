package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="`order`")
@Proxy(lazy = false)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private  int id;
    private int orderID;

    private String userID;

    private int venueID;

    /**
     * 1未审核 2已审核 3已完成 4失效
     */
    @Column(name="state")
    private int state;

    @Column(name="order_time")
    private LocalDateTime orderTime;

    @Column(name="start_time")
    private LocalDateTime startTime;

    private int hours;

    private int total;

}
