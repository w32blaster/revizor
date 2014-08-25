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
    List<String> parents = new ArrayList<String>();

    String author
    String message

    // used to draw a graph
    // curves - the set of short line types, that will be drawin in current line to the parents
    List<Byte> curves = new ArrayList<Byte>();
    String labelText
}