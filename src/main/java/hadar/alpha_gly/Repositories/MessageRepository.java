package hadar.alpha_gly.Repositories;

import hadar.alpha_gly.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {


    //count messages by chatroom
    @Query("SELECT COUNT(m) FROM Message m WHERE m.chatRoom.IdChatRoom = :id and m.seen = false ")
    public int countMessagesByChatRoom(@Param("id") Long id);


    @Query(value="SELECT message.text,user.first_name,user.last_name  FROM chat_room,user_sent_list,user,message\n" +
            "where chat_room.idchatroom=:id and message.chat_room_idchatroom=chat_room.idchatroom and user.id=user_sent_list.user_id\n" +
            "and user_sent_list.sent_list_idmsg=message.idmsg ",nativeQuery = true)
    public List<Object[]> ListsentMessagesByChatRoom(@Param("id") Long id);

    @Query(value="SELECT text,email FROM user_received_list,user,message\n" +
            "where user.id=user_received_list.user_id\n" +
            "and user_received_list.received_list_idmsg=message.idmsg ",nativeQuery = true)
    public List<Object[]> ListreceivedMessagesByChatRoom();

    //sort messages by date in chatroom
    @Query(value="SELECT message.text,CONCAT(user.first_name, ' ', user.last_name) AS full_name \n" +
            "FROM message\n" +
            "JOIN chat_room ON message.chat_room_idchatroom = chat_room.idchatroom\n" +
            "JOIN user_sent_list ON message.idmsg = user_sent_list.sent_list_idmsg\n" +
            "JOIN user ON user_sent_list.user_id = user.id\n" +
            "WHERE chat_room.idchatroom = ?1\n" +
            "ORDER BY message.sent ASC",nativeQuery = true)
    public List<Object[]> ListMessagesByChatRoom(@Param("id") Long id);

}
