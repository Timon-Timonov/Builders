package it.academy.pojo;

import it.academy.pojo.enums.PaymentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "calculations")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date month;

    @ManyToOne
    private Chapter chapter;

    @Column
    private Integer sum;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp calculationDate;

    @Column(nullable = false)
    private PaymentType type;
}
