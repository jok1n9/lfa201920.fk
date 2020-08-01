import java.util.*;
import org.stringtemplate.v4.*;

public class Compiler extends QuestionBaseVisitor<ST> {
   private STGroup templates = new STGroupFile("java.stg");
   
   @Override public ST visitProgram(QuestionParser.ProgramContext ctx) {
      ST res = templates.getInstanceOf("module");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      return res;
   }

   @Override public ST visitAction(QuestionParser.ActionContext ctx) {
      ST res = visitChildren(ctx);
      return res;
   }

   @Override public ST visitQuestion(QuestionParser.QuestionContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST qd = templates.getInstanceOf("questiondef");
      qd.add("value", ctx.STRING().getText());
      res.add("stat", qd.render());
      return res;
   }

   //rever este
   @Override public ST visitQuestionarray(QuestionParser.QuestionarrayContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST arr = templates.getInstanceOf("questionarr");
      ST pa  = templates.getInstanceOf("defar");
      arr.add("value", ctx.STRING(1).getText());
      arr.add("var", ctx.STRING(0).getText());
      res.add("stat", arr.render());
      for(int i =0 ; i < Integer.parseInt(ctx.STRING(0).getText()) ; i++){
         pa.remove("val0");
         pa.remove("val1");
         pa.add("val1", i);
         pa.add("val0", ctx.STRING(1).getText());
         res.add("stat", pa.render());
      }
      return res;
   }

