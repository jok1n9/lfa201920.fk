grammar JSON;

@header{ package Json; }

json: 	object

     ;

object:
	'{' pair(',' pair)* '}'
	| '{' '}'
	;

pair: '"' STRING '"' ':' value 			#TemaD
	| '"' STRING '"' '=' value 			#Resp
	|	STRING '-' '"' STRING '"' ':' value   #QuestionID
	;


value:
	STRING	 	#ValueString
	| object  	#ValueObject
	| 'true'   	#ValueTrue
	| 'false'	#ValueFalse
	;

fragment ID: [a-zA-ZáàâãéêíóôõúüçÀÁÂÃÉÊÍÓÔÕÚÜÇ?.!];
fragment INT: [0-9];
STRING: ((ID+|INT+)+|(ID+|INT+)+ (' ')+| (' ')+ (ID+|INT+)+ )+ ;
NUMBER:INT+;

WS : [\t\n\r]+ -> skip;
WHITESPACE: ' ' -> skip;

