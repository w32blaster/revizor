package revizor

import com.revizor.Comment

class CommentTagLib {
    static namespace = "cmt"

    def printCommentsInHierarchy = {attrs, body ->
        def comments = attrs.comments;

        def rootComments = comments.findAll { Comment comment -> comment.replies.isEmpty() }

        _recursivelyPrintCommentReplies(0, rootComments, out);
    }

    def _recursivelyPrintCommentReplies(indent, comments, out) {
        println comments
        comments.each { Comment comment ->
            out << g.render(template: "/comment/comment", model: ['comment': comment, 'indent': indent])
            out << "<div id='replies-container-${comment.id}-id' style='margin-left: ${indent * 5}px;'>";
            _recursivelyPrintCommentReplies(indent + 1, comment.replies, out);
            out << "<div id='new-reply-to-${comment.id}-form' class='panel' style='display:none;' />"
            out << "</div>";
        }
    }

}
