grammar MiniJ;

@header {
package ch.hslu.cobau.minij;
}

// milestone 2: parser

unit : ; // empty rule to make project compile 

PROGRAM:        'program';
BEGIN:          'begin';
PROCEDURE:      'procedure';
REF:            'ref';
RETURN:         'return';
END:            'end';

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






IDENTIFIER:     (DIGIT | LETTER);
DIGIT : [0-9] ;
LETTER : [a-z] [A-Z];