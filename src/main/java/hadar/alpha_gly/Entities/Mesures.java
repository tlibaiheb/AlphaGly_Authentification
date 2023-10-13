package hadar.alpha_gly.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Mesures implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    //date named captured
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCaptured;

    @Enumerated(EnumType.STRING)
    private stateEnum state;

    private float glucoseLevel;

    @ManyToOne(cascade = CascadeType.ALL)
    private KitDevices kitDevices;
}