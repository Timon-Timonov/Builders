package it.academy.pojo;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date month;

    @Column(name = "work_price_plan")
    private Integer workPricePlan;

    @Builder.Default
    @Column(name = "work_price_fact")
    private Integer workPriceFact = ZERO_INT_VALUE;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
}
