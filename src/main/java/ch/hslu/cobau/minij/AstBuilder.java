package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.constants.*;
import ch.hslu.cobau.minij.ast.entity.*;
import ch.hslu.cobau.minij.ast.expression.*;
import ch.hslu.cobau.minij.ast.statement.*;
import ch.hslu.cobau.minij.ast.type.*;

import java.util.LinkedList;
import java.util.Stack;

public class AstBuilder extends MiniJBaseVisitor<Object> {
    private final Stack<Object> proceduresStack = new Stack<>();
    private final Stack<Object> recordStructuresStack = new Stack<>();
    private final Stack<Object> stack = new Stack<>();

    @Override
    public Object visitUnit(MiniJParser.UnitContext ctx) {
        super.visitChildren(ctx);
        LinkedList<Declaration> globals = new LinkedList<>();
        LinkedList<Procedure> procedures = new LinkedList<>();
        LinkedList<RecordStructure> recordStructures = new LinkedList<>();

        while (!stack.empty()) {
            globals.addFirst((Declaration) stack.pop());
        }
        while (!proceduresStack.empty()) {
            procedures.addFirst((Procedure) proceduresStack.pop());
        }
        while (!recordStructuresStack.empty()) {
            recordStructures.addFirst((RecordStructure) recordStructuresStack.pop());
        }

        for (int i = 1; i < globals.size(); i++) {
            if(globals.get(i-1).equals(globals.get(i))){
                throw new RuntimeException(globals.get(i).getIdentifier() + " was is globals a duplicate!");
            }
        }

        for (int i = 1; i < procedures.size(); i++) {
            if(procedures.get(i-1).equals(procedures.get(i))){
                throw new RuntimeException(procedures.get(i).getIdentifier() + " was procedures a duplicate!");
            }
        }

        for (int i = 1; i < recordStructures.size(); i++) {
            if(recordStructures.get(i-1).equals(recordStructures.get(i))){
                throw new RuntimeException(recordStructures.get(i).getIdentifier() + " was recordStructures a duplicate!");
            }
        }

        return new Program(globals, procedures, recordStructures);
    }

    @Override
    public Object visitDeclarations(MiniJParser.DeclarationsContext ctx) {
        super.visitDeclarations(ctx);
        return null;
    }

    @Override
    public Object visitDeclaration(MiniJParser.DeclarationContext ctx) {
        super.visitChildren(ctx);
        var identifier = (String) stack.pop();
        var type = (Type) stack.pop();
        stack.push(new Declaration(identifier, type));
        return null;
    }

    @Override
    public Object visitMember(MiniJParser.MemberContext ctx) {
        super.visitMember(ctx);
        return null;
    }

    @Override
    public Object visitRecord(MiniJParser.RecordContext ctx) {
        super.visitRecord(ctx);

        LinkedList<Declaration> declarations = new LinkedList<>();
        while(stack.peek().getClass() == Declaration.class) {
            declarations.addFirst((Declaration) stack.pop());
        }

        for (int i = 1; i < declarations.size(); i++) {
            if(declarations.get(i-1).equals(declarations.get(i))){
                throw new RuntimeException(declarations.get(i).getIdentifier() + " was declarations a duplicate!");
            }
        }

        var identifier = (String) stack.pop();

        recordStructuresStack.push(new RecordStructure(identifier, declarations));
        return null;
    }

    @Override
    public Object visitProcedure(MiniJParser.ProcedureContext ctx) {
        super.visitProcedure(ctx);

        LinkedList<Statement> blockStatements = new LinkedList<Statement>();
        LinkedList<Declaration> declarationsStatements = new LinkedList<Declaration>();
        LinkedList<Parameter> parameters = new LinkedList<Parameter>();

        if(stack.peek().getClass() == Block.class) {
            blockStatements = new LinkedList<Statement>(((Block) stack.pop()).getStatements());
        }

        while(stack.peek().getClass() == DeclarationStatement.class) {
            declarationsStatements.addFirst(((DeclarationStatement) stack.pop()).getDeclaration());
        }

        for (int i = 1; i < declarationsStatements.size(); i++) {
            if(declarationsStatements.get(i-1).equals(declarationsStatements.get(i))){
                throw new RuntimeException(declarationsStatements.get(i).getIdentifier() + " was declarationsStatements a duplicate!");
            }
        }

        while(stack.peek().getClass() == Parameter.class) {
            parameters.addFirst((Parameter) stack.pop());
        }

        for (int i = 1; i < parameters.size(); i++) {
            if(parameters.get(i-1).equals(parameters.get(i))){
                throw new RuntimeException(parameters.get(i).getIdentifier() + " was parameters a duplicate!");
            }
        }

        var identifier = (String) stack.pop();

        proceduresStack.push(new Procedure(identifier, parameters, declarationsStatements, blockStatements));
        return null;
    }

    @Override
    public Object visitParameter(MiniJParser.ParameterContext ctx) {
        super.visitParameter(ctx);
        var reference = false;
        if(ctx.REF() != null) {
            reference = true;
        }
        var identifier = (String) stack.pop();
        var type = (Type) stack.pop();
        stack.push(new Parameter(identifier, type, reference));
        return null;
    }

