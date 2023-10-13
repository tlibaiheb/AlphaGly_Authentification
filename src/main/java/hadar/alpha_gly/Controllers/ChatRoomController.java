package hadar.alpha_gly.Controllers;


import hadar.alpha_gly.Entities.ChatRoom;
import hadar.alpha_gly.Entities.User;
import hadar.alpha_gly.Services.IChatRoomService;
import hadar.alpha_gly.Services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/chatroom/")
public class ChatRoomController {
    private IChatRoomService iChatRoomService;
    IUserService iUserService;

    @GetMapping("/retrieve-ChatRoom/{ChatRoom-id}")
    public ChatRoom retrieveChatRoom(@PathVariable("ChatRoom-id") String id) {
        return iChatRoomService.retrieveChatRoom(id);
    }


    @PostMapping("createChatroom")

    public ResponseEntity<?> createChatroom(HttpServletRequest request, @RequestBody ChatRoom ce) {
        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if ( iChatRoomService.AjouterChatrooms(email,ce))
            return ResponseEntity.status(HttpStatus.OK).body("Chatroom created !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");

    }


    //AddUserToChatRoom
    @PostMapping("AddUserToChatRoom")
    public ResponseEntity<?> AddUserToChatRoom(HttpServletRequest request,Long  Chatroomid, String invitedUserEmail) {

        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if (iUserService.AddUserToChatRoom(email,Chatroomid,invitedUserEmail))
            return ResponseEntity.status(HttpStatus.OK).body("User Has Been Invited !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");
    }
    @PutMapping("/updatechatroom")
    public ResponseEntity updatechatroom(HttpServletRequest request,@RequestBody ChatRoom e) {

        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
         if (iChatRoomService.Updatechatrooms(email,e))
            return ResponseEntity.status(HttpStatus.OK).body("Chatroom Updated !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");

    }


    @PutMapping("/removechatroom/{idchatroom}")
    public ResponseEntity RemoveChatrooms(HttpServletRequest request,@PathVariable("idchatroom") Long idchatroom) {

        String email= iUserService.UserVerificationReturnEmail(request);
        if (email.equals("Token Doesn't Match Authenfied User"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        if (iChatRoomService.RemoveChatrooms(email,idchatroom))
            return ResponseEntity.status(HttpStatus.OK).body("Chatroom Removed !");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Went Wrong !");
    }
    /*
    //getUserByName
    @GetMapping("/getUserByName/{name}")
    public User getUserByName(@PathVariable("name") String name) {
        return iChatRoomService.getUserByName(name);
    }*/
//searchChatRoomByName
    /*
    @GetMapping("/searchChatRoomByName/{name}")
    public List<ChatRoom> searchChatRoomByName(@PathVariable("name") String name) {
        return iChatRoomService.searchChatRoomByName(name);
    }*/
    //retrieveAllChatroomsSortedByName
    @GetMapping("/retrieveAllChatroomsSortedByName/{name}")
    public List<ChatRoom> retrieveAllChatroomsSortedByName(@PathVariable("name") String name) {
        return iChatRoomService.retrieveAllChatroomsSortedByName(name);
    }
    //searchUserByFirstLastName
    @GetMapping("/searchUserByFirstLastName/{First}/{Last}")
    public List<User> searchUserByFirstLastName(@PathVariable("First") String First,@PathVariable("Last") String Last) {
        return iChatRoomService.searchUserByFirstLastName(First,Last);
    }

    //public int countChatRoomByUser(Long userid);
    @GetMapping("/countChatRoomByUser/{userid}")
    public int countChatRoomByUser(@PathVariable("userid") Long userid) {
       // return iChatRoomService.countChatRoomByUser(userid);
    return 0;
    }
    //listUsersByChatRoom
    @GetMapping("/listUsersByChatRoom/{chatroomid}")
    public List<Object[]> listUsersByChatRoom(@PathVariable("chatroomid") Long chatroomid) {
        return iChatRoomService.ListUsersByChatRoom(chatroomid);
    }
}
