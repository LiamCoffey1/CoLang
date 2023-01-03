# CoLang

### TODO:
- Code input options --> load file (currently single)
- Verification of Syntax Tree - Only partial expection currently
- Optimization of Syntax Tree - Only constant folding currently

### FUTURE:
- Data structures



# Grammar

**SYMBOLS**

    <type> := string | number | boolean | void | any
    <boolean_opps> := || | &&  
    <add_ops> := + | -  
    <mult_opps> := * | /  
    <compare_opps> := == | >= | <= | > | < | !=  
    <assignment_operator> := += | -= | *= | /= | =

**PROGRAM**

    <program> ::= <block>
    <block> ::= <statements>

**STATMENTS**

    <statements> ::= <statement+>
    <statement> ::= <if_statement> 
        | <assign_statement> 
        | <while_statement> 
        | <return_statement>
        | <print_statement>
        | <declare_statement>
        | <function_call>
    <if_statement> ::= if <expression> <block> | if <expression> <block> else <block>
    <while_statement> ::= while <expression> <block>
    <return_statement> ::= return <expression>
    <print_statement> ::= print <expression>
    <assign_statement> ::= <identifier> <assignment_operator> <expression>

    <declare_statement> := <declare_variable_statement> | <declare_function_statement>
    <declare__variable_statement> ::= set <identifier> = <expression> || set <identifier>
    <declare_function_statement> ::= fun <fun_parameters> <block>

    <function_call> ::= <identifier> <fun_parameters> 
    <fun_parameters> ::= ( <parameter+> )
    <parameter> ::= <parameter_variable>, | <parameter_variable>
    <parameter_variable> ::= <identifier> | <expression>

    <class_def> ::= class <identifier> <class_body>
    <class_body> ::= { <declare_statement+> }

    <accessor> := <member_call> . <member_call> || <member_call>
    <member_call> := <identifier> || <function_call>

**EPRESSIONS**

    <expression> ::= <boolean> 
    <boolean> ::= <compare> | <expression> <boolean_opps> <expression>
    <compare> ::= <additive> | <expression> <compare_opps> <expression>
    <additive> ::= <multiplitive> | <expression> <add_opps> <expression>
    <multiplitive> := <primary> |  <expression> <mult_opps> <expression>
    <primary> ::= <accessor>
        | ( <expression> ) 
        | !<expression>
        | <constant> 


