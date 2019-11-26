package ImageHoster.controller;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.service.CommentService;
import ImageHoster.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "/image/{imageId}/{title}/comments", method = RequestMethod.POST)
    public String createComment(@RequestParam("comment") String comment, @PathVariable("imageId") Integer imageId, @PathVariable("title") String title,
                                Comment newComment, Model model, HttpSession session) throws IOException {

        Image image = imageService.getImage( imageId );
        List<Comment> commentList;
        if (image.getComments().isEmpty()) {
            commentList = new ArrayList<>();
        } else {
            commentList = image.getComments();
        }
        User user = (User) session.getAttribute( "loggeduser" );
        newComment.setImage( image );
        newComment.setUser( user );
        newComment.setCreatedDate( new Date() );
        newComment.setText( comment.toString() );
        Comment createdComment = commentService.createComment( newComment );
        commentList.add( createdComment );
        image.setComment( commentList );
        imageService.updateImage( image );
        return "redirect:/images/" + imageId + "/" + title;
    }

    private String convertUploadedFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString( file.getBytes() );
    }

}