    @Override
    public Object visitProcedureBody(MiniJParser.ProcedureBodyContext ctx) {
        super.visitProcedureBody(ctx);
        return null;
    }

    @Override
    public Object visitBlock(MiniJParser.BlockContext ctx) {
        super.visitBlock(ctx);
        LinkedList<Statement> statements = new LinkedList<>();
        while(stack.peek().getClass().getSuperclass() == Statement.class && stack.peek().getClass() != DeclarationStatement.class) {
            statements.addFirst((Statement) stack.pop());
        }
        stack.push(new Block(statements));
        return null;
    }

    @Override
    public Object visitDeclarationStatement(MiniJParser.DeclarationStatementContext ctx) {
        super.visitDeclarationStatement(ctx);
        stack.push(new DeclarationStatement((Declaration) stack.pop()));
        return null;
    }

    @Override
    public Object visitStatement(MiniJParser.StatementContext ctx) {
        super.visitStatement(ctx);
        return null;
    }

    @Override
    public Object visitAssignment(MiniJParser.AssignmentContext ctx) {
        super.visitAssignment(ctx);
        var right = (Expression) stack.pop();
        var left = (Expression) stack.pop();
        stack.push(new AssignmentStatement(left, right));
        return null;
    }

    @Override
    public Object visitCallStatement(MiniJParser.CallStatementContext ctx) {
        super.visitCallStatement(ctx);

        LinkedList<Expression> expressions = new LinkedList<>();

        while(stack.peek().getClass().getSuperclass() == MemoryAccess.class ||
                stack.peek().getClass().getSuperclass() == Constant.class) {
            expressions.addFirst((Expression) stack.pop());
        }

        var identifier = (String) stack.pop();
        stack.push(new CallStatement(identifier, expressions));
        return null;
    }

    @Override
    public Object visitWhileStatement(MiniJParser.WhileStatementContext ctx) {
        super.visitWhileStatement(ctx);
        var expression = (Expression) stack.pop();
        var statements = ((Block) stack.pop()).getStatements();
        stack.push(new WhileStatement(expression, statements));
        return null;
    }

    @Override
    public Object visitReturnStatement(MiniJParser.ReturnStatementContext ctx) {
        super.visitReturnStatement(ctx);
        stack.push(new ReturnStatement());
        return null;
    }

    @Override
    public Object visitIfStatement(MiniJParser.IfStatementContext ctx) {
        super.visitIfStatement(ctx);
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
        super.visitElsifClause(ctx);
        return null;
    }

    @Override
    public Object visitElseClause(MiniJParser.ElseClauseContext ctx) {
        super.visitElseClause(ctx);
        return null;
    }

    @Override
    public Object visitExpression(MiniJParser.ExpressionContext ctx) {
        super.visitExpression(ctx);
        //TODO Expression
        return null;
    }

    @Override
    public Object visitUnaryExpression(MiniJParser.UnaryExpressionContext ctx) {
        super.visitUnaryExpression(ctx);
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
        super.visitTrueConstant(ctx);
        stack.push(new TrueConstant());
        return null;
    }

    @Override
    public Object visitFalseConstant(MiniJParser.FalseConstantContext ctx) {
        super.visitFalseConstant(ctx);
        stack.push(new FalseConstant());
        return null;
    }

    @Override
    public Object visitIntegerConstant(MiniJParser.IntegerConstantContext ctx) {
        super.visitIntegerConstant(ctx);
        stack.push(new IntegerConstant(Long.parseLong(ctx.INTEGER().getText())));
        return null;
    }

    @Override
    public Object visitStringConstant(MiniJParser.StringConstantContext ctx) {
        super.visitStringConstant(ctx);
        stack.push(new StringConstant(ctx.STRINGCONSTANT().getText()));
        return null;
    }

    @Override
    public Object visitMemoryAccess(MiniJParser.MemoryAccessContext ctx) {
        super.visitMemoryAccess(ctx);
        stack.push(new VariableAccess(ctx.ID().getText()));
        return null;
    }

    @Override
    public Object visitType(MiniJParser.TypeContext ctx) {
        super.visitType(ctx);
        if(ctx.LBRACKET() != null) {
            //TODO Array
        }
        return null;
    }

    @Override
    public Object visitBasicType(MiniJParser.BasicTypeContext ctx) {
        super.visitBasicType(ctx);
        return null;
    }

    @Override
    public Object visitIntegerType(MiniJParser.IntegerTypeContext ctx) {
       super.visitIntegerType(ctx);
       stack.push(new IntegerType());
       return null;
    }

    @Override
    public Object visitStringType(MiniJParser.StringTypeContext ctx) {
        super.visitStringType(ctx);
        stack.push(new StringType());
        return null;
    }

    @Override
    public Object visitBooleanType(MiniJParser.BooleanTypeContext ctx) {
        super.visitBooleanType(ctx);
        stack.push(new BooleanType());
        return null;
    }

    @Override
    public Object visitRecordType(MiniJParser.RecordTypeContext ctx) {
        super.visitRecordType(ctx);
        stack.push(new RecordType((String) stack.pop()));
        return null;
    }

    @Override
    public Object visitIdentifier(MiniJParser.IdentifierContext ctx) {
        super.visitIdentifier(ctx);
        stack.push(ctx.ID().getText());
        return null;
    }
}
