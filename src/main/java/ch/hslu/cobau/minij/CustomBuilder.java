package ch.hslu.cobau.minij;

import ch.hslu.cobau.minij.ast.entity.Declaration;
import ch.hslu.cobau.minij.ast.entity.Procedure;
import ch.hslu.cobau.minij.ast.entity.Program;
import ch.hslu.cobau.minij.ast.entity.RecordStructure;

import java.util.LinkedList;
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
        return visitChildren(ctx);
    }


}
