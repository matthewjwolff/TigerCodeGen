package Mips;
import Temp.Temp;
import Temp.TempList;
import Temp.Label;
import Temp.LabelList;
import java.util.Hashtable;

public class Codegen {
  MipsFrame frame;
  public Codegen(MipsFrame f) {frame = f;}

  private Assem.InstrList ilist = null, last = null;

  private void emit(Assem.Instr inst) {
    if (last != null)
      last = last.tail = new Assem.InstrList(inst, null);
    else {
      if (ilist != null)
	throw new Error("Codegen.emit");
      last = ilist = new Assem.InstrList(inst, null);
    }
  }

  Assem.InstrList codegen(Tree.Stm s) {
    munchStm(s);
    Assem.InstrList l = ilist;
    ilist = last = null;
    return l;
  }

  static Assem.Instr OPER(String a, TempList d, TempList s, LabelList j) {
    return new Assem.OPER("\t" + a, d, s, j);
  }
  static Assem.Instr OPER(String a, TempList d, TempList s) {
    return new Assem.OPER("\t" + a, d, s);
  }
  static Assem.Instr MOVE(String a, Temp d, Temp s) {
    return new Assem.MOVE("\t" + a, d, s);
  }

  static TempList L(Temp h) {
    return new TempList(h, null);
  }
  static TempList L(Temp h, TempList t) {
    return new TempList(h, t);
  }

  void munchStm(Tree.Stm s) {
    if (s instanceof Tree.MOVE) 
      munchStm((Tree.MOVE)s);
    else if (s instanceof Tree.EXP)
      munchStm((Tree.EXP)s);
    else if (s instanceof Tree.JUMP)
      munchStm((Tree.JUMP)s);
    else if (s instanceof Tree.CJUMP)
      munchStm((Tree.CJUMP)s);
    else if (s instanceof Tree.LABEL)
      munchStm((Tree.LABEL)s);
    else
      throw new Error("Codegen.munchStm");
  }

  //TODO: can be improved
  void munchStm(Tree.MOVE s) {
      //are we loading or storing
      System.out.println("munch move");
      if(s.dst instanceof Tree.MEM) {
         Tree.MEM dst = (Tree.MEM)s.dst;
         Temp dstReg = munchExp(dst);
         emit(OPER("sw (`d0) `s0", L(dstReg), L(munchExp(s.src))));
      } else if (s.src instanceof Tree.MEM) {
         Temp srcReg = munchExp(s.src);
         emit(OPER("lw `d0 (`s0)", L(munchExp(s.dst)), L(srcReg)));
      } else {
          //not moving memory
        emit(OPER("lw `d0 `s0", L(munchExp(s.dst)), L(munchExp(s.src))));
      }
  }

  void munchStm(Tree.EXP s) {
    munchExp(s.exp);
  }

  //TODO
  void munchStm(Tree.JUMP s) {
      //jump to label (if label is passed in
      if(s.exp instanceof Tree.NAME) {
          //b = unconditional BRANCH
          emit(OPER("b " + ((Tree.NAME)s.exp).label.toString(), null, null, s.targets));
      }
      //JUMP to REGISTER (source 0)
      emit(OPER("jr `s0", null, L(munchExp(s.exp)), s.targets));
  }

  private static String[] CJUMP = new String[10];
  static {
    CJUMP[Tree.CJUMP.EQ ] = "beq";
    CJUMP[Tree.CJUMP.NE ] = "bne";
    CJUMP[Tree.CJUMP.LT ] = "blt";
    CJUMP[Tree.CJUMP.GT ] = "bgt";
    CJUMP[Tree.CJUMP.LE ] = "ble";
    CJUMP[Tree.CJUMP.GE ] = "bge";
    CJUMP[Tree.CJUMP.ULT] = "bltu";
    CJUMP[Tree.CJUMP.ULE] = "bleu";
    CJUMP[Tree.CJUMP.UGT] = "bgtu";
    CJUMP[Tree.CJUMP.UGE] = "bgeu";
  }

  //TODO
  void munchStm(Tree.CJUMP s) {
      //need to add const handling
      //jump operator two comparisons, temporaries holding comparison operators, true label, false label
      emit(OPER(CJUMP[s.relop] + " `s0 `s1 " + s.iftrue.toString(), null, L(munchExp(s.left), L(munchExp(s.right))), new LabelList(s.iftrue, new LabelList(s.iffalse, null))));
  }

  void munchStm(Tree.LABEL l) {
      emit(new Assem.LABEL(l.label.toString() + ":", l.label));
  }

