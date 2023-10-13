package hadar.alpha_gly.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KitDevices implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    private String name;
    private String model;
    private String version;
    private String serialNumber;
    private Boolean sold;
    private String country;

    //onetomany with mesures
    @OneToMany(mappedBy = "kitDevices", cascade = CascadeType.ALL)
    private List<Mesures> mesures;
}
