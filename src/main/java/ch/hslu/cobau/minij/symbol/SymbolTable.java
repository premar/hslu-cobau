package ch.hslu.cobau.minij.symbol;

import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Parameter;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.statement.Statement;
import ch.hslu.cobau.minij.ast.type.IntegerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {

    public ProcedureSymbol currentProcedure;

    private HashMap<ProcedureSymbol, ProcedureSymbol> procedureSymbol = new HashMap<ProcedureSymbol, ProcedureSymbol>();
    private HashMap<DeclarationSymbol, DeclarationSymbol> declarationSymbols = new HashMap<DeclarationSymbol, DeclarationSymbol>();

    public void addProcedure(String identifier) {
        if(currentProcedure == null) {

            currentProcedure = new ProcedureSymbol(identifier);
        } else {
            if(procedureSymbol.containsKey(currentProcedure)) {
                throw new RuntimeException("Procedure was already declared");
            } else {
                procedureSymbol.put(currentProcedure, currentProcedure);
                currentProcedure = new ProcedureSymbol(identifier);
            }
        }
    }

    public void addDeclaration(DeclarationSymbol declaration) {
        declarationSymbols.put(declaration, declaration);
    }

    public void checkAssignment(String identifier) {
        if(!checkGlobalAssignment(identifier) && !currentProcedure.checkAssignment(identifier)) {
            throw new RuntimeException("Assignment was not declared");
        }
    }

    public void checkCallStatement(String identifier, int paramCount) {
        if(!procedureSymbol.containsKey(new ProcedureSymbol(identifier))) {
            throw new RuntimeException("Procedure was not declared");
        } else {
            var toCheck = procedureSymbol.get(new ProcedureSymbol(identifier));
            var count = toCheck.parameters.size();
            if(count != paramCount) {
                throw new RuntimeException("Parameter dont equals.");
            }
        }
    }

    private boolean checkGlobalAssignment(String identifier) {
        return declarationSymbols.containsKey(new DeclarationSymbol(identifier, null));
    }
}