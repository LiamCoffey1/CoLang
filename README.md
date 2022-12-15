# CoLang

### TODO:
- Formalize grammar for the language
- Code input options --> load file and multiple line input from console (currently single)
- Token generator... abstract from string references to good token descriptions... 
- Types (duck for the moment, only string and int recognizable)
- Parsing for else statements
- Verification of Syntax Tree - Only partial expection currently
- Optimization of Syntax Tree - Only constant folding currently

### FUTURE:
- Variable Scope based on block
- functions - return types and usage within expressions
- Data structures

### GRAMMAR:
- B := S | SB
- S := A | IF | PRINT
- A := id AssOpp E
- E := id | value | E BinOpp E
- IF := if E { B }
