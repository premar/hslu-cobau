grammar MiniJ;

@header {
package ch.hslu.cobau.minij;
}

program
    : instruction* EOF;

instruction
    : variable
    | record
    | branch
    | loop;

branch
    : IF LBRACKET condition RBRACKET THEN instruction*
    ((ELSEIF LBRACKET condition RBRACKET THEN instruction*)*)?
    ELSE instruction* END SEMICOLON;

loop
    : WHILE LBRACKET condition RBRACKET DO instruction* END SEMICOLON;

record
    : RECORD identifier variable* END SEMICOLON;

condition
    : identifier
    (OR | AND | EQUAL | NEQUAL | BAS | BOS | SAS | SOS)
    identifier;

variable
    : type (LEDBRACKET REDBRACKET)? identifier SEMICOLON;

type
    :	BOOLEAN
    |	INT
    |   STRING;

identifier
    :	('_'|LETTER) ('_'|LETTER|DIGIT)* ;

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
TRUE:           'true';
FALSE:          'false';
LENGTH:         'length';

DIGIT:          '0'..'9' ;
LETTER:         ('a'..'z'|'A'..'Z');

COMMENT:        (('//' ~('\n')*) | '/*' .*? '*/') -> skip;
WS:             [ \t\r\n]+ -> skip;