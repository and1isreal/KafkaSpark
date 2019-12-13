package ru.kafkaspark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "limits_per_hour")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "limit_name")
    private String name;

    @Column(name = "limit_value")
    private int value;

    @Column(name = "effective_date")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
}
