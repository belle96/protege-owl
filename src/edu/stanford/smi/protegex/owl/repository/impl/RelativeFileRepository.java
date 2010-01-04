package edu.stanford.smi.protegex.owl.repository.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import edu.stanford.smi.protegex.owl.repository.util.RepositoryUtil;


public class RelativeFileRepository extends LocalFileRepository {

    private String relativeURL;

    public RelativeFileRepository(File file, URL baseURL, String relativeURL) throws MalformedURLException, URISyntaxException {
		super(file, RepositoryUtil.isForcedToBeReadOnly(RepositoryUtil.getURI(baseURL, relativeURL).getQuery()));
		this.relativeURL = RepositoryUtil.stripQuery(relativeURL);
	}

    public RelativeFileRepository(File file, String relativeURL, boolean isForceReadOnly) throws MalformedURLException, URISyntaxException {
		super(file, isForceReadOnly);
		this.relativeURL = RepositoryUtil.stripQuery(relativeURL);
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
        return "Relative file URL: " + relativeURL + "  (" + getFile().toString() + ")";
    }
}