  Temp munchExp(Tree.Exp s) {
    if (s instanceof Tree.CONST)
      return munchExp((Tree.CONST)s);
    else if (s instanceof Tree.NAME)
      return munchExp((Tree.NAME)s);
    else if (s instanceof Tree.TEMP)
      return munchExp((Tree.TEMP)s);
    else if (s instanceof Tree.BINOP)
      return munchExp((Tree.BINOP)s);
    else if (s instanceof Tree.MEM)
      return munchExp((Tree.MEM)s);
    else if (s instanceof Tree.CALL)
      return munchExp((Tree.CALL)s);
    else
      throw new Error("Codegen.munchExp");
  }

  Temp munchExp(Tree.CONST e) {
      //MIPS has a reserved register for 0
      if(e.value == 0)
        return frame.ZERO;
      //load the CONST into a new temporary, return that temporary
      Temp temp = new Temp();
      emit(OPER("li `d0 "+e.value, L(temp), null));
      return temp;
  }

  //"the symbolic constant n (corresponding to an assembly language label"
  //a label is just an address in program memory, load that address
  Temp munchExp(Tree.NAME e) {
      Temp temp = new Temp();
      emit(OPER("la `d0 "+e.label.toString(), L(temp), null));
    return temp;
  }
  
  Temp munchExp(Tree.TEMP e) {
    if (e.temp == frame.FP) {
      Temp t = new Temp();
      emit(OPER("addu `d0 `s0 " + frame.name + "_framesize",
		L(t), L(frame.SP)));
      return t;
    }
    return e.temp;
  }

  private static String[] BINOP = new String[10];
  static {
    BINOP[Tree.BINOP.PLUS   ] = "add";
    BINOP[Tree.BINOP.MINUS  ] = "sub";
    BINOP[Tree.BINOP.MUL    ] = "mulo";
    BINOP[Tree.BINOP.DIV    ] = "div";
    BINOP[Tree.BINOP.AND    ] = "and";
    BINOP[Tree.BINOP.OR     ] = "or";
    BINOP[Tree.BINOP.LSHIFT ] = "sll";
    BINOP[Tree.BINOP.RSHIFT ] = "srl";
    BINOP[Tree.BINOP.ARSHIFT] = "sra";
    BINOP[Tree.BINOP.XOR    ] = "xor";
  }

  private static int shift(int i) {
    int shift = 0;
    if ((i >= 2) && ((i & (i - 1)) == 0)) {
      while (i > 1) {
	shift += 1;
	i >>= 1;
      }
    }
    return shift;
  }

  //TODO - real implementation
  Temp munchExp(Tree.BINOP e) {
      //simple version
      //i mean it works right?
      Temp temp = munchExp(e.left);
      emit(OPER(BINOP[e.binop]+" `d0 `s0", L(temp), L(munchExp(e.right))));
    return temp;
  }

  //Calculate memory address, store/load handled elsewhere
  Temp munchExp(Tree.MEM e) {
    return munchExp(e.exp);
  }

  //TODO
  Temp munchExp(Tree.CALL s) {
      //if we're calling a NAME'd function
      TempList args = munchArgs(0, s.args);
      if(s.func instanceof Tree.NAME) {
          //jump to function definition, all callersaved registers are trashed, args registers are passed in
          emit(OPER("ja1 "+((Tree.NAME)s.func).label.toString(),frame.callerSaves,args));
      }
      else {
          //jump to instruction given by function, assume all caller-saved registers are trashed, passing parameters args
          emit(OPER("ja1 `d0 `s0", frame.callerSaves, L(munchExp(s.func), args)));
      }
    //hardwired address for the return value of a functions
    return frame.RV();
  }

  //more?
  private TempList munchArgs(int i, Tree.ExpList args) {
    if (args == null)
      return null;
    Temp src = munchExp(args.head);
    if (i > frame.maxArgs)
      frame.maxArgs = i;
    switch (i) {
    case 0:
      emit(MOVE("move `d0 `s0", frame.A0, src));
      break;
    case 1:
      emit(MOVE("move `d0 `s0", frame.A1, src));
      break;
    case 2:
      emit(MOVE("move `d0 `s0", frame.A2, src));
      break;
    case 3:
      emit(MOVE("move `d0 `s0", frame.A3, src));
      break;
    default:
      emit(OPER("sw `s0 " + (i-1)*frame.wordSize() + "(`s1)",
		null, L(src, L(frame.SP))));
      break;
    }
    return L(src, munchArgs(i+1, args.tail));
  }
}
