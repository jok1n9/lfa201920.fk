package Json;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.io.File;


public class JSONreal{
   
   static HashMap<String, String> id = new HashMap<String, String>();
   static ArrayList<String> resp = new ArrayList<String>();
   static HashMap<String, String> correct = new HashMap<String, String>();
   public JSONreal(String filename)  throws IOException {
      try {

         // create a CharStream that reads from standard input:
         CharStream input = CharStreams.fromFileName(filename);
         // create a lexer that feeds off of input CharStream:
         JSONLexer lexer = new JSONLexer(input);
         // create a buffer of tokens pulled from the lexer:
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         // create a parser that feeds off the tokens buffer:
         JSONParser parser = new JSONParser(tokens);
         // replace error listener:
         //parser.removeErrorListeners(); // remove ConsoleErrorListener
         //parser.addErrorListener(new ErrorHandlingListener());
         // begin parsing at json rule:
         ParseTree tree = parser.json();
         if (parser.getNumberOfSyntaxErrors() == 0) {
            // print LISP-style tree:
            // System.out.println(tree.toStringTree(parser));
            Interpreter visitor0 = new Interpreter();
            visitor0.visit(tree);
            id = visitor0.getID();
            resp = visitor0.getAnswer();
            correct = visitor0.getCorrect();
         }
      }
      catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      catch(RecognitionException e) {
         e.printStackTrace();
         System.exit(1);
      }
   }

   public String getID(String ID){
      String res = id.get(ID);
      return res;
   }
   public static String[] getAnswer(String ID){
      ArrayList<String> res = new ArrayList<String>();
      String s ="";
      for(int i=0 ; i< resp.size() ; i++){
         s="";
         s = resp.get(i);
         String[] a = s.split("-");
         if( (a[0].trim()).equals(ID.trim())){
            s = a[1];
            res.add(s);
         }
      } 
      String[] fin = res.toArray(new String[0]);
      return fin;
   }

   public String getCorrect(String ID){
      return correct.get(ID.trim());
   }
}
