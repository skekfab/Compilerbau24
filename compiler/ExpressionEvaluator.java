package compiler;

public class ExpressionEvaluator {
    private Lexer m_lexer;
    
    public ExpressionEvaluator(Lexer lexer) {
        m_lexer = lexer;
    }
    
    public int eval(String val) throws Exception {
        m_lexer.init(val);
        return getQuestionMarkExpr();
    }
    
    int getParantheseExpr() throws Exception {
        // parentheseExpr : NUMBER | LParen sumExpr RParen

        Token token = m_lexer.lookAhead();
        // parentheseExpr : NUMBER
        if (m_lexer.accept(TokenIntf.Type.INTEGER)) { // consume NUMBER
            return Integer.parseInt(token.m_value);
        }
        else {
        // parentheseExpr : LParen sumExpr RParen
            m_lexer.expect(TokenIntf.Type.LPAREN); //consume Lparen
            int result = getPlusMinusExpr(); //sumExpr
            m_lexer.expect(TokenIntf.Type.RPAREN); //consume Rparen
            return result;
        }
    }
    
    int getArrowExpr() throws Exception {
        return getParantheseExpr();
    }

    int getDashExpr() throws Exception {
        return getArrowExpr();
    }

    int getUnaryExpr() throws Exception {
        return getDashExpr();
    }
    
    int getMulDivExpr() throws Exception {
        return getUnaryExpr();
    }
    
    int getPlusMinusExpr() throws Exception {
        // plusMinusExpr : mulDivExpr ((PLUS|MINUS) mulDivExpr)*
        int result = getMulDivExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.PLUS ||
            nextToken.m_type == TokenIntf.Type.MINUS) {
            m_lexer.advance(); // consume PLUS|MINUS
            int rhsOperand = getMulDivExpr();
            if (nextToken.m_type == TokenIntf.Type.PLUS) {
                result = result + rhsOperand;
            } else {
                result = result - rhsOperand;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    int getBitAndOrExpr() throws Exception {
        return getPlusMinusExpr();
    }

    int getShiftExpr() throws Exception {
        return getBitAndOrExpr();
    }

    int getCompareExpr() throws Exception {
        return getShiftExpr();
    }

    int getAndOrExpr() throws Exception {
        return getCompareExpr();
    }

    int getQuestionMarkExpr() throws Exception {
        return getAndOrExpr();
    }
}
