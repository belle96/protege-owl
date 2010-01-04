package edu.stanford.smi.protegex.owl.inference.dig.exception;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Jun 19, 2004<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class DIGError {

    private String message;

    private int errorCode;

    private String id;


    public DIGError(String id, String message, int errorCode) {
        this.id = id;
        this.message = message;
        this.errorCode = errorCode;
    }


    public DIGError(String id, String message, String errorCode) {
        this.message = message;
        this.id = id;
        if (errorCode != null) {
            try {
                this.errorCode = Integer.parseInt(errorCode);
            }
            catch (NumberFormatException nfEx) {
                this.errorCode = -1;
            }
        }
        else {
            this.errorCode = -1;
        }
    }


    public String getMessage() {
        return message;
    }


    public int getErrorCode() {
        return errorCode;
    }


    public String getID() {
        return id;
    }
}

