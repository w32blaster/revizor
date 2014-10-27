package com.revizor.repos

/**
 * Abstract commit. 
 * A repository should return the list of commits using this class regardless
 * of repo type (Git, Mercurial...). It is used to to build graphs, 
 */
class Commit {
    
    // SHA code
    String id
    String svg
    String author
    String message
    int padding; // in px
}