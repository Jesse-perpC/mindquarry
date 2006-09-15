package com.mindquarry.types.conversation;

public enum ConversationQuery {

    GetById ("ganz viel XQuery Zeugs");
    
    private String queryString;
    
    private ConversationQuery(String queryString) {
        this.queryString = queryString;
    }
}
