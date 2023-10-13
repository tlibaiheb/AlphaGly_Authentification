package hadar.alpha_gly.Repositories;

import hadar.alpha_gly.Entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    //search by name
    @Query("SELECT c FROM ChatRoom c where c.nameChat LIKE %:n%")
    public List<ChatRoom> retrieveAllChatroomsSortedByName(@Param("n") String name);

    //search name chat
//COUNT     CHATROOMs BY USER
    @Query(value = "SELECT user_id FROM user_chat_rooms r WHERE r.chat_rooms_idchatroom = ?1",
            nativeQuery = true)
    public List<Long> ListUserRelatedtoChatRoom(Long chatroomid);

    //SELECT sent,text,email,name_chat FROM `message` ,`user_received_list`,`chat_room`,`user_sent_list`,`user`,`user_chat_rooms` where message.idmessage=user_received_list.message_idmessage and user_received_list.chat_room_idchatroom=chat_room.idchatroom and message.idmessage=user_sent_list.message_idmessage and user_sent_list.user_id=user.iduser and user_chat_rooms.chat_rooms_idchatroom=chat_room.idchatroom and user_chat_rooms.user_id=1 and chat_room.idchatroom=1
    @Query(value = "SELECT sent,text,email,name_chat FROM `message` ,`user_received_list`,`chat_room`,`user_sent_list`,`user`,`user_chat_rooms` where message.idmessage=user_received_list.message_idmessage and user_received_list.chat_room_idchatroom=chat_room.idchatroom and message.idmessage=user_sent_list.message_idmessage and user_sent_list.user_id=user.iduser and user_chat_rooms.chat_rooms_idchatroom=chat_room.idchatroom and user_chat_rooms.user_id=?1 and chat_room.idchatroom=?2",
            nativeQuery = true)
    public List<Object[]> ListMessagesByChatRoom(Long userid,Long chatroomid);

    @Query(value="SELECT name_chat,email\n" +
            "FROM user , user_chat_rooms ,chat_room  \n" +
            "WHERE user.id=user_chat_rooms.user_id\n" +
            "and user_chat_rooms.chat_rooms_idchatroom = chat_room.idchatroom\n" +
            "and chat_room.idchatroom =:chatroomid",nativeQuery = true)
    public List<Object[]> ListUsersByChatRoom(Long chatroomid);

    //Verify if user is in chatroom
    @Query(value = "select COUNT(*) FROM user_chat_rooms WHERE user_id = ?1 AND chat_rooms_idchatroom = ?2",
            nativeQuery = true)
    public Long VerifyUserCanInviteToChatRoom(Long userid,Long chatroomid);

}
