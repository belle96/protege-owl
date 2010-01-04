package edu.stanford.smi.protegex.owl.jena.parser;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import edu.stanford.smi.protege.util.AbstractFormatter;

public class TriplePostProcessorLogFormatter extends AbstractFormatter {

	@Override
	public String format(LogRecord record) {
		boolean showMethod = record.getLevel().intValue() >= Level.WARNING.intValue();
		return format(record, null, false, showMethod, false, false);
	}

}
