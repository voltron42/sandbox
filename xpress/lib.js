var lib=[{
	"name": "add",
	"returnType": "number",
	"paramTypes": ["number",	"number"],
	"infix": "+"
},
{
	"name": "sub",
	"returnType": "number",
	"paramTypes": ["number",
	"number"],
	"infix": "-"
},
{
	"name": "mult",
	"returnType": "number",
	"paramTypes": ["number",
	"number"],
	"infix": "*"
},
{
	"name": "div",
	"returnType": "number",
	"paramTypes": ["number",
	"number"],
	"infix": "/"
},
{
	"name": "mod",
	"returnType": "number",
	"paramTypes": ["number",
	"number"],
	"infix": "%"
},
{
	"name": "and",
	"returnType": "boolean",
	"paramTypes": ["boolean",
	"boolean"],
	"infix": "&&"
},
{
	"name": "or",
	"returnType": "boolean",
	"paramTypes": ["boolean",
	"boolean"],
	"infix": "||"
},
{
	"name": "xor",
	"returnType": "boolean",
	"paramTypes": ["boolean",
	"boolean"],
	"infix": "^"
},
{
	"name": "not",
	"returnType": "boolean",
	"paramTypes": ["boolean"]
},
{
	"name": "eq",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": "=="
},
{
	"name": "ne",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": "!="
},
{
	"name": "lt",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": "<"
},
{
	"name": "le",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": "<="
},
{
	"name": "gt",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": ">"
},
{
	"name": "ge",
	"returnType": "boolean",
	"paramTypes": ["?",
	"?"],
	"infix": ">="
},
{
	"name": "like",
	"returnType": "boolean",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "exact",
	"returnType": "boolean",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "if",
	"returnType": "?",
	"paramTypes": ["boolean",
	"?",
	"?"]
},
{
	"name": "length",
	"returnType": "number",
	"paramTypes": ["Array"]
},
{
	"name": "isNaN",
	"returnType": "boolean",
	"paramTypes": ["number"]
},
{
	"name": "isFinite",
	"returnType": "boolean",
	"paramTypes": ["number"]
},
{
	"name": "min",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "max",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "sum",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "product",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "average",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "median",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "mode",
	"returnType": "number",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "gcd",
	"returnType": "number",
	"paramTypes": ["number",
	"number"]
},
{
	"name": "lcm",
	"returnType": "number",
	"paramTypes": ["number",
	"number"]
},
{
	"name": "abs",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "ceil",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "floor",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "round",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "rand",
	"returnType": "number"
},
{
	"name": "sqrt",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "pow",
	"returnType": "number",
	"paramTypes": ["number",
	"number"]
},
{
	"name": "log",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "exp",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "cos",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "sin",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "tan",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "acos",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "asin",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "atan",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "atan2",
	"returnType": "number",
	"paramTypes": ["number"]
},
{
	"name": "e",
	"returnType": "number"
},
{
	"name": "pi",
	"returnType": "number"
},
{
	"name": "charAt",
	"returnType": "string",
	"paramTypes": ["string",
	"number"]
},
{
	"name": "charCodeAt",
	"returnType": "number",
	"paramTypes": ["string",
	"number"]
},
{
	"name": "concat",
	"returnType": "string",
	"paramTypes": ["string"],
	"isVarArg": true
},
{
	"name": "fromCharCode",
	"returnType": "string",
	"paramTypes": ["number"],
	"isVarArg": true
},
{
	"name": "indexOf",
	"returnType": "number",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "lastIndexOf",
	"returnType": "number",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "match",
	"returnType": "string",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "replace",
	"returnType": "string",
	"paramTypes": ["string",
	"string",
	"string"]
},
{
	"name": "search",
	"returnType": "number",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "slice",
	"returnType": "string",
	"paramTypes": ["string",
	"number",
	"number"]
},
{
	"name": "substring",
	"returnType": "string",
	"paramTypes": ["string",
	"number",
	"number"]
},
{
	"name": "lower",
	"returnType": "string",
	"paramTypes": ["string"]
},
{
	"name": "upper",
	"returnType": "string",
	"paramTypes": ["string"]
},
{
	"name": "trim",
	"returnType": "string",
	"paramTypes": ["string"]
},
{
	"name": "split",
	"returnType": "Array",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "formatString",
	"returnType": "string",
	"paramTypes": ["string",
	"?"],
	"isVarArg": true
},
{
	"name": "template",
	"returnType": "string",
	"paramTypes": ["string",
	"object"]
},
{
	"name": "parseDate",
	"returnType": "Date",
	"paramTypes": ["string",
	"string"]
},
{
	"name": "date",
	"returnType": "Date",
	"paramTypes": ["number",
	"number",
	"number"]
},
{
	"name": "dateAndTime",
	"returnType": "Date",
	"paramTypes": ["number",
	"number",
	"number",
	"number",
	"number",
	"number"]
},
{
	"name": "year",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "month",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "day",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "weekday",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "hour",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "minute",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "second",
	"returnType": "number",
	"paramTypes": ["Date"]
},
{
	"name": "formatDate",
	"returnType": "string",
	"paramTypes": ["Date",
	"string"]
},
{
	"name": "join",
	"returnType": "string",
	"paramTypes": ["Array",
	"string"]
},
{
	"name": "first",
	"returnType": "?",
	"paramTypes": ["Array"]
},
{
	"name": "last",
	"returnType": "?",
	"paramTypes": ["Array"]
},
{
	"name": "tail",
	"returnType": "Array",
	"paramTypes": ["Array"]
},
{
	"name": "reverse",
	"returnType": "Array",
	"paramTypes": ["Array"]
},
{
	"name": "splice",
	"returnType": "Array",
	"paramTypes": ["Array",
	"number",
	"number"]
},
{
	"name": "insert",
	"returnType": "Array",
	"paramTypes": ["Array",
	"number",
	"?"],
	"isVarArg": true
},
{
	"name": "namespace",
	"returnType": "?",
	"paramTypes": ["string"],
	"isVarArg": true
},
{
	"name": "entry",
	"returnType": "Entry",
	"paramTypes": ["string", "?"]
},
{
	"name": "array",
	"returnType": "Array",
	"paramTypes": ["?"],
	"isVarArg": true
},{
	"name": "map",
	"returnType": "object",
	"paramTypes": ["Entry"],
	"isVarArg": true
}];