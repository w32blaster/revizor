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
        comments.each { Comment comment ->
            out << g.render(template: "/comment/comment", model: ['comment': comment, 'indent': indent])
            _recursivelyPrintCommentReplies(++indent, comment.replies, out);
        }
    }

}
