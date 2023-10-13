package hadar.alpha_gly.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long Id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    //relation Roles
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();
    private String email;
    private String password;
    //Anti Bot + PWD Recover
    private Long token;

    @Enumerated(EnumType.STRING)
    private genderEnum gender;
    private Long phoneNumber1;
    private Long phoneNumber2;

    private Long weight;
    private Long height;
    @OneToOne(cascade = CascadeType.ALL)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private bloodTypeEnum bloodType;

    private String picture_URL;
    private boolean enabled;
    @Temporal(TemporalType.DATE)
    private Date joined;
    //Relation Messages
    @ManyToMany()
    List<Message> SentList;
    @ManyToMany()
    List<Message> ReceivedList;
    //Relation PWD
    @ManyToMany
    List<ChatRoom> chatRooms;
}