   @Override public ST visitLoad(QuestionParser.LoadContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST load = templates.getInstanceOf("load");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += "\"" + list.next() + "\"" + ",";
      }
      String fin= "";
      for(int i=0; i<s.length()-1 ; i++){
         fin += s.charAt(i);
      }
      load.add("val0", visit(ctx.call()).render());
      load.add("val1", "new String[]{" + fin);
      res.add("stat", load.render());
      return res;
   }

   @Override public ST visitCondCall(QuestionParser.CondCallContext ctx) {
      ST res = templates.getInstanceOf("if");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      res.add("val", ctx.STRING().getText());
      return res;
   }

   @Override public ST visitCondFCall(QuestionParser.CondFCallContext ctx) {
      ST res = templates.getInstanceOf("ifN");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      res.add("val", ctx.STRING().getText());
      return res;
   }

   @Override public ST visitCondElse(QuestionParser.CondElseContext ctx) {
      ST res = templates.getInstanceOf("else");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      return res;
   }

   @Override public ST visitForcall(QuestionParser.ForcallContext ctx) {
      ST res = templates.getInstanceOf("for1");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      res.add("value", visit(ctx.call()).render());
      res.add("val", ctx.STRING().getText());
      return res;
   }

   @Override public ST visitForcallarr(QuestionParser.ForcallarrContext ctx) {
      ST res = templates.getInstanceOf("for1");
      Iterator<QuestionParser.ActionContext> list = ctx.action().iterator();       
      while(list.hasNext()) {
         res.add("stat", visit(list.next()).render());
      }
      res.add("value", visit(ctx.callarr()).render());
      res.add("val", ctx.STRING().getText());
      return res;
   }


   //rever este
   @Override public ST visitFromCall(QuestionParser.FromCallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST from = templates.getInstanceOf("from");
      from.add("val0", visit(ctx.call()).render());
      from.add("val1", visit(ctx.select()).render());
      res.add("stat", from.render());
      return res;
   }
   ArrayList<String> store = new ArrayList<String>();
   @Override public ST visitFromCallans(QuestionParser.FromCallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      store.add(ctx.STRING(0).getText());
      store.add(ctx.STRING(1).getText());
      res=visit(ctx.select());
      return res;
   }
   @Override public ST visitSelectAnswer(QuestionParser.SelectAnswerContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST from = templates.getInstanceOf("fromans");
      from.add("val0", visit(ctx.call()).render());
      from.add("val1", ctx.STRING().getText());
      res.add("stat", from.render());
      return res;
   }

   @Override public ST visitSelectOne(QuestionParser.SelectOneContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST select = templates.getInstanceOf("select");
      ST def = templates.getInstanceOf("defid");
      def.add("val", str+"");
      def.add("val0", ctx.STRING().getText());
      select.add("val0", ctx.STRING().getText());
      res.add("stat", select.render());
      res.add("stat", def.render());
      return res;
   }

   @Override public ST visitSelectMultiple(QuestionParser.SelectMultipleContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST select = templates.getInstanceOf("selectM");
      Iterator list = ctx.STRING().iterator();
      String s = "";
      while(list.hasNext()){
         s += list.next() + "-";
      }
      String[] a = s.split("-");
      for(int i=0 ; i < a.length ; i++){
         select.remove("val");
         select.remove("val1");
         select.add("val", store.get(0) + "[" + i + "]"+ "." + store.get(1));
         select.add("val1", a[i]);
         res.add("stat", select.render());
      }
      return res;
   }

   @Override public ST visitImport1(QuestionParser.Import1Context ctx) {
      ST res = templates.getInstanceOf("stats");
      ST imp = templates.getInstanceOf("imports");
      imp.add("string1", ctx.STRING(0).getText());
      imp.add("string2", ctx.STRING(1).getText());
      res.add("stat", imp.render());
      return res;
   }

   @Override public ST visitDefCall(QuestionParser.DefCallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST def = templates.getInstanceOf("defstring");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      def.add("val0", visit(ctx.call()).render());
      def.add("val1", s);
      res.add("stat", def.render());
      return res;
   }

   @Override public ST visitDefString(QuestionParser.DefStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST def = templates.getInstanceOf("defstring");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      String[] a = s.split(" ");
      String f = "";
      for(int i=1 ; i < a.length ; i++){
         f += a[i] + " ";
      }
      def.add("val0", "String " + ctx.STRING(0).getText());
      def.add("val1", f);
      res.add("stat", def.render());
      return res;
   }

   @Override public ST visitTheme(QuestionParser.ThemeContext ctx) {
      return visitChildren(ctx);
   }

   @Override public ST visitValue(QuestionParser.ValueContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST def = templates.getInstanceOf("def");
      def.add("val0", visit(ctx.call()).render());
      def.add("val1", ctx.STRING().getText());
      res.add("stat", def.render());
      return res;
   }

   @Override public ST visitShowCallans(QuestionParser.ShowCallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("showans");
      print.add("value",visit(ctx.callans()).render());
      print.add("i", vari);
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitShowCall(QuestionParser.ShowCallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("show");
      print.add("value", visit(ctx.call()).render());
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitShowString(QuestionParser.ShowStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("show");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      print.add("value", "\"" + s + "\"");
      res.add("stat", print.render());
      return res;
   }
 
     @Override public ST visitCreateCallans(QuestionParser.CreateCallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("create");
      print.add("value", visit(ctx.callans()).render());
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitCreateCall(QuestionParser.CreateCallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("create");
      print.add("value", visit(ctx.call()).render());
      res.add("stat", print.render());
      return res;
   }
  @Override public ST visitCreateString(QuestionParser.CreateStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("create");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      print.add("value", "\"" + s + "\"");
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitSavecallans(QuestionParser.SavecallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("save");
      print.add("value", visit(ctx.callans()).render());
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitSavecall(QuestionParser.SavecallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("save");
      print.add("value", visit(ctx.call()).render());
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitSaveString(QuestionParser.SaveStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("save");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      print.add("value", "\"" + s + "\"");
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitCloseString(QuestionParser.CloseStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("close");
      Iterator list = ctx.STRING().iterator();  
      String s = "";     
      while(list.hasNext()) {
         s += list.next() + " ";
      }
      print.add("value", "\"" + s + "\"");
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitCloseCallans(QuestionParser.CloseCallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("close");
      print.add("value", visit(ctx.callans()).render());
      res.add("stat", print.render());
      return res;
   }
   @Override public ST visitCloseCall(QuestionParser.CloseCallContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST print = templates.getInstanceOf("close");
      print.add("value", visit(ctx.call()).render());
      res.add("stat", print.render());
      return res;
   }

   @Override public ST visitDifficulty(QuestionParser.DifficultyContext ctx) {
      return visitChildren(ctx);
   }

   @Override public ST visitShuffle(QuestionParser.ShuffleContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST shu = templates.getInstanceOf("shuffle");
      shu.add("val1", visit(ctx.call()).render());
      shu.add("val0", newVar());
      res.add("stat", shu.render());
      return res;
   }

   @Override public ST visitWaitS(QuestionParser.WaitSContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST scan = templates.getInstanceOf("scan");
      scan.add("val0", ctx.STRING(1).getText());
      scan.add("val1", newVar());
      scan.add("val3", newVar());
      scan.add("val4", ctx.STRING(0).getText());
      res.add("stat", scan.render());
      return res;
   }

   @Override public ST visitWaitcallr(QuestionParser.WaitcallrContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST scan = templates.getInstanceOf("scan");
      scan.add("val0", ctx.STRING().getText());
      scan.add("val1", newVar());
      scan.add("val3", newVar());
      scan.add("val4", visit(ctx.callarr()).render());
      res.add("stat", scan.render());
      return res;
   }
   StringBuilder str = new StringBuilder();
   @Override public ST visitCallString(QuestionParser.CallStringContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST cal = templates.getInstanceOf("stringcall");
      str.setLength(0);
      str.append(ctx.STRING(0).getText());
      cal.add("val0", ctx.STRING(0).getText());
      cal.add("val1", ctx.STRING(1).getText());
      res.add("stat", cal.render());
      return res;
   }

   @Override public ST visitCallArray(QuestionParser.CallArrayContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST cal = templates.getInstanceOf("stringcall");
      cal.add("val0", visit(ctx.callarr()).render());
      cal.add("val1", ctx.STRING().getText());
      res.add("stat", cal.render());
      return res;
   }

   @Override public ST visitCallarr1(QuestionParser.Callarr1Context ctx) {
      ST res = templates.getInstanceOf("stats");
      ST cal = templates.getInstanceOf("callrr1");
      cal.add("val", ctx.STRING().getText());
      res.add("stat", cal.render());
      return res;
   }
   
   @Override public ST visitCallarr2(QuestionParser.Callarr2Context ctx) {
      ST res = templates.getInstanceOf("stats");
      ST cal = templates.getInstanceOf("callrr2");
      cal.add("val", ctx.STRING(0).getText());
      cal.add("val0", ctx.STRING(1).getText());
      res.add("stat", cal.render());
      return res;
   }
   StringBuilder vari = new StringBuilder();
   @Override public ST visitCallans(QuestionParser.CallansContext ctx) {
      ST res = templates.getInstanceOf("stats");
      ST calans = templates.getInstanceOf("stringcallans");
      vari.setLength(0);
      vari.append(ctx.STRING().getText());
      calans.add("value", visit(ctx.call()).render());
      calans.add("val", ctx.STRING().getText());
      res.add("stat", calans.render());
      return res;
   }
   
   private String newVar() {
      numVars++;
      return "v" + numVars;
   }

   private int numVars=0;
}

