package edu.stanford.smi.protegex.owl.inference.protegeowl.task.protegereasoner;

import java.util.Collection;

import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecord;
import edu.stanford.smi.protegex.owl.inference.protegeowl.log.ReasonerLogRecordFactory;
import edu.stanford.smi.protegex.owl.inference.reasoner.ProtegeReasoner;
import edu.stanford.smi.protegex.owl.inference.reasoner.exception.ProtegeReasonerException;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;

public class GetIndividualPropertyValuesTask extends AbstractReasonerTask{
	private OWLIndividual individual;

	private ProtegeReasoner protegeReasoner;

	public GetIndividualPropertyValuesTask(OWLIndividual individual,
			ProtegeReasoner protegeReasoner) {
		super(protegeReasoner);
		this.individual = individual;
		this.protegeReasoner = protegeReasoner;
	}

	public int getTaskSize() {
		return 1;
	}

	@SuppressWarnings("deprecation")
	public void run() throws ProtegeReasonerException {
		ReasonerLogRecordFactory logRecordFactory = ReasonerLogRecordFactory.getInstance();

		setDescription("Getting properties values for individual");
		setProgress(0);

		setMessage("Querying reasoner...");

		ReasonerLogRecord parentRecord = logRecordFactory.createInformationMessageLogRecord("Inferred properties values for: " + individual.getBrowserText(), null);
		postLogRecord(parentRecord);

		Collection<Slot> slots = individual.getOwnSlots();

		for (Slot slot : slots) {
			if (!slot.isSystem() && slot instanceof RDFProperty) {
				RDFProperty prop = (RDFProperty) slot;

				if (prop instanceof OWLObjectProperty) {
					Collection<OWLIndividual> values = protegeReasoner.getRelatedIndividuals(individual, (OWLObjectProperty) prop);
					if (values != null && values.size() > 0) {
						ReasonerLogRecord propLogRecord = logRecordFactory.createOWLPropertyLogRecord(prop, parentRecord);
						postLogRecord(propLogRecord);

						for (OWLIndividual individual : values) {
							postLogRecord(logRecordFactory.createOWLInstanceLogRecord(individual, propLogRecord));
						}
					}
				} else if (prop instanceof OWLDatatypeProperty) {
					Collection values = protegeReasoner.getRelatedValues(individual, (OWLDatatypeProperty) prop);
					if (values != null && values.size() > 0) {
						ReasonerLogRecord propLogRecord = logRecordFactory.createOWLPropertyLogRecord(prop, parentRecord);
						postLogRecord(propLogRecord);

						for (Object value : values) {
							String message = value.toString();
							
							if (value instanceof RDFSLiteral) {
								message = ((RDFSLiteral)value).getBrowserText();
							}
							
							postLogRecord(logRecordFactory.createInformationMessageLogRecord(message, propLogRecord));
						}
					}
				}			
			}
		}

		setMessage("Finished");

		setTaskCompleted();		
	}

}
