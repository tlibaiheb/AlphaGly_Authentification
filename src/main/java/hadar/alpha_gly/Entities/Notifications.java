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
public class Notifications implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;
    private String from_user;
    private String to_user;
    private String title;
    private String content;
    //date annotation
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;

    @Enumerated(EnumType.STRING)
    private typeNotification typeNotification;
}