package com.demo.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Proxy(lazy = false)
public class OrderVo {
    private int orderID;

    private String userID;

    private int venueID;

    private String venueName;

    /**
     * 1未审核 2已审核 3已完成 4失效
     */
    private int state;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    private int hours;

    private int total;
}
