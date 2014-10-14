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
    List<String> parents = [];
    List<String> children = [];

    String author
    String message

    // used to draw a graph
    // curves - the set of short line types, that will be drawn in current row. All the row will be combined to the graph
    List<Byte> curves = [];
    // which column (branch) current node is placed on
    byte currentCurveIdx

    String labelText
}