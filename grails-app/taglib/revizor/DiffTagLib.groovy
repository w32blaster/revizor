package revizor

import groovy.xml.XmlUtil

import com.revizor.Comment
import com.revizor.CommentType
import com.revizor.LineType
import com.revizor.utils.Constants

class DiffTagLib {
	
    static namespace = "sc"
	private static final int SYMBOLS_TO_SKIP = "--- a/".length()
	private static final int INDENT_TREE = 10; 

	/**
	 * Prints the tree of modified files belonging to a given commit
	 * 
	 */
	def showFilesForReview = { attrs, body ->
		if (null == attrs.repo) {
			out << "A repository is not specified"
		}
		else {
			def diffEntities = getDiffLines(attrs.repo, attrs.commitID);
			def reviewId = attrs.reviewId
			
			def fileNames = [] as Set 
			diffEntities.each { file ->
				
				fileNames.addAll(file.findAll{ 
					it.startsWith("---") || it.startsWith('+++')
				})
				
			}
			
			NodeFile root = this.buildMapOfFiles(fileNames); 
			def hrefFileDetailsBase = g.createLink(controller: 'review', action: 'show', id: reviewId) + "/" + Constants.REVIEW_SINGLE_VIEW + "?" + Constants.PARAM_FILE_NAME + '='

			// print fileNames
			out << '<div class="list-group">'
			
			_recursivelyPrintTree(root, 0, hrefFileDetailsBase)
			
			out << '</div>'
			
		}
	}
	
	def _recursivelyPrintTree(node, level, hrefFileDetailsBase) {
		if (node.isLeaf) {
			
			def styleFileStatus
			switch (node.status) {
				case Constants.ACTION_ADDED:
					styleFileStatus = "added"
					break;

				case Constants.ACTION_DELETED:
					styleFileStatus = "deleted"
					break;

				case Constants.ACTION_MODIFIED:
					styleFileStatus = "modified"
					break;
			}
			
			out << """
					<div class='tree-leaf' style='margin-left: ${level * INDENT_TREE}px;'>
						<span class='${styleFileStatus} new'>
							<a href="${hrefFileDetailsBase + node.fullPath}">
								${node.name}
							</a>
						</span>
					</div>
				   """
		}
		else {
			out << "<div class='tree-node' style='margin-left: ${level * INDENT_TREE}px;'>"
					
				    if(level == 0) {
						out << "<span class='ico'>/ </span>"
					}
					else {
						out << "<span class='ico glyphicon glyphicon-chevron-down'></span>"
					}
					
			out << " ${node.name}</div>"
			
			node.children.each { child ->
				_recursivelyPrintTree(child, level + 1, hrefFileDetailsBase)
			}
		}
	}
	
	/**
	 * Builds tree of files that will be used to build a graphical tree
	 * It should modify path to a nested maps. For example,
	 * 
	 * 	--- a/src/package/c.groovy
	 * 
	 * should produce the tree (not map, but NodeFile files):
	 * 	
	 * ['src':
	 * 		['package':[
	 * 			'name': 'c.groovy',
	 * 			'status': 1
	 * 			]
	 * 		]
	 * ]
	 * 
	 * @return NodeFile with populates subnodes (children).
	 */
	def buildMapOfFiles(lstFiles) {
		NodeFile rootNode = new NodeFile('name': 'root', 'children': [], 'parent': null)
		
		boolean isAddedNew = false
		lstFiles.eachWithIndex { file, i ->
			if (file == "--- /dev/null") {
				// next file in the list will be added
				isAddedNew = true
			}
			else if (file == "+++ /dev/null") {
				def fullPath = lstFiles[i-1].substring(SYMBOLS_TO_SKIP)
				_recursiveAddFileToTree(rootNode, fullPath.split("/"), getTypeOfLine(file), fullPath);
			}
			else {
				def fullPath = file.substring(SYMBOLS_TO_SKIP)
				
				if (isAddedNew) {
					// on previous iteration we found '--- /dev/null', that means this file is completely new
					isAddedNew = false;
					_recursiveAddFileToTree(rootNode, fullPath.split("/"), getTypeOfLine(lstFiles[i-1]), fullPath);
				}
				else {
					_recursiveAddFileToTree(rootNode, fullPath.split("/"), getTypeOfLine(file), fullPath);
				}
			}
		}
		
		return rootNode
	}
	
