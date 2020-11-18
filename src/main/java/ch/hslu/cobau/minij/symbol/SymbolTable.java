package ch.hslu.cobau.minij.symbol;

import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.type.Type;

import java.util.HashSet;

public class SymbolTable {

    public ProcedureSymbol currentProcedure;
    private HashSet<ProcedureSymbol> procedureSymbol = new HashSet<ProcedureSymbol>();
    private HashSet<DeclarationSymbol> declarationSymbols = new HashSet<DeclarationSymbol>();

    public void addProcedure(String identifier) {
        if(currentProcedure == null) {
            currentProcedure = new ProcedureSymbol(identifier);
        } else {
            if(procedureSymbol.contains(currentProcedure)) {
                throw new RuntimeException("Procedure was already declared");
            } else {
                procedureSymbol.add(currentProcedure);
                currentProcedure = new ProcedureSymbol(identifier);
            }
        }
    }

    public void addDeclaration(DeclarationSymbol declaration) {
        declarationSymbols.add(declaration);
    }

    public void checkAssignment(String identifier) {
        if(!checkGlobalAssignment(identifier) && !currentProcedure.checkAssignment(identifier)) {
            throw new RuntimeException("Assignment was not declared");
        }
    }

    private boolean checkGlobalAssignment(String identifier) {
        return declarationSymbols.contains(new DeclarationSymbol(identifier, null));
    }
}