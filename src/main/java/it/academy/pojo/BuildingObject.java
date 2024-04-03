package it.academy.pojo;

import it.academy.pojo.enums.ObjectStatus;
import it.academy.pojo.legalEntities.Developer;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "chapters")
@ToString(exclude = "chapters")
@Entity
@Table(name = "building_objects")
public class BuildingObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Builder.Default
    private ObjectStatus status = ObjectStatus.PREPARATION;

    @ManyToOne
    private Developer developer;

    @Embedded
    private Address address;

    @Builder.Default
    @OneToMany(mappedBy = "buildingObject")
    private Set<Chapter> chapters = new HashSet<>();

    public void addChapter(Chapter chapter) {

        if (chapter != null) {
            chapter.setBuildingObject(this);
            this.chapters.add(chapter);
        }
    }
}
