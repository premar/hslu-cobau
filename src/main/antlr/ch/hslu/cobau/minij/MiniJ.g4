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
    | assign
    | call
    | (RETURN SEMICOLON)
    | (globalvariable SEMICOLON);

procedure
    : PROCEDURE IDENTIFIER LBRACKET parameter? RBRACKET
    (((globalvariable | variable) SEMICOLON)*)?
    (procedure_1 | procedure_2);

procedure_1:
    BEGIN
    (instruction*)?
    END SEMICOLON?;

procedure_2:
    LCUBRACKET
    (instruction*)?
    RCUBRACKET SEMICOLON?;

parameter
    : REF? (globalvariable | variable) ((COMMA REF? (globalvariable | variable))*)?;

// Globale Variable?
globalvariable
    : IDENTIFIER IDENTIFIER;

branch
    : IF LBRACKET condition RBRACKET THEN instruction*
    ((ELSEIF LBRACKET condition RBRACKET THEN instruction*)*)?
    (ELSE instruction*)? END SEMICOLON;

loop
    : WHILE LBRACKET condition RBRACKET DO instruction* END SEMICOLON;

record
    : RECORD IDENTIFIER ((variable | globalvariable) SEMICOLON)* END SEMICOLON?;

condition
    : (IDENTIFIER | VALUE | IDENTIFIER LEDBRACKET (VALUE | IDENTIFIER) REDBRACKET)
    (OR | AND | EQUAL | NEQUAL | BAS | BOS | SAS | SOS)
    (IDENTIFIER | VALUE | IDENTIFIER LEDBRACKET (VALUE | IDENTIFIER) REDBRACKET);

assign
    :   (IDENTIFIER | VALUE | (DOT IDENTIFIER (LEDBRACKET (VALUE | IDENTIFIER) REDBRACKET)?) | (IDENTIFIER (LEDBRACKET VALUE REDBRACKET)*)) ASSIGN (PLUS | MINUS)? assign SEMICOLON
    |   left=assign (TIMES|DIVIDED) right=assign
    |   left=assign (PLUS|MINUS) right=assign
    |   left=assign MODULO right=assign
    |   left=assign (OR | AND | EQUAL | NEQUAL | BAS | BOS | SAS | SOS) right=assign
    |   (INVERT | MINUS | PLUS | (DEC | INC))* (IDENTIFIER | VALUE | LBRACKET assign RBRACKET)
    |   (IDENTIFIER | VALUE) (DEC | INC)
    |   IDENTIFIER
    |   IDENTIFIER (LEDBRACKET (VALUE | IDENTIFIER) REDBRACKET)*
    |   IDENTIFIER DOT IDENTIFIER (LEDBRACKET VALUE REDBRACKET)*
    |   VALUE
    |   IDENTIFIER DOT IDENTIFIER
    |   LBRACKET assign RBRACKET;

assign_new
    :   (IDENTIFIER | VALUE) ASSIGN (PLUS | MINUS)? assign SEMICOLON
    |   left=assign (TIMES|DIVIDED) right=assign
    |   left=assign MODULO right=assign
    |   left=assign (PLUS|MINUS) right=assign
    |   LBRACKET assign RBRACKET
    |   (IDENTIFIER | VALUE);

call
    : IDENTIFIER LBRACKET assign? ((COMMA assign)*)? RBRACKET SEMICOLON;

variable
    : type ((LEDBRACKET REDBRACKET)*)? IDENTIFIER
    | LEDBRACKET REDBRACKET* IDENTIFIER
    | DOT IDENTIFIER;

variable_new
    : IDENTIFIER
    | IDENTIFIER DOT IDENTIFIER
    | type IDENTIFIER
    | type LEDBRACKET REDBRACKET IDENTIFIER;

type
    :	BOOLEAN
    |	INT
    |   STRING;

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
INVERT:         '!';
ASSIGN:         '=';
INC:            '++';
DEC:            '--';
DOT:            '.';

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