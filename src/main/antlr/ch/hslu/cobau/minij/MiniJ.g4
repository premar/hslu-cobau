grammar MiniJ;

@header {
package ch.hslu.cobau.minij;
}

unit
    : program;

program
    : (procedure | instruction)* EOF;

instruction
    : (variable SEMICOLON)
    | record
    | branch
    | loop
    //| assign
    | test2
    | (globalvariable SEMICOLON);

procedure
    : PROCEDURE IDENTIFIER LBRACKET parameter? RBRACKET
    ((variable SEMICOLON)*)?
    open
    ((instruction*)? | (RETURN SEMICOLON)?)? | (call*)?
    close SEMICOLON?;

parameter
    : REF? (globalvariable | variable) ((COMMA (globalvariable | variable))*)?;

// Globale Variable?
globalvariable
    : IDENTIFIER IDENTIFIER;

branch
    : IF LBRACKET condition RBRACKET THEN instruction*
    ((ELSEIF LBRACKET condition RBRACKET THEN instruction*)*)?
    ELSE instruction* END SEMICOLON;

loop
    : WHILE LBRACKET condition RBRACKET DO instruction* END SEMICOLON;

record
    : RECORD IDENTIFIER (variable SEMICOLON)* END SEMICOLON;

condition
    : IDENTIFIER
    (OR | AND | EQUAL | NEQUAL | BAS | BOS | SAS | SOS)
    IDENTIFIER;

test2
    : IDENTIFIER '='
    LBRACKET*? test RBRACKET*?;

test
    : (VALUE | IDENTIFIER)? (PLUS | MINUS | DIVIDED | TIMES | MODULO) (VALUE | IDENTIFIER | test);

// TODO
assign
    : IDENTIFIER '='
    LBRACKET*? (((VALUE | IDENTIFIER) | (VALUE | IDENTIFIER) (PLUS | MINUS | DIVIDED | TIMES | MODULO) (VALUE | IDENTIFIER))) RBRACKET*? SEMICOLON;

call
    : IDENTIFIER LBRACKET IDENTIFIER? ((COMMA IDENTIFIER)*)? RBRACKET SEMICOLON;

variable
    : type (LEDBRACKET REDBRACKET)? IDENTIFIER;

type
    :	BOOLEAN
    |	INT
    |   STRING;

open
    : BEGIN | LCUBRACKET;

close
    : END | RCUBRACKET;

//==========================================================
// Operatoren
//==========================================================
MODULO:         '%';
DIVIDED:        '/';
TIMES:          '*';
PLUS:           '+';
MINUS:          '-';
LBRACKET:       '(';
RBRACKET:       ')';
LEDBRACKET:     '[';
REDBRACKET:     ']';
LCUBRACKET:     '{';
RCUBRACKET:     '}';
OR:             '||';
AND:            '&&';
EQUAL:          '==';
NEQUAL:         '!=';
BAS:            '>';
BOS:            '>=';
SAS:            '<';
SOS:            '<=';
SEMICOLON:      ';';
COMMA:          ',';
QUOTATIONMARK:  '"';

//==========================================================
// Reserved Keywords
//==========================================================
RECORD:         'record';
INT:            'int';
BOOLEAN:        'boolean';
STRING:         'string';
IF:             'if';
ELSE:           'else';
THEN:           'then';
WHILE:          'while';
DO:             'do';
REF:            'ref';
RETURN:         'return';
BEGIN:          'begin';
END:            'end';
ELSEIF:         'elsif';

LENGTH:         'length';
PROCEDURE:      'procedure';

COMMENT:        (('//' ~('\n')*) | '/*' .*? '*/') -> skip;
WS:             [ \t\r\n]+ -> skip;

VALUE:          BVALUE | NVALUE | SVALUE;
NVALUE:         (PLUS | MINUS)? DIGIT+;
BVALUE:         TRUE | FALSE;
SVALUE:         QUOTATIONMARK ANYCHAR* QUOTATIONMARK;
IDENTIFIER:     ('_'|LETTER)+ ('_'|LETTER|DIGIT)*;

DIGIT:          '0'..'9' ;
LETTER:         ('a'..'z'|'A'..'Z');
ANYCHAR:        . ;
TRUE:           'true';
FALSE:          'false';