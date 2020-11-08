package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.constants.StringConstant;
import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.entity.Program;
import ch.hslu.cobau.minij.ast.entity.RecordStructure;
import ch.hslu.cobau.minij.ast.expression.Expression;
import ch.hslu.cobau.minij.ast.expression.UnaryExpression;
import ch.hslu.cobau.minij.ast.expression.UnaryOperator;
import ch.hslu.cobau.minij.ast.statement.*;
import ch.hslu.cobau.minij.ast.type.BooleanType;
import ch.hslu.cobau.minij.ast.type.IntegerType;
import ch.hslu.cobau.minij.ast.type.StringType;
import ch.hslu.cobau.minij.ast.type.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CustomBuilder extends MiniJBaseVisitor<Object> {
    private final Stack<Object> globalsStack = new Stack<>();
    private final Stack<Object> proceduresStack = new Stack<>();
    private final Stack<Object> recordStructuresStack = new Stack<>();
    private final Stack<Object> stack = new Stack<>();

    @Override
    public Object visitUnit(MiniJParser.UnitContext ctx) {
        visitChildren(ctx);

        LinkedList<Declaration> globals = new LinkedList<>();
        LinkedList<Procedure> procedures = new LinkedList<>();
        LinkedList<RecordStructure> recordStructures = new LinkedList<>();

        while (!globalsStack.empty()) {
            globals.addFirst((Declaration) globalsStack.pop());
        }

        while (!proceduresStack.empty()) {
            procedures.addFirst((Procedure) proceduresStack.pop());
        }

        while (!recordStructuresStack.empty()) {
            recordStructures.addFirst((RecordStructure) recordStructuresStack.pop());
        }

        return new Program(globals, procedures, recordStructures);
    }

    @Override
    public Object visitDeclarations(MiniJParser.DeclarationsContext ctx) {
        visitDeclarations(ctx);
        return null;
    }

    @Override
    public Object visitDeclaration(MiniJParser.DeclarationContext ctx) {
        visitChildren(ctx);
        var type = (Type) stack.pop();
        var identifier = (String) stack.pop();
        globalsStack.push(new Declaration(identifier, type));
        return null;
    }

    @Override
    public Object visitMember(MiniJParser.MemberContext ctx) {
        visitMember(ctx);
        return null;
    }

    @Override
    public Object visitRecord(MiniJParser.RecordContext ctx) {
        visitRecord(ctx);
        var identifier = (String) stack.pop();

        LinkedList<Declaration> declarations = new LinkedList<>();
        while(stack.peek().getClass() == Declaration.class) {
            declarations.addFirst((Declaration) stack.pop());
        }

        stack.push(new RecordStructure(identifier, declarations));
        return null;
    }

    @Override
    public Object visitProcedure(MiniJParser.ProcedureContext ctx) {
        visitProcedure(ctx);
        var identifier = (String) stack.pop();

        LinkedList<Declaration> declarations = new LinkedList<>();
        while(stack.peek().getClass() == Declaration.class) {
            declarations.addFirst((Declaration) stack.pop());
        }

        LinkedList<Statement> statements = new LinkedList<>();
        while(stack.peek().getClass() == Statement.class) {
            statements.addFirst((Statement) stack.pop());
        }

        stack.push(new Procedure(identifier, declarations, statements));
        return null;
    }

    @Override
    public Object visitParameter(MiniJParser.ParameterContext ctx) {
        visitParameter(ctx);
        return null;
    }

    @Override
    public Object visitProcedureBody(MiniJParser.ProcedureBodyContext ctx) {
        visitProcedureBody(ctx);
        return null;
    }

    @Override
    public Object visitBlock(MiniJParser.BlockContext ctx) {
        visitBlock(ctx);
        LinkedList<Statement> statements = new LinkedList<>();
        while(stack.peek().getClass() == Statement.class) {
            statements.addFirst((Statement) stack.pop());
        }
        stack.push(new Block(statements));
        return null;
    }

    @Override
    public Object visitDeclarationStatement(MiniJParser.DeclarationStatementContext ctx) {
        visitDeclarationStatement(ctx);
        stack.push(new DeclarationStatement((Declaration) stack.pop()));
        return null;
    }

    @Override
    public Object visitStatement(MiniJParser.StatementContext ctx) {
        visitStatement(ctx);
        return null;
    }

    @Override
    public Object visitAssignment(MiniJParser.AssignmentContext ctx) {
        visitAssignment(ctx);
        var right = (Expression) stack.pop();
        var left = (Expression) stack.pop();
        stack.push(new AssignmentStatement(left, right));
        return null;
    }

    @Override
    public Object visitCallStatement(MiniJParser.CallStatementContext ctx) {
        visitCallStatement(ctx);
        var identifier = (String) stack.pop();

        LinkedList<Expression> expressions = new LinkedList<>();
        while(stack.peek().getClass() == Expression.class) {
            expressions.addFirst((Expression) stack.pop());
        }
        stack.push(new CallStatement(identifier, expressions));
        return null;
    }

    @Override
    public Object visitWhileStatement(MiniJParser.WhileStatementContext ctx) {
        visitWhileStatement(ctx);
        var expression = (Expression) stack.pop();
        var statements = ((Block) stack.pop()).getStatements();
        stack.push(new WhileStatement(expression, statements));
        return null;
    }

    @Override
    public Object visitReturnStatement(MiniJParser.ReturnStatementContext ctx) {
        visitReturnStatement(ctx);
        stack.push(new ReturnStatement());
        return null;
    }

    @Override
    public Object visitIfStatement(MiniJParser.IfStatementContext ctx) {
        visitIfStatement(ctx);
        Block elseBlock = null;
        var expression = (Expression) stack.pop();
        var statements = ((Block) stack.pop()).getStatements();

        if(stack.peek().getClass() == Block.class)
        {
            elseBlock = ((Block) stack.pop());
        }

        stack.push(new IfStatement(expression, statements, elseBlock));
        return null;
    }

    @Override
    public Object visitElsifClause(MiniJParser.ElsifClauseContext ctx) {
        visitElsifClause(ctx);
        return null;
    }

    @Override
    public Object visitElseClause(MiniJParser.ElseClauseContext ctx) {
        visitElseClause(ctx);
        return null;
    }

    @Override
    public Object visitExpression(MiniJParser.ExpressionContext ctx) {
        visitExpression(ctx);
        //TODO Expression
        return null;
    }

    @Override
    public Object visitUnaryExpression(MiniJParser.UnaryExpressionContext ctx) {
        visitUnaryExpression(ctx);
        var expression = (Expression) stack.pop();
        var type = MiniJParser.VOCABULARY.getSymbolicName(ctx.unaryOp.getType());
        UnaryOperator operator = null;
        switch(type) {
            case "MINUS":
                operator = UnaryOperator.MINUS;
                break;
            case "NOT":
                operator = UnaryOperator.NOT;
                break;
            case "PLUS":
                operator = UnaryOperator.PLUS;
                break;
            case "INCREMENT":
                operator = UnaryOperator.POST_INCREMENT;
                break;
            case "DECREMENT":
                operator = UnaryOperator.POST_DECREMENT;
                break;
        }
        stack.push(new UnaryExpression(expression, operator));
        return null;
    }

    @Override
    public Object visitTrueConstant(MiniJParser.TrueConstantContext ctx) {
        visitTrueConstant(ctx);
        stack.push(new StringConstant(ctx.TRUE().getText()));
        return null;
    }

    @Override
    public Object visitFalseConstant(MiniJParser.FalseConstantContext ctx) {
        visitFalseConstant(ctx);
        stack.push(new StringConstant(ctx.FALSE().getText()));
        return null;
    }

    @Override
    public Object visitIntegerConstant(MiniJParser.IntegerConstantContext ctx) {
        visitIntegerConstant(ctx);
        stack.push(new StringConstant(ctx.INTEGER().getText()));
        return null;
    }

    @Override
    public Object visitStringConstant(MiniJParser.StringConstantContext ctx) {
        visitStringConstant(ctx);
        stack.push(new StringConstant(ctx.STRINGCONSTANT().getText()));
        return null;
    }

    @Override
    public Object visitMemoryAccess(MiniJParser.MemoryAccessContext ctx) {
        visitMemoryAccess(ctx);
        //TODO MemoryAccess
        return null;
    }

    @Override
    public Object visitType(MiniJParser.TypeContext ctx) {
        visitType(ctx);
        if(ctx.LBRACKET() != null) {
            //TODO Array
        }
        return null;
    }

    @Override
    public Object visitBasicType(MiniJParser.BasicTypeContext ctx) {
        visitBasicType(ctx);
        return null;
    }

    @Override
    public Object visitIntegerType(MiniJParser.IntegerTypeContext ctx) {
       visitIntegerType(ctx);
       stack.push(new IntegerType());
       return null;
    }

    @Override
    public Object visitStringType(MiniJParser.StringTypeContext ctx) {
        visitStringType(ctx);
        stack.push(new StringType());
        return null;
    }

    @Override
    public Object visitBooleanType(MiniJParser.BooleanTypeContext ctx) {
        visitBooleanType(ctx);
        stack.push(new BooleanType());
        return null;
    }

    @Override
    public Object visitRecordType(MiniJParser.RecordTypeContext ctx) {
        visitRecordType(ctx);
        return null;
    }

    @Override
    public Object visitIdentifier(MiniJParser.IdentifierContext ctx) {
        visitIdentifier(ctx);
        stack.push(ctx.ID().getText());
        return null;
    }
}
