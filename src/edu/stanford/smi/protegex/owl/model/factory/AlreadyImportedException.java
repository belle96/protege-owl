package edu.stanford.smi.protegex.owl.model.factory;

public class AlreadyImportedException extends Exception {
    private static final long serialVersionUID = -9141966184981940921L;

    public AlreadyImportedException() {

    }
    
    public AlreadyImportedException(String message) {
        super(message);
    }
    
    public AlreadyImportedException(String message, Throwable t) {
        super(message, t);
    }
    
    public AlreadyImportedException(Throwable t) {
        super(t.getMessage(), t);
    }

}
