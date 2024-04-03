package it.academy.pojo;

import it.academy.pojo.enums.Roles;
import it.academy.pojo.enums.UserStatus;
import it.academy.pojo.legalEntities.LegalEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Roles role;

    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.AKTIVE;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private LegalEntity legalEntity;
}
