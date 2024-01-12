/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters.depricated;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class TP_VP {

    private long validityPeriod;
    private Metric metric;
    private TP_VPFormat format;

    public TP_VP(Metric metric, TP_VPFormat format) {
        this.metric = metric;
        this.format = format;
    }

    public void encode(AsnOutputStream aos) throws Exception {
        switch (format) {
            case relativeFormat:
                encode_relative(aos);
                break;
            case absoluteFormat:
                encode_absolute(aos);
                break;
            case enhancedFormat:
//                encode_enhanced(aos);
                break;
        }
    }

    private void decode_relative(AsnInputStream ais) throws IOException {
        int i = ais.read();
        switch (getMetric()) {
            case minutes:
                this.validityPeriod = (i + 1) * 5;
                break;
            case hours:
                this.validityPeriod = 720 + (i - 143) * 30;
                break;
            case weeks:
                this.validityPeriod = (i - 192) * 10080;
                break;
        }
    }

    private void encode_relative(AsnOutputStream aos) {
        switch (getMetric()) {
            case minutes:
                int i = (int) Math.ceil(validityPeriod / 5.0) - 1;
                aos.write(i);
                break;
            case hours:
                i = (int) ((Math.ceil(validityPeriod - 720) / 30.0) + 143);
                aos.write(i);
                break;
            case weeks:
                i = (int) (Math.ceil(validityPeriod / 10080.0) + 192);
                aos.write(i);
                break;
        }
    }

    public void decode(AsnInputStream ais) throws IOException {
        switch (format) {
            case relativeFormat:
                decode_relative(ais);
                break;
            case absoluteFormat:
//                decode_absolute(ais);
                break;
            case enhancedFormat:
//                decode_enhanced(ais);
                break;
        }

    }

    /**
     * @return the validityPeriod
     */
    public long getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod
     */
    public void setValidityPeriod(long validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return the metric
     */
    public Metric getMetric() {
        return metric;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(Metric metric) {
        this.metric = metric;




    }

    private void encode_absolute(AsnOutputStream aos) {
    }

    public enum Metric {

        minutes,
        hours,
        weeks;
    }

    public enum TP_VPFormat {

        relativeFormat,
        absoluteFormat,
        enhancedFormat
    }
}
