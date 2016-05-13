package com.ajaxjs.net.ftp.sun.misc;

import java.io.PrintStream;

class RegexpNode {
    char c;
    RegexpNode firstchild;
    RegexpNode nextsibling;
    int depth;
    boolean exact;
    Object result;
    String re = null;

    RegexpNode () {
        c = '#';
        depth = 0;
    }
    RegexpNode (char C, int depth) {
        c = C;
        this.depth = depth;
    }
    RegexpNode add(char C) {
        RegexpNode p = firstchild;
        if (p == null)
            p = new RegexpNode (C, depth+1);
        else {
            while (p != null)
                if (p.c == C)
                    return p;
                else
                    p = p.nextsibling;
            p = new RegexpNode (C, depth+1);
            p.nextsibling = firstchild;
        }
        firstchild = p;
        return p;
    }
    RegexpNode find(char C) {
        for (RegexpNode p = firstchild;
                p != null;
                p = p.nextsibling)
            if (p.c == C)
                return p;
        return null;
    }
    void print(PrintStream out) {
        if (nextsibling != null) {
            RegexpNode p = this;
            out.print("(");
            while (p != null) {
                out.write(p.c);
                if (p.firstchild != null)
                    p.firstchild.print(out);
                p = p.nextsibling;
                out.write(p != null ? '|' : ')');
            }
        } else {
            out.write(c);
            if (firstchild != null)
                firstchild.print(out);
        }
    }
}