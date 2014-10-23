package com.revizor.utils;

/**
 * Created with IntelliJ IDEA.
 * User: ilja
 * Date: 21/03/14
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {

    boolean IS_TREE_LOG_ENABLED = true;

    String LOCAL_REPO_PATH = System.getProperty("user.home") + "/.repo";
    
    String REVIEW_SINGLE_VIEW = "single";
    String REVIEW_SIDE_BY_SIDE_VIEW = "side-by-side";
    
    byte ACTION_ADDED = 1;
    byte ACTION_DELETED = 2;
    byte ACTION_MODIFIED = 3;
    
    String PARAM_FILE_NAME = "fileName";
    int MAX_PER_REQUEST = 5;

    byte CURVE_VERTICAL = 1;      // "|"
    byte CURVE_SLASH = 2;         // "/"
    byte CURVE_BACK_SLASH = 3;    // "\"
    byte CURVE_VERTICAL_ACT = 4;      
    byte CURVE_SLASH_ACT = 5;         
    byte CURVE_BACK_SLASH_ACT = 6;
    byte CURVE_BLANK = 7;
    byte CURVE_MERGE_ACT = 8;
    byte CURVE_MERGE = 9;
    byte CURVE_ROOT = 10;
}