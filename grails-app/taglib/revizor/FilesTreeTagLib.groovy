package revizor

import com.revizor.Comment
import com.revizor.CommentType
import com.revizor.Review
import com.revizor.utils.Constants

/**
 * TagLib helps to build a list of modified files formed in a tree.
 *
 * There is a list of modified files in each review on the left. As long as
 * files are packed in packages, that represents directories, it is a good idea
 * to print them as a tree. Result should look like this:
 *
 * com
 *   ↳ revizor
 *          ↳ service
 *               ↳ ServiceA.groovy
 *                 ServiceB.groovy
 *          ↳ domain
 *               ↳ DomainA.groovy
 *
 *  This TagLib helps to print all the modified files in this way.
 *
 */
class FilesTreeTagLib {

    static namespace = "ft"
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
            def diffEntities = DiffTagLib.getDiffLines(attrs.repo, attrs.commitID);
            def reviewId = attrs.reviewId

            def fileNames = [] as Set
            diffEntities.each { file ->

                fileNames.addAll(file.findAll{
                    it.startsWith("---") || it.startsWith('+++')
                })

            }

            NodeFile root = this.buildMapOfFiles(fileNames);
            def currentReviewMode = Constants.REVIEW_SINGLE_VIEW .equals(params["viewType"]) ? Constants.REVIEW_SINGLE_VIEW : Constants.REVIEW_SIDE_BY_SIDE_VIEW
            def hrefFileDetailsBase = attrs.urlPrefix + "/" + currentReviewMode + "?" + Constants.PARAM_FILE_NAME + '='

            // print fileNames
            out << '<div class="list-group">'

            def mapCommentsToFile = [:]
            if (reviewId) {
                def review = Review.get(reviewId)

                // build map "file name" <=> "comments for this file"
                mapCommentsToFile = Comment.findAllByReviewAndType(review, CommentType.LINE_OF_CODE)
                        .groupBy { Comment comment -> comment.getFileName() }

            }

            _recursivelyPrintTree(root, 0, hrefFileDetailsBase, mapCommentsToFile)

            out << '</div>'

        }
    }

    def _recursivelyPrintTree(node, level, hrefFileDetailsBase, Map<String, Comment> mapCommentsToFile) {
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

            def countOfComments = ""
            if (mapCommentsToFile.containsKey(node.fullPath)) {
                def cnt = mapCommentsToFile.get(node.fullPath).size()
                countOfComments = "<span title='${message(code: 'comments.in.file', args: [cnt])}'>(${cnt})</span>"
            }

            def selectedNodeCSSstyle = (params.fileName == node.fullPath) ? "selected" : ""

            out << """
					<div class='tree-leaf' style='margin-left: ${level * INDENT_TREE}px;'>
						<span class='${styleFileStatus} ${selectedNodeCSSstyle}'>
							<a href="${hrefFileDetailsBase + node.fullPath}">
								${node.name}
							</a>
                            ${countOfComments}
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
                _recursivelyPrintTree(child, level + 1, hrefFileDetailsBase, mapCommentsToFile)
            }
        }
    }

    /**
     * Builds tree of files that will be used to build a graphical tree
     * It should modify path to a nested maps. For example,
     *
     * 	--- a/src/package/c.groovy
     *
     * should produce the tree (NodeFile objects):
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