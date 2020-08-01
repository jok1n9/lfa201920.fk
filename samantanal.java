import java.util.*;
import org.stringtemplate.v4.*;
import java.util.*;
import org.antlr.v4.runtime.tree.*;
public class samantanal extends QuestionBaseVisitor<Object> {
	ArrayList<String> quet = new ArrayList<String>();
	//criação de uma question, não podem existir question repetidas
	@Override public Object visitQuestion(QuestionParser.QuestionContext ctx) {
		String a=ctx.STRING().getText();
		if(quet.contains(a)){
			System.out.println("VARIAVEL QUESTION "+a+ "declarada mais que uma vez. Closing...");
			System.exit(1);
		}
		else
		{
			quet.add(a);
		}
		return visitChildren(ctx); }
	@Override public Object visitQuestionarray(QuestionParser.QuestionarrayContext ctx) {
		String a=ctx.STRING(1).getText();
		if(quet.contains(a)){
			System.out.println("VARIAVEL QUESTION "+a+ "declarada mais que uma vez. Closing...");
			System.exit(1);
		}
		else
		{
			quet.add(a);
		}
		return visitChildren(ctx); }
	//não pode ser chamada uma question que não tenha sido criada
	@Override public Object visitCallString(QuestionParser.CallStringContext ctx) {
		String a=ctx.STRING(0).getText();
		String b=ctx.STRING(1).getText();
		if(!quet.contains(a)){
			System.out.println("VARIAVEL "+ a+ " NUNCA DECLARADA");
			System.exit(1);
		}

		String[] c= {"val", "id", "quest", "ans", "user"};
		boolean d= false;
		for(int i=0; i<4; i++){
			if(b.equals(c[i])){
				d=true;
			}
		}
		if(!d){
			System.out.println(b+" NAO EXISTE NESTA LINGUAGEM");
			System.exit(1);}
		return visitChildren(ctx);}

	@Override public Object visitCallarr1(QuestionParser.Callarr1Context ctx) { 
		String a=ctx.STRING().getText();
		if(!quet.contains(a)){
			System.out.println("VARIAVEL "+a+" NUNCA DECLARADA");
			System.exit(1);
		}

		return visitChildren(ctx);} 

	@Override public Object visitCallarr2(QuestionParser.Callarr2Context ctx) { 
		String a=ctx.STRING(0).getText();
		if(!quet.contains(a)){
			System.out.println("variavel question não declarada");
			System.exit(1);
		}
		return visitChildren(ctx);
	}
}