package com.ajaxjs.developertools.tools.mysql.tools;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 格式化 SQL
 *
 * @author Frank Cheung<sp42@qq.com>
 */
public class BasicFormatterImpl {
    private static final Set<String> BEGIN_CLAUSES = new HashSet<>();
    private static final Set<String> END_CLAUSES = new HashSet<>();
    private static final Set<String> LOGICAL = new HashSet<>();
    private static final Set<String> QUANTIFIERS = new HashSet<>();
    private static final Set<String> DML = new HashSet<>();
    private static final Set<String> MISC = new HashSet<>();
    static final String indentString = "    ";
    static final String initial = "\n    ";

    public String format(String source) {
        return new FormatProcess(source).perform().trim();
    }

    static {
        BEGIN_CLAUSES.add("left");
        BEGIN_CLAUSES.add("right");
        BEGIN_CLAUSES.add("inner");
        BEGIN_CLAUSES.add("outer");
        BEGIN_CLAUSES.add("group");
        BEGIN_CLAUSES.add("order");

        END_CLAUSES.add("where");
        END_CLAUSES.add("set");
        END_CLAUSES.add("having");
//		END_CLAUSES.add("join");
        END_CLAUSES.add("from");
        END_CLAUSES.add("by");
        END_CLAUSES.add("join");
        END_CLAUSES.add("into");
        END_CLAUSES.add("union");

        LOGICAL.add("and");
        LOGICAL.add("or");
        LOGICAL.add("when");
        LOGICAL.add("else");
        LOGICAL.add("end");

        QUANTIFIERS.add("in");
        QUANTIFIERS.add("all");
        QUANTIFIERS.add("exists");
        QUANTIFIERS.add("some");
        QUANTIFIERS.add("any");

        DML.add("insert");
        DML.add("update");
        DML.add("delete");

        MISC.add("select");
        MISC.add("on");
    }

    private static class FormatProcess {
        boolean beginLine = true;
        boolean afterBeginBeforeEnd = false;
        boolean afterByOrSetOrFromOrSelect = false;
        @SuppressWarnings("unused")
        boolean afterValues = false;
        boolean afterOn = false;
        boolean afterBetween = false;
        boolean afterInsert = false;
        int inFunction = 0;
        int parensSinceSelect = 0;
        private final LinkedList<Integer> parenCounts = new LinkedList<>();
        private final LinkedList<Boolean> afterByOrFromOrSelects = new LinkedList<>();

        int indent = 1;

        StringBuffer result = new StringBuffer();
        StringTokenizer tokens;
        String lastToken;
        String token;
        String lcToken;

        public FormatProcess(String sql) {
            tokens = new StringTokenizer(sql, "()+*/-=<>'`\"[], \n\r\f\t", true);
        }

        public String perform() {
            result.append("\n    ");

            while (tokens.hasMoreTokens()) {
                token = tokens.nextToken();
                lcToken = token.toLowerCase();

                if ("'".equals(token)) {
                    String t;
                    do {
                        t = tokens.nextToken();
                        token += t;
                    } while ((!"'".equals(t)) && (tokens.hasMoreTokens()));
                } else if ("\"".equals(token)) {
                    String t;

                    do {
                        t = tokens.nextToken();
                        token += t;
                    } while (!"\"".equals(t));
                }

                if ((afterByOrSetOrFromOrSelect) && (",".equals(token))) {
                    commaAfterByOrFromOrSelect();
                } else if ((afterOn) && (",".equals(token))) {
                    commaAfterOn();
                } else if ("(".equals(token)) {
                    openParen();
                } else if (")".equals(token)) {
                    closeParen();
                } else if (BasicFormatterImpl.BEGIN_CLAUSES.contains(lcToken)) {
                    beginNewClause();
                } else if (BasicFormatterImpl.END_CLAUSES.contains(lcToken)) {
                    endNewClause();
                } else if ("select".equals(lcToken)) {
                    select();
                } else if (BasicFormatterImpl.DML.contains(lcToken)) {
                    updateOrInsertOrDelete();
                } else if ("values".equals(lcToken)) {
                    values();
                } else if ("on".equals(lcToken)) {
                    on();
                } else if ((afterBetween) && (lcToken.equals("and"))) {
                    misc();
                    afterBetween = false;
                } else if (BasicFormatterImpl.LOGICAL.contains(lcToken)) {
                    logical();
                } else if (isWhitespace(token)) {
                    white();
                } else {
                    misc();
                }

                if (!isWhitespace(token))
                    lastToken = lcToken;

            }

            return result.toString();
        }

