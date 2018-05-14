package pl.coddlers.models.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String title;

    private String description;

    @Column(nullable=false)
    private Date startDate;

    @Column(nullable=false)
    private Date endDate;


}
