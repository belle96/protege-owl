package edu.stanford.smi.protegex.owl.model.classparser.dl;

import edu.stanford.smi.protegex.owl.model.classdisplay.compact.CompactOWLClassDisplay;

import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jan 25, 2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DLSyntaxParserUtil {
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
}
