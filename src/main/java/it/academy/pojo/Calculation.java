package it.academy.pojo;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "transferSet")
@ToString(exclude = "transferSet")
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

    @Column(name = "work_price_fact")
    private Integer workPriceFact;


    @ManyToOne
    private Chapter chapter;

    @Builder.Default
    @OneToMany(mappedBy = "calculation", fetch = FetchType.EAGER)
    private Set<MoneyTransfer> transferSet = new HashSet<>();
}
