package hadar.alpha_gly.Services;

import hadar.alpha_gly.Entities.ChatRoom;
import hadar.alpha_gly.Entities.Message;
import hadar.alpha_gly.Repositories.ChatRoomRepository;
import hadar.alpha_gly.Repositories.MessageRepository;
import hadar.alpha_gly.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class MessageService implements IMessageService{

    MessageRepository messagesRepository;
    ChatRoomRepository chatRoomRepository;

    UserRepository userRepository;

    @Override
    public List<Message> retrieveAllMessages() {
        return (List<Message>) messagesRepository.findAll();
    }

    @Override
    public Message AjouterMessages(Message Message,Long ChatRoom) {
        ChatRoom cr = chatRoomRepository.findById(ChatRoom).get();
        Message.setChatRoom(cr);
        return messagesRepository.save(Message);
    }

    @Override
    public Message UpdateMessages(Message e,Long ChatRoom) {
        ChatRoom cr = chatRoomRepository.findById(ChatRoom).get();
        e.setChatRoom(cr);
        return messagesRepository.save(e);
    }

    /*
        @Override
        public Message AjouterMessages(Message Message,Long ChatRoom) {
        ChatRoom cr = chatRoomRepository.findById(ChatRoom).get();
        Message.setChatRoom(cr);
        messagesRepository.save(Message);
        User user1 = userRepository.findById(5L).get(); // 5L is the id of the user emeteur
        userRepository.insertMessage(5L,2L);
        User user2 = userRepository.findById(4L).get();
        userRepository.recieveMessage(4L,2L);
        return Message;
    }
   */

    @Override
    public Message RetrieveMessages(Long idMessage) {
        return messagesRepository.findById(idMessage).get();
    }

    @Override
    public Message RemoveMessages(Long idMessage) {
        messagesRepository.deleteById(idMessage);
        return null;
    }
   @Override
   public int countMessagesByChatRoom(Long idChatRoom) {
     return messagesRepository.countMessagesByChatRoom(idChatRoom);
   }
/*
    @Override
    public void insertMessage(Long iduser, Long sent_list_idmsg) {
         userRepository.insertMessage(iduser,sent_list_idmsg);
    }
     @Override
     public void recieveMessage(Long iduser,Long received_list_idmsg) {
         userRepository.recieveMessage(iduser,received_list_idmsg);
     }

*/
    //ListMessagesByChatRoom
    @Override
    public List<Object[]>  ListsentMessagesByChatRoom(Long id) {
        return messagesRepository.ListsentMessagesByChatRoom(id);
    }
    @Override
    public List<Object[]>  ListreceivedMessagesByChatRoom() {
        return messagesRepository.ListreceivedMessagesByChatRoom();
    }
    @Override
    public ChatRoom ListMessagesByChatRoom(Long id) {

        //return messagesRepository.ListMessagesByChatRoom(id);
        return chatRoomRepository.findById(id).orElse(null);
    }

}

