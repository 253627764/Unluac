package unluac.decompile.block;

import java.util.ArrayList;
import java.util.List;

import unluac.decompile.Output;
import unluac.decompile.Registers;
import unluac.decompile.expression.Expression;
import unluac.decompile.statement.Statement;
import unluac.parse.LFunction;

public class TForBlock extends Block {

  private final int register;
  private final int length;
  private final boolean hack;
  private final Registers r;
  private final List<Statement> statements;
  
  public TForBlock(LFunction function, int begin, int end, int register, int length, Registers r, boolean hack) {
    super(function, begin, end);
    this.register = register;
    this.length = length;
    this.r = r;
	this.hack = hack;
    statements = new ArrayList<Statement>(end - begin + 1);
  }

  @Override
  public int scopeEnd() {
    return end - 3;
  }
  
  @Override
  public boolean breakable() {
    return true;
  }
  
  @Override
  public boolean isContainer() {
    return true;
  }
  
  @Override
  public void addStatement(Statement statement) {
    statements.add(statement);    
  }

  @Override
  public boolean isUnprotected() {
    return false;
  }

  @Override
  public int getLoopback() {
    throw new IllegalStateException();
  }

  @Override
  public void print(Output out) {
    out.print("for ");
    int begin = hack ? this.begin : this.begin - 1;
    r.getTarget(register + 3, begin).print(out);
    for(int r1 = register + 4; r1 <= register + 2 + length; r1++) {
      out.print(", ");
      r.getTarget(r1, begin).print(out);
    }    
    out.print(" in ");
    Expression value;
    value = r.getValue(register, begin);
    value.print(out);
    if(!value.isMultiple()) {
      out.print(", ");
      value = r.getValue(register + 1, begin);
      value.print(out);
      if(!value.isMultiple()) {
        out.print(", ");
        value = r.getValue(register + 2, begin);
        value.print(out);
      }
    }
    out.print(" do");
    out.println();
    out.indent();
    Statement.printSequence(out, statements);
    out.dedent();
    out.print("end");
  }

  
}
