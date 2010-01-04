package edu.stanford.smi.protegex.owl.repository.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileInputSource implements InputStreamSource {
    private File f;
    
    public FileInputSource(File f) {
        this.f = f;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(f);
    }

    public URL getURL() {
        try {
            return f.toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

}
