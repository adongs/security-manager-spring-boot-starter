package com.adongs.session.terminal;


import java.io.Serializable;


public class Terminal<T extends Serializable> extends AbstractTerminal{

    private final static ThreadLocal<Terminal> THREADLOCAL = new ThreadLocal<>();

    protected Terminal(Token token, TerminalFactory terminalFactory) {
        super(token, terminalFactory);
    }

    @Override
    public void logout() {
        THREADLOCAL.remove();
        super.logout();
    }

    protected static void set(Terminal terminal){
        THREADLOCAL.remove();
        THREADLOCAL.set(terminal);
    }

    public static Terminal get(){
        return THREADLOCAL.get();
    }

}
