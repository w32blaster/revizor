package revizor

import com.revizor.Comment

class CommentTagLib {

    static namespace = "cmt"

    def printCommentsInHierarchy = {attrs, body ->
        def comments = attrs.comments;

        def rootComments = comments.findAll { Comment comment -> !comment.replyTo }

        _recursivelyPrintCommentReplies(0, rootComments);
    }

    def printOneComment = { attrs, body ->
        _recursivelyPrintCommentReplies(attrs.indent, [attrs.comment]);
    }

    def _recursivelyPrintCommentReplies(indent, comments) {
        comments
            .sort { it.ident() }
            .each { Comment comment ->

                out << g.render(template: "/comment/comment", model: ['comment': comment, 'indent': indent])

                out << "<div id='replies-container-${comment.id}-id' style='max-width:800px;'>";
                    if(comment.replies) {
                        _recursivelyPrintCommentReplies( (indent + 1), comment.replies);
                    }
                    out << "<div id='new-reply-to-${comment.id}-form' class='panel' style='display:none;'></div>"
                out << "</div>"
        }
    }

}
