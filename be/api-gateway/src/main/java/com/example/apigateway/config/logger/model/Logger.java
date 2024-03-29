package com.example.apigateway.config.logger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
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
    @Lob
    private String requestData;
    @Column
    @Lob
    private String responseData;
    @Column
    @Lob
    private String headerRequest;
    @Column
    @Lob
    private String headerResponse;
    @Column
    private int httpResult;
    @Column
    private String requestIP;
    @Column
    private String parameter;
    @Column
    private String processTime;

    @DateTimeFormat(pattern = "dd-mmm-yyyy hh:mm:ss.s")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


}