        private void commaAfterOn() {
            out();
            indent -= 1;
            newline();
            afterOn = false;
            afterByOrSetOrFromOrSelect = true;
        }

        private void commaAfterByOrFromOrSelect() {
            out();
            newline();
        }

        private void logical() {
            if ("end".equals(lcToken))
                indent -= 1;

            newline();
            out();
            beginLine = false;
        }

        private void on() {
            indent += 1;
            afterOn = true;
            newline();
            out();
            beginLine = false;
        }

        private void misc() {
            out();
            if ("between".equals(lcToken))
                afterBetween = true;

            if (afterInsert) {
                newline();
                afterInsert = false;
            } else {
                beginLine = false;

                if ("case".equals(lcToken))
                    indent += 1;
            }
        }

        private void white() {
            if (!beginLine)
                result.append(" ");
        }

        private void updateOrInsertOrDelete() {
            out();
            indent += 1;
            beginLine = false;

            if ("update".equals(lcToken))
                newline();

            if ("insert".equals(lcToken))
                afterInsert = true;
        }

        private void select() {
            out();
            indent += 1;
            newline();
            parenCounts.addLast(parensSinceSelect);
            afterByOrFromOrSelects.addLast(afterByOrSetOrFromOrSelect);
            parensSinceSelect = 0;
            afterByOrSetOrFromOrSelect = true;
        }

        private void out() {
            result.append(token);
        }

        private void endNewClause() {
            if (!afterBeginBeforeEnd) {
                indent -= 1;

                if (afterOn) {
                    indent -= 1;
                    afterOn = false;
                }

                newline();
            }
            out();
            if (!"union".equals(lcToken))
                indent += 1;

            newline();
            afterBeginBeforeEnd = false;
            afterByOrSetOrFromOrSelect = (("by".equals(lcToken)) || ("set".equals(lcToken)) || ("from".equals(lcToken)));
        }

        private void beginNewClause() {
            if (!afterBeginBeforeEnd) {
                if (afterOn) {
                    indent -= 1;
                    afterOn = false;
                }
                indent -= 1;
                newline();
            }

            out();
            beginLine = false;
            afterBeginBeforeEnd = true;
        }

        private void values() {
            indent -= 1;
            newline();
            out();
            indent += 1;
            newline();
            afterValues = true;
        }

        private void closeParen() {
            parensSinceSelect -= 1;
            if (parensSinceSelect < 0) {
                indent -= 1;
                parensSinceSelect = parenCounts.removeLast();
                afterByOrSetOrFromOrSelect = afterByOrFromOrSelects.removeLast();
            }

            if (inFunction > 0) {
                inFunction -= 1;
                out();
            } else {
                if (!afterByOrSetOrFromOrSelect) {
                    indent -= 1;
                    newline();
                }
                out();
            }

            beginLine = false;
        }

        private void openParen() {
            if ((isFunctionName(lastToken)) || (inFunction > 0))
                inFunction += 1;

            beginLine = false;

            if (inFunction > 0)
                out();
            else {
                out();
                if (!afterByOrSetOrFromOrSelect) {
                    indent += 1;
                    newline();
                    beginLine = true;
                }
            }

            parensSinceSelect += 1;
        }

        private static boolean isFunctionName(String tok) {
            char begin = tok.charAt(0);
            boolean isIdentifier = (Character.isJavaIdentifierStart(begin)) || ('"' == begin);
            return (isIdentifier) && (!BasicFormatterImpl.LOGICAL.contains(tok)) && (!BasicFormatterImpl.END_CLAUSES.contains(tok))

                    && (!BasicFormatterImpl.QUANTIFIERS.contains(tok)) && (!BasicFormatterImpl.DML.contains(tok)) && (!BasicFormatterImpl.MISC.contains(tok));
        }

        private static boolean isWhitespace(String token) {
            return " \n\r\f\t".contains(token);
        }

        private void newline() {
            result.append("\n");

            for (int i = 0; i < indent; i++)
                result.append("    ");

            beginLine = true;
        }
    }
}