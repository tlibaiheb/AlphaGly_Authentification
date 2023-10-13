package hadar.alpha_gly.Services;

import hadar.alpha_gly.Entities.ChatRoom;
import hadar.alpha_gly.Entities.User;

import java.util.List;

public interface IChatRoomService {
    List<ChatRoom> retrieveAllChatrooms();
    boolean AjouterChatrooms (String email,ChatRoom chatroom);

    boolean Updatechatrooms (String email,ChatRoom e);
    boolean RemoveChatrooms(String email,Long idchatroom);

    public ChatRoom retrieveChatRoom(String id);
    public List<ChatRoom> retrieveAllChatroomsSortedByName(String name);

    public List<User> searchUserByFirstLastName(String First, String Last);

    public List<Object[]> ListUsersByChatRoom(Long chatroomid);


}
