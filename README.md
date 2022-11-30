# CoLang

TODO:
- Formalize grammar for the language
- Code input options --> load file and multiple line input from console (currently single)
- Token generator needs pattern matching (regex)... abstract from string references to good token descriptions... token visitor for syntax tree
- Types (duck for the moment, only string and int recognizable)
- Expressions only support E => (id | value) + (id | value).. needs to be able to parse expressions on either side aswell... so E => E | E BinOp E | Id | Value
- Condition statements should use expressions that evaluate to booleans (only supports (id | value) == (id | value))... need comparison operators 
- Parsing for else statements

FUTURE:
- Variable Scope based on block
- functions
- Lists

GRAMMAR:
B := S | SB
S := A | IF | PRINT
A := id AssOpp E
E := id | value | E BinOpp E
IF := if E { B }
