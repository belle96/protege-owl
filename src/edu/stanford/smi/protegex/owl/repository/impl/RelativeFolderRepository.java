package edu.stanford.smi.protegex.owl.repository.impl;

import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: matthewhorridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Sep 26, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RelativeFolderRepository extends LocalFolderRepository {

    private String relativeURL;


    public RelativeFolderRepository(URL baseURL, String relativeURL) throws MalformedURLException,
            URISyntaxException {
        this(baseURL, relativeURL, RepositoryUtil.isForcedToBeReadOnly(getURI(baseURL, relativeURL).getQuery()));
    }


    public RelativeFolderRepository(URL baseURL, String relativeURL, boolean forceReadOnly) throws MalformedURLException,
            URISyntaxException {

        super(new File(getURI(baseURL, relativeURL).getPath()),
                forceReadOnly);
        this.relativeURL = RepositoryUtil.stripQuery(relativeURL);
    }


    private static URI getURI(URL baseURL, String relativeURL) throws MalformedURLException,
            URISyntaxException {
        return new URI(new URL(baseURL, relativeURL).toString());
    }





    public String getRepositoryDescriptor() {
        try {
            URI uri = new URI(relativeURL);
            return uri.toString() + "?" + RepositoryUtil.FORCE_READ_ONLY_FLAG + "=" + isForceReadOnly();
        }
        catch (URISyntaxException e) {
            return "";
        }
    }


    public String getRepositoryDescription() {
        return "Relative URL: " + relativeURL + "  (" + getFile().toString() + ")";
    }
}

