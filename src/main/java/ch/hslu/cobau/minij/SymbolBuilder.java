package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.BaseAstVisitor;
import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.entity.RecordStructure;
import ch.hslu.cobau.minij.ast.expression.VariableAccess;
import ch.hslu.cobau.minij.ast.statement.AssignmentStatement;
import ch.hslu.cobau.minij.symbol.DeclarationSymbol;
import ch.hslu.cobau.minij.symbol.SymbolTable;

public class SymbolBuilder extends BaseAstVisitor {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public void visit(Procedure procedure) {
        symbolTable.addProcedure(procedure.getIdentifier());
        super.visit(procedure);
    }

    @Override
    public void visit(RecordStructure recordStructure) {
        super.visit(recordStructure);
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
        if(symbolTable.currentProcedure == null) {
            throw new RuntimeException("Error");
        } else {
            if(assignment.getLeft().getClass() == VariableAccess.class) {
            }
        }
        super.visit(assignment);
    }
}
