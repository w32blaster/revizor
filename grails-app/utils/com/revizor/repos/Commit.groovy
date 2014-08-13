package com.revizor.repos

/**
 * Abstract commit. 
 * A repository should return the list of commits using this class regardless
 * of repo type (Git, Mercurial...). It is used to to build graphs, 
 */
class Commit {
    
    // SHA code
    String id;
    
    // list of children and parent SHA's
    List<String> children;
    List<String> parents;

    String author
    String message

    // used to draw a graph
    int curveTypes
    String color
    String hoverText
}