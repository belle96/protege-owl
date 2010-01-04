/*
 * Created on Mar 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.stanford.smi.protegex.owlx.examples.javaDemo;

import junit.framework.TestCase;

/**
 * @author rouquett
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class JavaDemoApplicationTest extends TestCase {

    public void testMain() {
        try {
            JavaDemoApplication.main(null);
        }
        catch (Exception e) {
            fail(e.toString());
        }
    }

}
