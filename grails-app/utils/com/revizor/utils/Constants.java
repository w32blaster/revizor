package com.revizor.utils;

/**
 * Created with IntelliJ IDEA.
 * User: ilja
 * Date: 21/03/14
 * Time: 11:07
 * To change this template use File | Settings | File Templates.
 */
public interface Constants {
    String LOCAL_REPO_PATH = System.getProperty("user.home") + "/repo";
    
    String REVIEW_SINGLE_VIEW = "single";
    String REVIEW_SIDE_BY_SIDE_VIEW = "side-by-side";
    
    byte ACTION_ADDED = 1;
    byte ACTION_DELETED = 2;
    byte ACTION_MODIFIED = 3;
    
    String PARAM_FILE_NAME = "fileName";
    int MAX_PER_REQUEST = 5;
}