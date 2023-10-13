package hadar.alpha_gly.Services;

import hadar.alpha_gly.Entities.ChatRoom;
import hadar.alpha_gly.Entities.User;
import hadar.alpha_gly.Repositories.ChatRoomRepository;
import hadar.alpha_gly.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;


@Service
@AllArgsConstructor
public class ChatRoomService implements IChatRoomService {

    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;
    IUserService userService;


    @Override
    public List<ChatRoom> retrieveAllChatrooms() {
        return (List<ChatRoom>) chatRoomRepository.findAll();
    }



    @Override
    @Transactional
    public boolean AjouterChatrooms(String email,@RequestBody ChatRoom ChatRoom) {
         chatRoomRepository.save(ChatRoom);
         userService.AffectToChatRoom(email,ChatRoom);
         return true;

    }

    @Override
    public boolean Updatechatrooms(String email,ChatRoom e) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null)
            if (chatRoomRepository.VerifyUserCanInviteToChatRoom(user.getId(), e.getIdChatRoom()) >= 1) {
                {
                    chatRoomRepository.save(e);
                    return true;
                }

            }
        return false;
    }


    @Override
    public boolean RemoveChatrooms(String email,Long idchatroom) {
        ChatRoom chatRoom= chatRoomRepository.findById(idchatroom).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);
        if (chatRoom!=null && user != null)
            if (chatRoomRepository.VerifyUserCanInviteToChatRoom(user.getId(), idchatroom) >= 1)
            {
            chatRoom.setVisibility(false);
            chatRoomRepository.save(chatRoom);
            return true;
            }
        return false;
    }

    @Override
    public ChatRoom retrieveChatRoom(String id) {
        return chatRoomRepository.findById(Long.parseLong(id)).get();
    }


    @Override
    public List<ChatRoom> retrieveAllChatroomsSortedByName(String name) {
        List<ChatRoom> chatroom =chatRoomRepository.retrieveAllChatroomsSortedByName(name);
        for (ChatRoom chatRoom : chatroom) {
            System.out.println(chatRoom);
        }
        return chatroom;
    }

    @Override
    public List<User> searchUserByFirstLastName(String First, String Last) {
        return userRepository.findByFirstNameOrLastNameContainingIgnoreCase(First, Last);
    }

    //list users by chatroom
    @Override
    public List<Object[]> ListUsersByChatRoom(Long chatroomid) {
        return chatRoomRepository.ListUsersByChatRoom(chatroomid);
    }

}

