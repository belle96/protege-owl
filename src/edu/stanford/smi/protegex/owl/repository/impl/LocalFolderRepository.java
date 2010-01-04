package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.repository.util.FileInputSource;
import edu.stanford.smi.protegex.owl.repository.util.OntologyNameExtractor;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 12, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class LocalFolderRepository extends AbstractLocalRepository {
    private static transient Logger log = Log.getLogger(LocalFolderRepository.class);

	public static final String RECURSIVE_FLAG = "Recursive";

	private boolean recursive;

    public LocalFolderRepository(File folder) {
        this(folder, false);
    }


    public LocalFolderRepository(File folder, boolean forceReadOnly) {
        this(folder, forceReadOnly, false);
    }

	public LocalFolderRepository(File folder, boolean forceReadOnly, boolean recursive) {
		super(folder, forceReadOnly);
		this.recursive = recursive;
		refresh();
	}

    public void refresh() {
        super.refresh();
        update();
    }


    private void update() {
        if (getFile().isDirectory() == false) {        	
	            Log.getLogger().warning("[Local Folder Repository] The specified file must be a directory. (" + getFile() + ")");
	        return;
        }
	    update(getFile());
    }

	private void update(File file) {
		File [] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File currentFile = files[i];
	        if(currentFile.isFile()) {
				try {
					checkFile(currentFile);
				}
				catch (IOException e) {
                                  Log.emptyCatchBlock(e);
				}
	        }
	        else if(recursive && currentFile.isDirectory() && currentFile.isHidden() == false) {
		        update(currentFile);
	        }
        }
	}


    private void checkFile(File file) throws IOException {
        if (file.getName().endsWith(".owl") && file.isFile()) {
            OntologyNameExtractor extractor = new OntologyNameExtractor(new FileInputSource(file));
            if (extractor.getOntologyName() != null) {
                URI name = extractor.getOntologyName();
                putOntology(name, file);
            }
        }
    }


	public String getRepositoryDescriptor() {
		String descriptor = super.getRepositoryDescriptor();
		if(descriptor != null) {
			descriptor += "&" + RECURSIVE_FLAG + "=" + Boolean.toString(recursive);
		}
		return descriptor;
	}


    public String getRepositoryDescription() {
        String description = "Local folder ";
        return description += getFile().getPath();
    }

}

