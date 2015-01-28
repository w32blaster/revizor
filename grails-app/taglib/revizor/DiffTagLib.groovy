package revizor

import groovy.xml.XmlUtil

import com.revizor.Comment
import com.revizor.CommentType
import com.revizor.LineType
import com.revizor.utils.Constants

/**
 * TagLib renders a code of a changed file in a diff format.
 * It means, that it sets up line numbers on the left side
 * and highlights added lines with green colour and removed lines
 * with red one.
 *
 */
class DiffTagLib {
	
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

                    def isSingleViewMode = Constants.REVIEW_SINGLE_VIEW .equals(params["viewType"])
					def isReview = (attrs.review != null)
					def comments = isReview ? Comment.findAllByReviewAndTypeAndFileName(attrs.review, CommentType.LINE_OF_CODE, attrs.fileName) : []
					def mapComments = comments.groupBy({ comment -> comment.lineOfCode });
					
					def fileName = extractFileName(file[0])
					def isContentStarted = false;
					def range;
                    def extension = fileName[fileName.lastIndexOf('.')+1 .. -1]
					
					out << "<h3>$fileName</h3>"
	                out << '<div id="diff-panel-id" class="panel panel-default"><div class="panel-body"><table class="code-table">'
	                file.eachWithIndex { line, i ->

						byte type = -1;
						def newCommentFormId = "new-comment-container-${i}-id"
						def commentContainerId = "comment-container-"
						
						if (line.startsWith('@@')) {
							range = extractRange(line)

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
                            if (isSingleViewMode) {
                                this.renderRowForSingleView(line, commentContainerId, newCommentFormId, i, type, isReview, range, extension, mapComments)
                            }
                            else {
                                this.renderRowForSideToSideView(line, commentContainerId, newCommentFormId, i, type, isReview, range, extension, mapComments)
                            }
	                	}
	                    
	                }
	                out << '</table></div></div>'
				}
            }
        }

    }

    /**
     * Renders only one row (line) depending on its type for "Single view" mode.
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
    private void renderRowForSingleView(line, commentContainerId, newCommentFormId, i, byte type, isReview, range, extension, mapComments) {
        def commentsForTheLine
        out << "<tr>"

        if (line.startsWith('-')) {
            commentContainerId += range.original + "-" + LineType.ORIGINAL
            type = Constants.ACTION_DELETED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.ORIGINAL, range.original, i, commentContainerId)}</td>"
            out << "<td class='line-deleted'>${range.original}</td>"
            out << "<td> </td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.ORIGINAL)
        } else if (line.startsWith('+')) {
            commentContainerId += range.new + "-" + LineType.NEW
            type = Constants.ACTION_ADDED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.NEW, range.new, i, commentContainerId)}</td>"
            out << "<td> </td>"
            out << "<td><span class='line-added'>${range.new}</span></td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.new++, LineType.NEW)
        } else {
            commentContainerId += range.original + "-" + LineType.UNMODIFIED
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.UNMODIFIED, range.original, i, commentContainerId)}</td>"
            out << "<td>${range.original}</td>"
            out << "<td>${range.new++}</td>"
            commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.UNMODIFIED)
        }


        out << "<td>"
        out << printStyledLineOfCode(line, type, extension)
        out << this.renderComments(commentContainerId, commentsForTheLine, newCommentFormId)
        out << "</td>"
        out << "</tr>"
    }


    /**
     * Renders only one row (line) depending on its type for "Side to side" mode.
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
    private void renderRowForSideToSideView(line, commentContainerId, newCommentFormId, i, byte type, isReview, range, extension, mapComments) {
        def commentsForTheLine
        out << "<tr>"

        if (line.startsWith('-')) {
            commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.ORIGINAL)
            commentContainerId += range.original + "-" + LineType.ORIGINAL
            type = Constants.ACTION_DELETED
            out << "<td>"
            out << printStyledLineOfCode(line, type, extension)
            out << this.renderComments(commentContainerId, commentsForTheLine, newCommentFormId)
            out << "</td>"
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.ORIGINAL, range.original, i, commentContainerId)}</td>"
            out << "<td class='line-deleted'>${range.original}</td>"
            out << "<td> </td>"
            out << "<td> </td>"
        } else if (line.startsWith('+')) {
            commentsForTheLine = findCommentsForLine(mapComments, range.new++, LineType.NEW)
            commentContainerId += range.new + "-" + LineType.NEW
            type = Constants.ACTION_ADDED
            out << "<td> </td>"
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.NEW, range.new, i, commentContainerId)}</td>"
            out << "<td> </td>"
            out << "<td><span class='line-added'>${range.new}</span></td>"
            out << "<td>"
            out << printStyledLineOfCode(line, type, extension)
            out << this.renderComments(commentContainerId, commentsForTheLine, newCommentFormId)
            out << "</td>"
        } else {
            commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.UNMODIFIED)
            commentContainerId += range.original + "-" + LineType.UNMODIFIED
            out << "<td>"
            out << printStyledLineOfCode(line, type, extension)
            out << this.renderComments(commentContainerId, commentsForTheLine, newCommentFormId)
            out << "</td>"
            out << "<td>${getButtonHtml(isReview, newCommentFormId, LineType.UNMODIFIED, range.original, i, commentContainerId)}</td>"
            out << "<td>${range.original}</td>"
            out << "<td>${range.new++}</td>"
            out << "<td>"
            out << printStyledLineOfCode(line, type, extension)
            out << "</td>"

        }

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

		// the function "showForm" in the _commentsScript.gsp
		return isReview ?
				"""
				<button id='show-form-btn-${idx}' class='btn-comment btn btn-default btn-xs' onclick='showForm(\"${newCommentFormID}\", \"${lineType}\", ${lineOfCode}, \"${commentContainerID}\", null, 0);'>
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
	 * Extracts two values - range since for original and for new files.
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

    private List findCommentsForLine(map, idx, type) {
    	def lst = map[idx]
    	return (lst && lst[0].typeOfLine == type) ? lst : null 
    }

}
