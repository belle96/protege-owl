package edu.stanford.smi.protegex.owl.inference.dig.exception;

import java.util.ArrayList;
import java.util.Collection;


/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 14, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGErrorException extends DIGReasonerException {

    private ArrayList errorList;


    public DIGErrorException(ArrayList errorList) {
        super(((DIGError) (errorList.get(0))).getMessage());

        this.errorList = errorList;
    }


    /**
     * Gets the exception code associated with this
     * DIGErrorException
     *
     * @return An integer exception code.
     */
    public int getErrorCode(int index) {
        return ((DIGError) errorList.get(index)).getErrorCode();
    }


    /**
     * Gets the exception message of the specified
     * exception.
     *
     * @param index The index of the exception
     * @return The exception message
     */
    public String getMessage(int index) {
        return ((DIGError) errorList.get(index)).getMessage();
    }


    /**
     * Gets the <code>DIGError</code> object
     * associated with this exception.
     */
    public DIGError getDIGError(int index) {
        return (DIGError) errorList.get(index);
    }


    /**
     * Gets the number of DIGErrors
     */
    public int getNumberOfErrors() {
        return errorList.size();
    }


    public Collection getErrors() {
        return errorList;
    }
}

