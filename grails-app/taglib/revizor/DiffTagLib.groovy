package revizor

import groovy.xml.XmlUtil

import com.revizor.Comment
import com.revizor.CommentType
import com.revizor.LineType
import com.revizor.utils.Constants

import javax.sound.sampled.Line

/**
 * TagLib renders a code of a changed file in a diff format.
 * It means, that it sets up line numbers on the left side
 * and highlights added lines with green colour and removed lines
 * with red one.
 *
 * There are two modes: Single and Side-to-Side. Single mode shows code in one column, where
 * deleted and added lines are displayed on different rows. In the Side-to-side mode
 * an old code and new one are displayed in two columns.
 */
class DiffTagLib {

    private static final String CONTAINER_ID_PREFIX = "comment-container-"
	
    static namespace = "sc"

    /**
     * Print one commit in a diff format
     */
    def showDiffForCommit = { attrs, body ->
		if (null == attrs.repo) {
            out << "A repository is not specified"
        }
        else {
            def diffEntities = getDiffLines(attrs.repo, attrs.commitID);

            diffEntities.each { file ->
				def isShown = attrs.fileName ? file[0].endsWith(attrs.fileName) : true
				
				if (isShown) {

                    def isSideBySideViewMode = Constants.REVIEW_SIDE_BY_SIDE_VIEW.equals(params["viewType"])
					def isReview = (attrs.review != null)
					def comments = isReview ? Comment.findAllByReviewAndTypeAndFileName(attrs.review, CommentType.LINE_OF_CODE, attrs.fileName) : []
					def mapComments = comments.groupBy({ comment -> comment.lineOfCode });
					
					def fileName = extractFileName(file[0])
					def isContentStarted = false;
					def range;
                    def extension = fileName[fileName.lastIndexOf('.')+1 .. -1]
					
					out << "<h3>$fileName</h3>"
	                out << '<div id="diff-panel-id" class="panel panel-default"><div id="diff-container-id" class="panel-body"><table class="code-table">'

                    if (isSideBySideViewMode) {
                        this.renderFileCodeForSideToSideMode(file, isContentStarted, isReview, range, extension, mapComments)
                    }
                    else {
                        this.renderFileCodeForSingleMode(file, isContentStarted, isReview, range, extension, mapComments)
                    }

	                out << '</table></div></div>'
				}
            }
        }

    }

    /**
     * Render code changes in DIFF format for a single file.
     *
     * @param file
     * @param isContentStarted
     * @param isSideBySideViewMode
     * @param isReview
     * @param range
     * @param extension
     * @param mapComments
     * @param lstLeft
     * @param lstRight
     */
    private void renderFileCodeForSingleMode(file, isContentStarted, isReview, range, extension, mapComments) {
        file.eachWithIndex { line, i ->

            byte type = -1;
            def newCommentFormId = "new-comment-container-${i}-id"

            if (line.startsWith('@@')) {

                // range is defined before each code hunk. We use these values as line numbers, increase them on each iteration
                range = this.extractRange(line)

                if (isContentStarted) {
                    // show the break between code hunks
                        out << """
                                    <tr>
                                        <td> </td>
                                        <td colspan='2'><span class='glyphicon glyphicon-resize-small'></span> </td>
                                        <td><hr/></td>
                                    </tr>
                                    """
                }
                isContentStarted = true;
            }
            // skip first five lines, because it is a header
            else if (isContentStarted) {
                this.renderRowForSingleView(line, newCommentFormId, i, type, isReview, range, extension, mapComments)
            }
        }
    }

    /**
     * Render code changes in DIFF format for a single file in Side-to-side mode.
     *
     * @param file
     * @param isContentStarted
     * @param isSideBySideViewMode
     * @param isReview
     * @param range
     * @param extension
     * @param mapComments
     * @param lstLeft
     * @param lstRight
     */
    private void renderFileCodeForSideToSideMode(file, isContentStarted, isReview, range, extension, mapComments) {
        def lstLeft = []
        def lstRight = []

        mapComments.each { k,v ->
            v.each { comment ->
                println "[$k] = '${comment.text}', type = ${comment.typeOfLine}"
            }
        }

        file.eachWithIndex { line, i ->

            if (line.startsWith('@@')) { // for example "@@ -47,51 +47,51 @@ public class Oven {"

                // range is defined before each code hunk. We use these values as line numbers, increase them on each iteration
                range = this.extractRange(line)

                if (isContentStarted) {

                    // at the end of code hunk dump (render) collected lines and draw "break" line
                    this.dumpLinesToRendering(lstLeft, lstRight, isReview, extension, mapComments)
                    lstLeft = []
                    lstRight = []

                    out << """
                                <tr>
                                    <td colspan="2"><hr/></td>
                                    <td><span class='glyphicon glyphicon-resize-small'></span> </td>
                                    <td><span class='glyphicon glyphicon-resize-small'></span> </td>
                                    <td><hr/></td
                                </tr>
                                """

                }
                isContentStarted = true;
            }
            // skip first five lines, because it is a header
            else if (isContentStarted) {
                // collect lines to two arrays
                this.collectLinesIntoTwoArrays(lstLeft, lstRight, line, range, i)
            }
        }

        // final render
        this.dumpLinesToRendering(lstLeft, lstRight, isReview, extension, mapComments)
    }