	/**
	 * Retrieves the type of a line by its prefix
 	 */
	def getTypeOfLine(line) {
		if (line.startsWith("--- a/") || line.startsWith("+++ b/")) {
			return Constants.ACTION_MODIFIED;
		}
		else if (line.startsWith("--- /dev/null")) {
			return Constants.ACTION_ADDED;
		}
		else if (line.startsWith("+++ /dev/null")) {
			return Constants.ACTION_DELETED;
		}
		else {
			return -1;
		}
	}
	
	/**
	 * Recursively builds one branch of a tree and appends a file as a leaf of that branch.
 	 */
	private _recursiveAddFileToTree(parentNode, arrPath, status, fullPath) {
		
		// find itself: as long as we have two records for each file (--- a/ and +++ b/) 
		// the current node could be created on previous iteration
		def currentNode = parentNode.children.find { it.name.equals(arrPath[0]) }
		
		if(arrPath.size() > 1) {
			if (currentNode) {
				_recursiveAddFileToTree(currentNode, arrPath[1..arrPath.size()-1], status, fullPath)
			}
			else {
				currentNode = new NodeFile('name': arrPath[0], 'children': [], 'parent': parentNode)
				parentNode.children << currentNode 
				_recursiveAddFileToTree(currentNode, arrPath[1..arrPath.size()-1], status, fullPath)
			}
		}
		else {
			if (currentNode) {

				if (currentNode.status == Constants.ACTION_ADDED && status == Constants.ACTION_MODIFIED) {
					currentNode.status = Constants.ACTION_ADDED;
				}
				else if (currentNode.status == Constants.ACTION_MODIFIED && status == Constants.ACTION_DELETED) {
					currentNode.status = Constants.ACTION_DELETED;
				}

			}
			else {
				
				// leaf:
				currentNode = new NodeFile(
					'name': arrPath[0], 
					'children': null, 
					'parent': parentNode, 
					'isLeaf' : true,
					'fullPath': fullPath, 
					'status': status)
				
				parentNode.children << currentNode
			}
		}
	}
	
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
					
					out << "<h2>$fileName</h2>"
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
								out << "<td class='line-added'>${range.new}</td>"
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
							out << printStyledLineOfCode(line, type)
							
							// add comment for this line of code, if they exist
							out << "<div id='${commentContainerId}' class='code-line-comments' style='display:${commentsForTheLine ? "visible" : "none"};'>"
							if (commentsForTheLine) {
								commentsForTheLine.each { comment ->
									out << g.render(template: "/comment/comment", model: ['comment': comment])
								}
							}
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
	
	private String getButtonHtml(newCommentFormID, commentType, lineOfCode, idx, commentContainerID) {
		return """
				<button id='show-form-btn-${idx}' class='btn-comment btn btn-default btn-xs' onclick='showForm(this, \"${newCommentFormID}\", \"${commentType}\", ${lineOfCode}, \"${commentContainerID}\");'>
					<span class='glyphicon glyphicon-comment'></span>
				</button>
				"""
	}

	private String printStyledLineOfCode(String line, byte type) {
		def additionalClass = ''
		if (type == Constants.ACTION_DELETED) {
			additionalClass = 'line-deleted';
		}
		if (type == Constants.ACTION_ADDED) {
			additionalClass = 'line-added';
		}
		return "<code class='line $additionalClass'>${XmlUtil.escapeXml(line)}</code>"
	}

	private def getDiffLines(repository, commitID) {
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

/**
 * Class represents a node in a tree
 * 
 * @author ilja
 *
 */
public class NodeFile {

	String name;
	String fullPath;
	byte status;
	boolean isLeaf = false;
	List<NodeFile> children;
	NodeFile parent;
	
	/**
	 * Overrided method to compare two trees. Used mostly by Unit tests
 	*/
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		NodeFile other = (NodeFile) obj;
		
		if (children == null) {
			if (other.children != null) return false;
		} else {
			if (children.size() != other.children?.size()) return false;
			
			for (int i = 0; i < children.size(); i++) {
				if (!children[i].equals(other.children[i])) return false;	
			}
		}
			
		if (isLeaf != other.isLeaf) return false;
		
		if (fullPath == null) {
			if (other.fullPath != null) return false;
		} else if (!fullPath.equals(other.fullPath))
			return false;
		
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (parent == null) {
			if (other.parent != null) return false;
		} else if (!parent.name.equals(other.parent.name))
			return false;
			
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		def ch = ""
		children.each {
			ch = ch + "name=" + it.name + ", status=" + it.status + "; "
		}
		
		return "NodeFile [name=" + name + 
		", status=" + status + 
		", isLeaf=" + isLeaf +
		", fullPath=" + fullPath +
		", children " + children?.size() + " = {" + ch + "} ]";
	}
	
	
}