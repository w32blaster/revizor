package revizor

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * 
 */
@TestFor(DiffTagLib)
class DiffTagLibUnitSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	
	def "The file name is correctly extracted from the very first line" () {
		when:
			def fileName = tagLib.extractFileName(fileDiffLine)
		then:
			fileName == expectedFileName
		where:
			fileDiffLine                                                                                                               | expectedFileName
			'diff --git a/grails-app/utils/com/sticky/capes/utils/Paths.java b/grails-app/utils/com/sticky/capes/utils/Paths.java'     | 'Paths.java'
			'diff a/grails-app/utils/com/sticky/capes/utils/Paths.java b/grails-app/utils/com/sticky/capes/utils/Paths.java'           | 'Paths.java'
			'diff -r d099c944477c -r 83d608559a70 Thinking_In_Java/thinking-in-java.adoc'                                              | 'thinking-in-java.adoc'
	}
	
	def "The line numbers are correctly extracted from the range header" () {
		when:
			def lineRange = tagLib.extractRange(line)
		
		then: 'the first number with - is range for original file, + is for new file'
			lineRange.original == sinceOriginal
			lineRange.new == sinceNew
			
		where:
			line																| sinceOriginal	| sinceNew
			"@@ -64,8 +64,8 @@ class GitRepository implements IRepository {" 	| 64			| 64
			"@@ -215,13 +230,22 @@ class DiffTagLib {" 							| 215			| 230
	}
}
