package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.BaseAstVisitor;
import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.entity.RecordStructure;
import ch.hslu.cobau.minij.ast.expression.VariableAccess;
import ch.hslu.cobau.minij.ast.statement.AssignmentStatement;
import ch.hslu.cobau.minij.ast.statement.DeclarationStatement;
import ch.hslu.cobau.minij.symbol.DeclarationSymbol;
import ch.hslu.cobau.minij.symbol.SymbolTable;

public class SymbolBuilder extends BaseAstVisitor {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(Procedure procedure) {
        symbolTable.addProcedure(procedure.getIdentifier());
        var list = procedure.getFormalDeclaration();
        var test = list.size();
        for (int i = 0; i < list.size(); i++) {
            symbolTable.currentProcedure.addDeclaration(new DeclarationSymbol(list.get(i).getIdentifier(), list.get(i).getType()));
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
