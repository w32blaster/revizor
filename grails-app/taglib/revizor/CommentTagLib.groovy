package revizor

import com.revizor.Comment
import com.revizor.User
import com.revizor.utils.Constants

class CommentTagLib {

    static namespace = "cmt"

    def printCommentsInHierarchy = {attrs, body ->
        def comments = attrs.comments;
        def unreadComments = attrs.unreadComments

        def rootComments = comments.findAll { Comment comment -> !comment.replyTo }

        _recursivelyPrintCommentReplies(0, rootComments, unreadComments);
    }

    def printOneComment = { attrs, body ->
        _recursivelyPrintCommentReplies(attrs.indent, [attrs.comment], []);
    }

    def _recursivelyPrintCommentReplies(indent, comments, unreadComments) {
        comments
            .sort { it.ident() }
            .each { Comment comment ->

                out << g.render(template: "/comment/comment", model: [
                        'comment': comment,
                        'indent': indent,
                        'isUnread': (comment.ident() in unreadComments) ])

                out << "<div id='replies-container-${comment.id}-id' style='max-width:800px;'>";
                    if(comment.replies) {
                        _recursivelyPrintCommentReplies( (indent + 1), comment.replies, unreadComments);
                    }
                    out << "<div id='new-reply-to-${comment.id}-form' class='panel' style='display:none;'></div>"
                out << "</div>"
        }
    }

    def highlightUsername = {attrs, body ->

        def highlightedBody = body().replaceAll(/${Constants.USERNAME_DELIMETER}[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+/,
                { String email ->
                    def mentionedUser = User.findByEmail(email.substring(1));
                    g.render(template: "/comment/mention", model: ['user': mentionedUser])
                })
        out << highlightedBody
    }

}
