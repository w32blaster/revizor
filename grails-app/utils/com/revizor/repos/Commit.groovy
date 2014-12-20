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
    String authorEmail
    String message
    String fullMessage
    Date commitDate
    int padding; // in px

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}