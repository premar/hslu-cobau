grammar MiniJ;

@header {
package ch.hslu.cobau.minij;
}

// milestone 2: parser

unit : var ; // empty rule to make project compile

programm: PROCEDURE 'main' LBRACKET RBRACKET BEGINBODY instruction ENDBODY;

BEGINBODY:      BEGIN | LCUBRACKET;
ENDBODY:        END | RCUBRACKET;

PROGRAM:        'program';
BEGIN:          'begin';
PROCEDURE:      'procedure';
REF:            'ref';
RETURN:         'return';
END:            'end';

instruction:    var+;

var:            LITERAL IDENTIFIER SEM;

LITERAL:        INT | BOOLEAN | STRING;
IDENTIFIER:     LETTER+;

WS:             [ \t\r\n]+ -> skip;


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
SEM:            ';';

IF:             'if';
THEN:           'then';
WHILE:          'while';
DO:             'do';
ELSE:           'else';

//==========================================================
// TYPES
//==========================================================
RECORD:         'record';
INT:            'int';
BOOLEAN:        'boolean';
STRING:         'string';

DIGIT :         '0'..'9';
LETTER :        'a'..'z' | 'A'..'Z';