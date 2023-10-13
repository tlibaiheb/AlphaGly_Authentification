package hadar.alpha_gly.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idmsg")
    private Long IdMsg;
    private String text;
    private boolean visibility;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent ;
    private boolean seen;
    //Relation->ChaTRoom
    @ManyToOne
    @JsonBackReference
    ChatRoom chatRoom;


}
