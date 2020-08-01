package Json;
import java.util.*;
public class Interpreter extends JSONBaseVisitor<String> {
   static HashMap<String, String> id = new HashMap<String, String>();
   static ArrayList<String> resp = new ArrayList<String>();
   static HashMap<String, String> correct = new HashMap<String, String>();
   StringBuilder str = new StringBuilder();
   StringBuilder cr = new StringBuilder();

   @Override public String visitJson(JSONParser.JsonContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitObject(JSONParser.ObjectContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitTemaD(JSONParser.TemaDContext ctx) {
      return visit(ctx.value());
   }

   @Override public String visitQuestionID(JSONParser.QuestionIDContext ctx) {
      str.setLength(0);
      str.append(ctx.STRING(0).getText());
      id.put(ctx.STRING(0).getText().trim(), ctx.STRING(1).getText());
      return visit(ctx.value());
   }

   @Override public String visitValueString(JSONParser.ValueStringContext ctx) {
      return ctx.STRING().getText();
   }

   @Override public String visitValueObject(JSONParser.ValueObjectContext ctx) {
      return visitChildren(ctx);
   }

   @Override public String visitValueTrue(JSONParser.ValueTrueContext ctx) {
      return "true";
   }

   @Override public String visitValueFalse(JSONParser.ValueFalseContext ctx) {
      return "false";
   }

   @Override public String visitResp(JSONParser.RespContext ctx) {
      resp.add(str + "-" + ctx.STRING().getText());
      cr.setLength(0);
      cr.append(ctx.STRING().getText());
      if(visit(ctx.value()).equals("true")){
         String s0 = str+"";
         String s1 = cr+"";
         correct.put(s0.trim(),s1);
      }
      return ctx.STRING().getText();
   }

   public static HashMap<String,String> getID(){
      return id;
   }
   public static  ArrayList<String> getAnswer(){
      return resp;
   }
   public static HashMap<String,String> getCorrect(){
      return correct;
   }
}