    /**
     * Render code lines for one code hunk. Used only in Side-by-side Mode.
     *
     * @param lstLeft
     * @param lstRight
     * @param isReview
     * @param extension
     * @param mapComments
     */
    private void dumpLinesToRendering(ArrayList lstLeft, ArrayList lstRight, boolean isReview, extension, Map<Integer, List<Comment>> mapComments) {
            def maxLinesCnt = Math.max(lstLeft.size(), lstRight.size())
            for (int i = 0; i < maxLinesCnt; i++) {
                final CodeLine leftLine = (lstLeft.size() > i) ? lstLeft.get(i) : new CodeLine(codeLine: "")
                final CodeLine rightLine = (lstRight.size() > i) ? lstRight.get(i) : new CodeLine(codeLine: "")
                // render one single row in two columns
                this.renderRowForSideToSideView(
                        leftLine,
                        rightLine,
                        i,
                        isReview,
                        extension,
                        mapComments)
            }
    }

    /**
     * Renders only one row (line) depending on its type for "Single view" mode.
     * Additionally, it prints all the comments under this line.
     *
     * @param line
     * @param newCommentFormId
     * @param i
     * @param type
     * @param isReview
     * @param commentsForTheLine
     * @param range
     * @param extension
     * @param mapComments
     */
    private void renderRowForSingleView(line, newCommentFormId, i, byte type, isReview, range, extension, mapComments) {
        def commentsForTheLine
        def commentContainerId

        out << "<tr>"

        if (line.startsWith('-')) {
            commentContainerId = CONTAINER_ID_PREFIX + range.original + "-" + LineType.ORIGINAL
            type = Constants.ACTION_DELETED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.ORIGINAL, range.original, i, commentContainerId)}</td>"
            out << "<td class='line-deleted'>${range.original}</td>"
            out << "<td> </td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.original, [LineType.ORIGINAL])
            range.original++
        } else if (line.startsWith('+')) {
            commentContainerId = CONTAINER_ID_PREFIX + range.new + "-" + LineType.NEW
            type = Constants.ACTION_ADDED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.NEW, range.new, i, commentContainerId)}</td>"
            out << "<td> </td>"
            out << "<td><span class='line-added'>${range.new}</span></td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.new, [LineType.NEW])
            range.new++
        } else {
            commentContainerId = CONTAINER_ID_PREFIX + range.original + "-" + LineType.UNMODIFIED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.UNMODIFIED, range.original, i, commentContainerId)}</td>"
            out << "<td>${range.original}</td>"
            out << "<td>${range.new++}</td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.original, [LineType.UNMODIFIED])
            range.original++
        }


        out << "<td>"
        out << printStyledLineOfCode(line, type, extension)
        out << this.renderComments(commentContainerId, commentsForTheLine, newCommentFormId)
        out << "</td>"
        out << "</tr>"
    }

    private void collectLinesIntoTwoArrays(listLeft, listRight, line, range, idx) {
        if (line.startsWith('-')) {
            listLeft << new CodeLine(type: Constants.ACTION_DELETED, lineNumber: range.original, codeLine: line, lineType: LineType.ORIGINAL, no: idx)
            range.original++

        } else if (line.startsWith('+')) {
            listRight << new CodeLine(type: Constants.ACTION_ADDED, lineNumber: range.new, codeLine: line, lineType: LineType.NEW, no: idx)
            range.new++

        } else {
            listLeft << new CodeLine(lineNumber: range.original, codeLine: line, lineType: LineType.UNMODIFIED, no: idx)
            listRight << new CodeLine(lineNumber: range.new, codeLine: line, lineType: LineType.UNMODIFIED, no: idx)
            range.original++
            range.new++
        }
    }

    /**
     * Renders only one row (line) with two columns for the "Side to side" mode.
     * Additionally, it prints all the comments under this line.
     *
     * @param line
     * @param commentContainerId
     * @param newCommentFormId
     * @param i
     * @param type
     * @param isReview
     * @param commentsForTheLine
     * @param range
     * @param extension
     * @param mapComments
     */
    private void renderRowForSideToSideView(CodeLine lineLeft, CodeLine lineRight, i, isReview, extension, mapComments) {
        out << "<tr>"

        def commentContainerLeftId = CONTAINER_ID_PREFIX + lineLeft.lineNumber + "-" + lineLeft.lineType
        def commentContainerRightId = CONTAINER_ID_PREFIX + lineRight.lineNumber + "-" + LineType.NEW

        def commentsForTheLeftLine = this.findCommentsForLine(mapComments, lineLeft.lineNumber, [LineType.ORIGINAL, LineType.UNMODIFIED])
        def commentsForTheRightLine = this.findCommentsForLine(mapComments, lineRight.lineNumber, [LineType.NEW])

        def newCommentFormLeftId = "new-comment-container-${lineLeft.no}-left-id"
        def newCommentFormRightId = "new-comment-container-${lineRight.no}-right-id"

        /*
         Left Column:
         */
        out << "<td>"
        out << printStyledLineOfCode(lineLeft.codeLine, lineLeft.type, extension)
        out << this.renderComments(commentContainerLeftId, commentsForTheLeftLine, newCommentFormLeftId)
        out << "</td>"
        if (lineLeft.lineType == LineType.ORIGINAL) {
            out << "<td>${getButtonHtml(isReview, newCommentFormLeftId, LineType.ORIGINAL, lineLeft.lineNumber, i, commentContainerLeftId)}</td>"
        }
        else {
            out << "<td>${getButtonHtml(isReview, newCommentFormLeftId, LineType.UNMODIFIED, lineLeft.lineNumber, i, commentContainerLeftId)}</td>"
        }
        out << "<td>${lineLeft.lineNumber == 0 ? "" : lineLeft.lineNumber}</td>"

        /*
         Right column:
         */
        out << "<td>${lineRight.lineNumber == 0 ? "" : lineRight.lineNumber}</td>"

        if (lineRight.lineType == LineType.NEW) {
            out << "<td>${getButtonHtml(isReview, newCommentFormRightId, LineType.NEW, lineRight.lineNumber, i, commentContainerRightId)}</td>"
        }
        else {
            out << "<td></td>"
        }

        out << "<td>"
        out << printStyledLineOfCode(lineRight.codeLine, lineRight.type, extension)
        if (lineRight.lineType == LineType.NEW) {
            out << this.renderComments(commentContainerRightId, commentsForTheRightLine, newCommentFormRightId)
        }
        out << "</td>"

        out << "</tr>"

    }

    private String renderComments(commentContainerId, commentsForTheLine, newCommentFormId) {
        def html = new StringBuffer()
        // add comment for this line of code, if they exist
        html << "<div id='${commentContainerId}' class='code-line-comments' style='display:${commentsForTheLine ? "visible" : "none"};'>"
        if (commentsForTheLine) {
            html << cmt.printCommentsInHierarchy(['comments': commentsForTheLine, 'indent': 0])
        }
        html << "</div>"

        // placeholder, where will be added the form to write a new comment or to reply to another
        html << "<div id='${newCommentFormId}' class='panel' style='display:none;' />"

        return html.toString()
    }

    private String getButtonHtml(isReview, newCommentFormID, lineType, lineOfCode, idx, commentContainerID) {

        def style = (lineType == LineType.ORIGINAL) ? "btn-comment-deleted" : ( (lineType == LineType.NEW) ? "btn-comment-added" : "" )

		// the function "showForm" in the _commentsScript.gsp
		return isReview ?
				"""
				<button id='show-form-btn-${idx}' class='btn-comment btn btn-default btn-xs ${style}' onclick='showForm(\"${newCommentFormID}\", \"${lineType}\", ${lineOfCode}, \"${commentContainerID}\", null, 0);'>
					<span class='glyphicon glyphicon-comment'></span>
				</button>
				"""
				: " "
	}

	private String printStyledLineOfCode(String line, byte type, String extension) {
		def additionalClass = ''
		if (type == Constants.ACTION_DELETED) {
			additionalClass = 'line-deleted';
		}
		if (type == Constants.ACTION_ADDED) {
			additionalClass = 'line-added';
		}
        def lang = extension ? "lang-$extension" : ""
		return "<pre class='prettyprint ${lang} reset-pre $additionalClass'>${XmlUtil.escapeXml(line)}</pre>"
	}

	public static getDiffLines(repository, commitID) {
		def repo = repository.initImplementation();
		return repo.getDiffForCommit(commitID);
	} 

	/**
	 * Extracts only the name of file omitting the path
	 */
	def extractFileName (line) {
		return line.split('/')[-1];
	}
	
	/**
	 * Extracts two values - range "since" for original and for new files.
	 *
	 * @param line, for example '@@ -92,7 +92,7 @@ class CommentController {'
	 * @return map
	 */
    def extractRange(line) {
		return [
            'original': (line =~ /(?<=@@\s-)\d+(?=,)/ )[0].toInteger(),
            'new': (line =~ /(?<=\+)\d+(?=,\d+\s@@)/)[0].toInteger()
            ]
    }

    /**
     * Extracts comments only for current line (idx) and type
     *
     * @param map - map "line number" <=> "list of comments"
     * @param idx - line number
     * @param lstTypes - list of comment types that expected to be returned
     * @return
     */
    private List findCommentsForLine(map, idx, lstTypes) {
    	def lstComments = map[idx]
    	return (lstComments) ?
                lstComments.findAll { it.typeOfLine in lstTypes}
                : null
    }

    private class CodeLine {
        String codeLine
        int lineNumber
        int no
        byte type
        LineType lineType
    }

}
