package platform.vv.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
public class VVCode {
    //mark id as primary key
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private String id;

    @Column
    private String content;

    @Column
    private Integer time;

    @Column
    private Integer views;

    @Column
    private LocalDateTime date;

}

