package com.revizor.utils;

/**
 * Created with IntelliJ IDEA.
 * User: ilja
 * Date: 21/03/14
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    
    // function for debugging purposes: prints graph to the console
    static printTree(commits) {
        commits.reverseEach { commit ->
            
            def line1 = ""
            def line2 = ""
            //println "commit ${commit.id} has curves ${commit.curves.size()}: ${commit.curves}"
            if (commit.curves.size() == 0) {
                // this is root
                line1 += " " + commit.id
            }
            else {
                commit.curves.each { curve ->
                    switch (curve) {
                        case Constants.CURVE_VERTICAL:
                            line1 += " |"
                            line2 += " |"
                            break;

                        case Constants.CURVE_SLASH:
                            line1 += " |"
                            line2 += "/ "
                            break;

                        case Constants.CURVE_BACK_SLASH:
                            line1 += "  \\"
                            line2 += " |"
                            break;

                        case Constants.CURVE_VERTICAL_ACT:
                            line1 += " " + commit.id
                            line2 += " |"
                            break;

                        case Constants.CURVE_SLASH_ACT:
                            line1 += " " + commit.id
                            line2 += "/ "
                            break;

                        case Constants.CURVE_BACK_SLASH_ACT:
                            line1 += " " + commit.id
                            line2 += "  \\"
                            break;

                        case Constants.CURVE_BLANK:
                            line1 += "  "
                            line2 += "  "
                            break;
                        default:
                            line1 += " *"
                            line2 += " *"
                            break;                        
                    }
                }
            }
            
            println line1
            println line2
        }
        println ""
    }

}