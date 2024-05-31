package it.academy.pojo;

import it.academy.pojo.enums.ChapterStatus;
import it.academy.pojo.legalEntities.Contractor;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chapters")
public class Chapter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    @Builder.Default
    private ChapterStatus status = ChapterStatus.FREE;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;
}
