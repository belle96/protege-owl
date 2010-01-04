package edu.stanford.smi.protegex.owl.model.classparser.compact;

import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.classdisplay.compact.CompactOWLClassDisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Holger Knublauch  <holger@knublauch.com>
 */
public class CompactParserUtil {

    private final static CompactOWLClassDisplay display = new CompactOWLClassDisplay();

    private static Map characterMap = new HashMap();


    static {
        // Sets up default character map
        setUseDefaultCharacterMap();
    }


    public static void setCharacterMap(Map map) {
        characterMap = map;
    }


    public static void setUseDefaultCharacterMap() {
        characterMap = new HashMap();
        characterMap.put(display.getOWLMinCardinalitySymbol(), new Character('<'));
        characterMap.put(display.getOWLCardinalitySymbol(), new Character('='));
        characterMap.put(display.getOWLMinCardinalitySymbol(), new Character('>'));
        characterMap.put(display.getOWLAllValuesFromSymbol(), new Character('*'));
        characterMap.put(display.getOWLSomeValuesFromSymbol(), new Character('?'));
        characterMap.put(display.getOWLHasValueSymbol(), new Character('$'));
        characterMap.put(display.getOWLComplementOfSymbol(), new Character('!'));
        characterMap.put(display.getOWLIntersectionOfSymbol(), new Character('&'));
        characterMap.put(display.getOWLUnionOfSymbol(), new Character('|'));
    }


    public static String getParseableString(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, " ()[]{}", true);
        String result = "";
        while (tokenizer.hasMoreTokens()) {
            String curTok = tokenizer.nextToken();
            if (characterMap.containsKey(curTok)) {
                result += ((Character) characterMap.get(curTok));
            }
            else {
                result += curTok;
            }
        }
        return result;
    }


//    /**
//     * Detects usages of xsd: types as expression fillers, and makes sure that
//     * they start with xsd:.  This is needed to simplify the grammar which
//     * grew historically.  Eventually this should be cleaned up.
//     *
//     * @param owlModel the OWLModel
//     * @param text     the text to preprocess
//     * @return the next text or the same if no xsd: types were found
//     */
//    public static String preprocess(OWLModel owlModel, String text) {
//        text = getParseableString(text);
//        boolean changed = true;
//        while (changed) {
//            changed = false;
//            int next = text.indexOf('*');
//            if (next < 0) {
//                next = text.indexOf('?');
//            }
//            if (next >= 0) {
//                String rest = text.substring(next + 1);
//                String newRest = preprocessFiller(owlModel, rest);
//                if (newRest != rest) {
//                    String start = text.substring(0, next + 1);
//                    text = start + newRest;
//                    changed = true;
//                }
//            }
//        }
//        return text;
//    }


//    public static String preprocessFiller(OWLModel owlModel, String text) {
//        text = text.trim();
//        if (text.length() > 0 && text.charAt(0) >= 'a' && text.charAt(0) <= 'z') {
//            int index = 1;
//            while (index < text.length() && text.charAt(index) >= 'a' && text.charAt(index) <= 'z') {
//                index++;
//            }
//            String sub = text.substring(0, index);
//            if (sub.equals("xsd")) {
//                return text;
//            }
//            else {
//                String name = "xsd:" + sub;
//                if (owlModel.getRDFSDatatypeByName(name) != null) {
//                    return name + text.substring(index);
//                }
//            }
//        }
//        return text;
//    }
}
