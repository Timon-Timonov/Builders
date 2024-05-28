package it.academy.pojo;


import it.academy.pojo.enums.PaymentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "money_transfer")
public class MoneyTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private int sum = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp calculationDate;

    @Column(nullable = false)
    private PaymentType type;

    @ManyToOne
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;
}
