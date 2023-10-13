package hadar.alpha_gly.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idchatroom")
    private Long IdChatRoom;
    private String nameChat;
    private boolean visibility;
    private Boolean isActive;
    //Relation-->Message
    @OneToMany(mappedBy = "chatRoom")
    @JsonManagedReference
    List<Message> messages;
    //Relation-->User

}
