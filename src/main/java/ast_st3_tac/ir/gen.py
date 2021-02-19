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
    AssignVarVar,
    AssignVarFlatCallResult,
    AssignVarArrayAccess,
    AssignVarFieldAccess,
    AssignVarBinop,
    AssignVarUnop,
    AssignVarAllocObject,
    AssignVarTrue,
    AssignVarFalse,
    AssignVarNull,
    AssignVarNum,
    AssignVarString,
    StoreArrayAssignOpCall,
    StoreArrayVar,
    StoreFieldAssignOpCall,
    StoreFieldVar,
    StoreVarAssignOpCall,
    StoreVarVar,
    FlatCallVoid,
    FlatCallConstructor,
    AssignVarFlatCallClassCreationTmp
"""
opcodes_arr = []
for opc in opcodes.split(','):
    opcodes_arr.append(opc.strip())

opcodes_arr.sort()

# private StoreFieldAssignOpCall storeFieldAssignOpCall;
# public FlatCodeItem(AssignVarBinop assignVarBinop) { this.opcode = Opc.AssignVarBinop; this.assignVarBinop = assignVarBinop; }
# public boolean isAssignVarBinop() { return this.opcode == Opc.AssignVarBinop; }
# else if(isAssignVarBinop()) { return assignVarBinop.toString(); }

str1 = ''
str2 = ''
str3 = ''
str4 = ''

for opc in opcodes_arr:
    varname = opc[0].lower() + opc[1:]
    str1 += 'private ' + opc + ' ' + varname + ';\n'
    str2 += 'public FlatCodeItem(' + opc + ' ' + varname + ') { this.opcode = Opc.' + opc + '; this.' + varname + ' = ' + varname + '; }\n'
    str3 += 'public boolean is' + opc + '() { return this.opcode == Opc.' + opc + '; }\n'
    str4 += 'else if(is' + opc + '()) { return ' + varname + '.toString(); }\n'

print(str1)
print(str2)
print(str3)
print(str4)











