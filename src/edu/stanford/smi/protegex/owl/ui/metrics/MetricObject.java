package edu.stanford.smi.protegex.owl.ui.metrics;

/**
 * User: matthewhorridge<br>
 * The Univeristy Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: Apr 20, 2005<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class MetricObject {

    private String metricName;

    private Object value;


    public MetricObject(String metricName,
                        Object value) {
        this.metricName = metricName;
        this.value = value;
    }


    public MetricObject(String metricName,
                        int value) {
        this.metricName = metricName;
        this.value = new Integer(value);
    }


    public String getMetricName() {
        return metricName;
    }


    public Object getValue() {
        return value;
    }


    public String toString() {
        return metricName + ": " + value.toString();
    }
}

