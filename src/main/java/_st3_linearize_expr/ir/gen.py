from codecs import getdecoder
types = 'i8|u8|i16|u16|i32|u32|i64|u64|f32|f64|boolean\
|void|type_parameter|class|function|array|tuple'

basetypes = 'i8|u8|i16|u16|i32|u32|i64|u64|f32|f64'

meth0 = 'get_size|get_align'
meth1 = 'equal_to|class_template|iterated|reference|primitive'
meth2 = 'has_signedness|signed|unsigned'
meth3 = 'arithmetic|integer|floating'

expr = 'EASSIGN|EBINARY|EUNARY|EPRIMARY_IDENT|EPRIMARY_STRING\
|EPRIMARY_NUMBER|EPRIMARY_NULL_LITERAL|ECAST|EMETHOD_INVOCATION\
|EFIELD_ACCESS|ECLASS_INSTANCE_CREATION|ESELF|EARRAY_INSTANCE_CREATION\
|ESTRING_CONST|EARRAY_ACCESS'

types = 'byte,char,short,int,long,float,double'

opcodes = """
    AssignVarAllocObject               ,
    AssignVarBinop                     ,
    AssignVarFalse                     ,
    AssignVarFieldAccess               ,
    AssignVarStaticFieldAccess         ,
    AssignVarFlatCallClassCreationTmp  ,
    AssignVarFlatCallStringCreationTmp ,
    AssignVarFlatCallResult            ,
    AssignVarFlatCallResultStatic      ,
    AssignVarNull                      ,
    AssignVarNum                       ,
    AssignVarTernaryOp                 ,
    AssignVarTrue                      ,
    AssignVarUnop                      ,
    AssignVarVar                       ,
    AssignVarSizeof                    ,
    FlatCallConstructor                ,
    FlatCallVoid                       ,
    FlatCallVoidBuiltin                ,
    FlatCallVoidStaticClassMethod      ,
    StoreFieldVar                      ,
    StoreVarField                      ,
    IntrinsicText                      ,
    CallListenerVoidMethod             ,
    CallListenerResultMethod           ,
    StoreVarVar                        
"""
opcodes_arr = []
for opc in opcodes.split(','):
    opcodes_arr.append(opc.strip())

opcodes_arr.sort()

# private StoreFieldAssignOpCall storeFieldAssignOpCall;
# public FlatCodeItem(AssignVarBinop assignVarBinop) { this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
# public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
# if(isAssignVarBinop()) { return assignVarBinop.toString(); }
# public AssignVarAllocObject getAssignVarAllocObject() { return assignVarAllocObject; }

# public Var getDest() {
# 
#   if (isAssignVarAllocObject()) {
#     return assignVarAllocObject.getLvalue();
#   }
#   if (isStoreArrayVar()) {
#     err();
#   }
# 
#   throw new AstParseException("unknown item for result: " + toString());
# }
# 
# private void err() { throw new AstParseException("unexpected item for result: " + toString()); }

fields = ''
constructors = ''
methods = ''
to_strings = '  @Override\n  public String toString() {\n'
getters = '  public Opc getOpcode() { return this.opcode; }\n'
get_dest = '  public Var getDest() {\n'

assigns_ops = """
  public boolean isOneOfAssigns() {
      return 
         isAssignVarAllocObject()  
      || isAssignVarBinop()  
      || isAssignVarFalse()  
      || isAssignVarFieldAccess() 
      || isAssignVarStaticFieldAccess()  
      || isAssignVarFlatCallClassCreationTmp()
      || isAssignVarFlatCallStringCreationTmp()
      || isAssignVarFlatCallResult()
      || isAssignVarFlatCallResultStatic()
      || isAssignVarNull()   
      || isAssignVarNum()   
      || isAssignVarTernaryOp()  
      || isAssignVarTrue()   
      || isAssignVarUnop()   
      || isAssignVarVar(); 
  }
"""

for opc in opcodes_arr:
    varname = opc[0].lower() + opc[1:]
    fields += '  private ' + opc + ' ' + varname + ';\n'
    constructors += '  public FlatCodeItem(' + opc + ' ' + varname + ') { this.opcode = Opc.' + opc + '; this.' + varname + ' = ' + varname + '; }\n'
    methods += '  public boolean is' + opc + '() { return this.opcode == Opc.' + opc + '; }\n'
    to_strings += '    if(is' + opc + '()) { return ' + varname + '.toString(); }\n'
    getters += '  public ' + opc + ' get' + opc + '() { return this.' + varname + '; }\n'
    
    get_dest += '    if(is' + opc + '()) {\n'
    if str(opc).startswith('Assign'):
        get_dest += '      return ' + varname + '.getLvalue();\n'
    elif str(opc) == 'FlatCallConstructor':
         get_dest += '      return ' + varname + '.getThisVar();\n'
    elif str(opc) == 'IntrinsicText':
         get_dest += '      return ' + varname + '.getDest();\n'
    elif str(opc) == 'CallListenerResultMethod':
         get_dest += '      return ' + varname + '.getDest();\n'
    else:
        get_dest += '      err();\n'
        
    get_dest += '    }\n'

print('  //@formatter:off')
print(fields)
print(constructors)
print(methods)

to_strings += '    return \"?UnknownItem\"; \n  }\n';
print(to_strings)

print(getters)
print(assigns_ops)

get_dest += '    throw new AstParseException(\"unknown item for result: \" + toString());\n'
get_dest += '  }\n'
get_dest += '  private void err() { throw new AstParseException(\"unexpected item for result: \" + toString()); }'


print(get_dest)
print('  //@formatter:on')




















