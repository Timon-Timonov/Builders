package it.academy.pojo;


import it.academy.pojo.enums.PaymentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static it.academy.util.constants.Constants.ZERO_INT_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "money_transfer")
public class MoneyTransfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private int sum = ZERO_INT_VALUE;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp calculationDate;

    @Column(nullable = false)
    private PaymentType type;

    @ManyToOne
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;
}
