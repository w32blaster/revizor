package revizor

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
       // remove whitespaces between tags
        html = (html =~ />{1}\s+<{1}/).replaceAll("><")

        // TODO: replace this with trim()
        // remove whitespace before the first and after the last tag
        html = (html =~ /^\s+<{1}/).replaceAll("<")
        return (html =~ />{1}\s+$/).replaceAll(">")
    }
}