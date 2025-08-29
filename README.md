# CoLang

### TODO:
- Code input options --> load file (currently single)
- Verification of Syntax Tree - Only partial expection currently
- Optimization of Syntax Tree - Only constant folding currently

### FUTURE:
- Data structures

### Web Server
/webserver/Server.java

Serves .cl files from /dist folder and renders as html

e.g

```ruby
component button {
    view $<button>{buttonText}</button>$
}
component image {
    view $<image src = '{image}'></image>$
}
component card {
    view $<div class = "container"><c-image/><c-button/></div>$
}
fun getCards() {
    return $<div><c-card buttonText = "Go here" image = "google.ie"/><c-card buttonText = "Go here too" image =  "bing.ie"/></div>$
}
print $<div>{getCards()}</div>$
```


# Examples


### Variables 
```ruby
var testVar = 2
var array = [1, 2, 3, 4]
var stringVar = "Hello"
var boole = true && false
var inst = new Instance()
testVar = 5
stringVar = testVar.toString() + " <- testVar"
```
    
### Control

```ruby
while (i != 5) {
    i += 1
}
if (true) {
    print true
} else {
    print false
}
foreach in array(value) {
    print value
}
```

### Classes 

```ruby
class NewClass {
    let width = 0
    init() {
        width = 5
    }
    fun getWidth() {
        return width
    }
}
let inst = new NewClass() {
    width = 10
}
print "Width: " + inst.getWidth().toString()
```



# Grammar

**SYMBOLS**
```ruby
<type> := string | number | boolean | void | any
<boolean_opps> := || | &&
<add_ops> := + | -
<mult_opps> := * | /
<compare_opps> := == | >= | <= | > | < | !=
<assignment_operator> := += | -= | *= | /= | =
```

**PROGRAM**
```ruby
<program> ::= <block>
<block> ::= <statements>
```
**STATMENTS**
```ruby
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

<class_def> ::= component <identifier> <component_body>
<component_body> ::= { <declare_statement+> || <component_view> }
<component_view> ::= view <clml_expression>


<accessor> := <member_call> . <member_call> || <member_call>
<member_call> := <identifier> || <function_call>
```
**EPRESSIONS**
```ruby
<expression> ::= <boolean> 
<boolean> ::= <compare> | <expression> <boolean_opps> <expression>
<compare> ::= <additive> | <expression> <compare_opps> <expression>
<additive> ::= <multiplitive> | <expression> <add_opps> <expression>
<multiplitive> := <primary> |  <expression> <mult_opps> <expression>
<primary> ::= <accessor>
    | ( <expression> ) 
    | !<expression>
    | $<constant>$
    | <constant> 
```

