grammar Question;

program:action* EOF;

action:(show|load|for1| difficulty|from|import1|shuffle| question|questionarray|definition|create|save|close|value|wait1|theme|cond) ;

question: 'question' STRING ';';

questionarray: 'question' '[' STRING ']' STRING ';';

load:  call '=' '{' ('"' STRING '"' ','*)* '}' ';'; 
for1: 'for' '(' STRING ';' (call) ')' '{' action* '}' # forcall
    | 'for' '(' STRING ';' (callarr) ')' '{' action* '}' # forcallarr
    ;

from: STRING '[' ']' '.' STRING '=' 'from'  STRING  'difficulty' STRING select #fromCallans
    | call '=' 'from'  STRING  'difficulty' STRING select #fromCall
    | call '=' 'from'  STRING  'select' 'answers' ';' #selectAnswer 
    ;

select: 'select'  STRING ';'  #selectOne
    | 'select'  (STRING |',')* ';'  #selectMultiple
    ;    
cond: 'if' '(' STRING ')' '{' action* '}' #condCall
    | 'if' '(' '!' STRING ')' '{' action* '}' #condFCall
    | 'else' '{' action* '}' #condElse
    ;
import1: 'import' '"' STRING* '.' STRING* '"' ';';

definition: STRING '=' '"' STRING* '"' ';'  #defString
            | call '=' '"' STRING* '"' ';'  #defCall
            ;

theme: call '.' STRING* '.' difficulty '=' STRING* ';';

value: call '=' STRING ';';

show: 'show' call ';' #showCall
    | 'show' callans ';' #showCallans
    | 'show' '"' STRING* '"' ';'     #showString
    ;
create: 'create' call  ';' #createCall
        |'create' callans ';' #createCallans
        |'create' '"' STRING* '"' ';' #createString
;

save:'save' call ';' #savecall
    |'save' callans ';' #savecallans
    |'save' '"' STRING* '"' ';' #saveString
    ;
close:'close' call ';'  #closeCall
    |'close' callans ';' #closeCallans
    |'close' '"' STRING* '"' ';' #closeString
;

difficulty: 'difficulty' STRING*;

shuffle: call '.' 'shuffle' ';';

wait1:  STRING '.wait' STRING 'input' ';'  #waitS
    |   callarr '.wait' STRING 'input' ';' #waitcallr
    ;

call: callarr '.' STRING #callArray
    | STRING '.' STRING  #callString
    ;

callarr: STRING '['']' #callarr1
    |   STRING '[' STRING ']' #callarr2
    ;

callans: call '(' STRING ')' ;


//lexer ------------------------------------
STRING: (ID+|INT+)+ ;
fragment ID: [a-zA-ZáàâãéêíóôõúüçÀÁÂÃÉÊÍÓÔÕÚÜÇ?!:];
fragment INT: [0-9];

WS : [\t\n\r]+ -> skip;
LINE_COMMENT	: '//' .*? '\n' -> skip;
COMMENT		: '/*' .*? '*/' -> skip;
WHITELINE: ' ' -> skip;
ERROr: . ; 
