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

					def comments = Comment.findAllByReviewAndTypeAndFileName(attrs.review, CommentType.LINE_OF_CODE, attrs.fileName)
					def mapComments = comments.groupBy({ comment -> comment.lineOfCode });
					
					def fileName = extractFileName(file[0])
					def isContentStarted = false;
					def range;
                    def extension = fileName[fileName.lastIndexOf('.')+1 .. -1]
					
					out << "<h3>$fileName</h3>"
	                out << '<div id="diff-panel-id" class="panel panel-default"><div class="panel-body"><table class="code-table">'
	                file.eachWithIndex { line, i ->

						byte type = -1;
						def commentsForTheLine;
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
							out << "<tr>"
							
							if (line.startsWith('-')) {
								commentContainerId += range.original + "-" + LineType.ORIGINAL
								type = Constants.ACTION_DELETED
								out << "<td>${getButtonHtml(newCommentFormId, LineType.ORIGINAL, range.original, i, commentContainerId)}</td>"
								out << "<td class='line-deleted'>${range.original}</td>"
								out << "<td> </td>"
								commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.ORIGINAL)
							}
							else if (line.startsWith('+')) {
								commentContainerId += range.new + "-" + LineType.NEW
								type = Constants.ACTION_ADDED
								out << "<td>${getButtonHtml(newCommentFormId, LineType.NEW, range.new, i, commentContainerId)}</td>"
								out << "<td> </td>"
								out << "<td><span class='line-added'>${range.new}</span></td>"
								commentsForTheLine = findCommentsForLine(mapComments, range.new++, LineType.NEW)
							}
							else {
								commentContainerId += range.original + "-" + LineType.UNMODIFIED
								out << "<td>${getButtonHtml(newCommentFormId, LineType.UNMODIFIED, range.original, i, commentContainerId)}</td>"
								out << "<td>${range.original}</td>"
								out << "<td>${range.new++}</td>"
								commentsForTheLine = findCommentsForLine(mapComments, range.original++, LineType.UNMODIFIED)
							}
							
		                    out << "<td>"
							out << printStyledLineOfCode(line, type, extension)
							
							// add comment for this line of code, if they exist
							out << "<div id='${commentContainerId}' class='code-line-comments' style='display:${commentsForTheLine ? "visible" : "none"};'>"

							if(commentsForTheLine) {
								out << cmt.printCommentsInHierarchy(['comments': commentsForTheLine, 'indent': 0])
							}
/*
							if (commentsForTheLine) {
								commentsForTheLine.each { comment ->
									out << g.render(template: "/comment/comment", model: ['comment': comment])
								}
							}*/
							out << "</div>"

							// placeholder, where will be added the form to write a new comment or to reply to another
							out << "<div id='${newCommentFormId}' class='panel' style='display:none;' />"

							out << "</td>"
		                    out << "</tr>"
	                	}
	                    
	                }
	                out << '</table></div></div>'
				}
            }
        }

    }
	
	private String getButtonHtml(newCommentFormID, lineType, lineOfCode, idx, commentContainerID) {
		return """
				<button id='show-form-btn-${idx}' class='btn-comment btn btn-default btn-xs' onclick='showForm(this, \"${newCommentFormID}\", \"${lineType}\", ${lineOfCode}, \"${commentContainerID}\", \"${CommentType.LINE_OF_CODE.name()}\");'>
					<span class='glyphicon glyphicon-comment'></span>
				</button>
				"""
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
