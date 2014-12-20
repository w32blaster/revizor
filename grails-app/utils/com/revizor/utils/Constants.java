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

    String REVIZOR_ROOT = System.getProperty("user.home") + "/.revizor";
    String LOCAL_REPO_PATH = REVIZOR_ROOT + "/repo";
    String LOCAL_DB_PATH = REVIZOR_ROOT + "/db";

    String REVIEW_SINGLE_VIEW = "single";
    String REVIEW_SIDE_BY_SIDE_VIEW = "side-by-side";
    
    byte ACTION_ADDED = 1;
    byte ACTION_DELETED = 2;
    byte ACTION_MODIFIED = 3;
    
    String PARAM_FILE_NAME = "fileName";
    int MAX_PER_REQUEST = 10;

    int ROW_HEIGHT = 35; // in px
    String SPACE = " ";

    String SMART_COMMIT_CREATE_REVIEW = "+review";

    char USERNAME_DELIMETER = '~';
}