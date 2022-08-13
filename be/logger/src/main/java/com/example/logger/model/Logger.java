package com.example.logger.model;

import feign.Request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Logger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String requestId;
    @Column
    private String uri;
    @Column
    private String method;
    @Column
    private String requestData;
    @Column
    private String responseData;
    @Column(columnDefinition = "text")
    private String headerRequest;
    @Column(columnDefinition = "text")
    private String headerResponse;
    @Column
    private int httpResult;
    @Column
    private String requestIP;
    @Column
    private String parameter;
    @Column
    private String processTime;
}
