package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.BaseAstVisitor;
import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.entity.RecordStructure;
import ch.hslu.cobau.minij.ast.expression.VariableAccess;
import ch.hslu.cobau.minij.ast.statement.AssignmentStatement;
import ch.hslu.cobau.minij.ast.statement.CallStatement;
import ch.hslu.cobau.minij.ast.statement.DeclarationStatement;
import ch.hslu.cobau.minij.symbol.DeclarationSymbol;
import ch.hslu.cobau.minij.symbol.ParameterSymbol;
import ch.hslu.cobau.minij.symbol.SymbolTable;

public class SymbolBuilder extends BaseAstVisitor {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(Procedure procedure) {

        if(procedure.getIdentifier().contains("main")) {
            var parameter = procedure.getFormalParameter();
            if(parameter.size() > 0) {
                throw new RuntimeException("Main cannot have parameter");
            }
        }

        symbolTable.addProcedure(procedure.getIdentifier());

        var declarationList = procedure.getFormalDeclaration();
        for (int i = 0; i < declarationList.size(); i++) {
            symbolTable.currentProcedure.addDeclaration(new DeclarationSymbol(declarationList.get(i).getIdentifier(), declarationList.get(i).getType()));
        }

        var parameterList = procedure.getFormalParameter();
        for (int i = 0; i < parameterList.size(); i++) {
            symbolTable.currentProcedure.addParameter(new ParameterSymbol(parameterList.get(i).getIdentifier(), parameterList.get(i).getType()));
        }

        super.visit(procedure);
    }

    @Override
    public void visit(RecordStructure recordStructure) {
        super.visit(recordStructure);
    }

    @Override
    public void visit(VariableAccess variable) {
        symbolTable.checkAssignment(variable.getIdentifier());
        super.visit(variable);
    }

    @Override
    public void visit(CallStatement callStatement) {
        symbolTable.checkCallStatement(callStatement.getIdentifier(), callStatement.getParameters().size());
        super.visit(callStatement);
    }

    @Override
    public void visit(Declaration declaration) {
        if(symbolTable.currentProcedure == null) {
            symbolTable.addDeclaration(new DeclarationSymbol(declaration.getIdentifier(), declaration.getType()));
        } else {
            symbolTable.currentProcedure.addDeclaration(new DeclarationSymbol(declaration.getIdentifier(), declaration.getType()));
        }
        super.visit(declaration);
    }

    @Override
    public void visit(AssignmentStatement assignment) {
        super.visit(assignment);
    }
}
