import java.util.LinkedList;

public class PrereqParser {
    
    public PrereqParser() {
    }
    
    public static LinkedList<String> makeClean(String toParse) {
        toParse = toParse.replaceAll(",", " ");
        toParse = toParse.replaceAll("or", "");
        toParse = toParse.replaceAll("prerequisites", "");
        toParse = toParse.replaceAll(":", "");
        toParse = toParse.replace(".", "");
        toParse = toParse.replaceAll("and", "");
        toParse = toParse.replaceAll("&", "");
        String[] splited = toParse.split("\\s+");
        LinkedList<String> toReturn = new LinkedList<String>();
        for (int i = 0; i < splited.length; i++) {
            if (isInt(splited[i])) {
                //System.out.println("i is: " + splited[i]);
                for (int z = i; z >= 0; z --) {
                    if(!isInt(splited[z]) && !splited[z].equals(" ")) {
                        //System.out.println("z is " + splited[z]);
                        String toAdd = splited[z] + " " + splited[i];
                        toReturn.add(toAdd);
                        break;
                    }
                    
                }
            }
        }
        return toReturn;
    }
    
    public static boolean isInt(String x) {
        try {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
  
}