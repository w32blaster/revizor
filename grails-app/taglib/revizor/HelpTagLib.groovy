package revizor

import com.revizor.Reviewer
import com.revizor.User

/**
 * Helpers tag lib
 */
class HelpTagLib {
    static namespace = "h"

    /**
     * Tag renders a template in one line. It could be useful in the Javascript code to append any
     * HTML code on the fly
     */
    def renderInOneLine = { attrs ->
        if (!attrs.template) {
            out << "Error, template should be presented in the renderInOneLine tag";
            return;
        }
        
        def html = g.render(template: attrs.template, model: attrs.model)
        
        out << HelpTagLib.toSingleLine(html);
    }

    public static String toSingleLine(html) {
        html = html.replace("\n\n", "")
        // remove whitespaces between tags
        html = (html =~ />{1}\s+<{1}/).replaceAll("><")

        // TODO: replace this with trim()
        // remove whitespace before the first and after the last tag
        html = (html =~ /^\s+<{1}/).replaceAll("<")
        return (html =~ />{1}\s+$/).replaceAll(">")
    }

    /**
     * Checks whether given user is in the reviewers list
     *
     * @param currentUser - user to be checked
     * @param lstReviewers - list of reviewers
     * @return
     */
    public static boolean isUserIsReviewer(User currentUser, def lstReviewers) {
        for (Reviewer rev : lstReviewers) {
            if (rev.reviewer.id == currentUser.id)
                return true
        }
        return false;
    }
}