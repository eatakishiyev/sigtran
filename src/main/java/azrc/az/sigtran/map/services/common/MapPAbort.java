/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.common;

/**
 *
 * @author root
 */
public class MapPAbort {

    private ProviderReason providerReason;
    private Source source;

    /**
     * @return the providerReason
     */
    public ProviderReason getProviderReason() {
        return providerReason;
    }

    /**
     * @param providerReason the providerReason to set
     */
    public void setProviderReason(ProviderReason providerReason) {
        this.providerReason = providerReason;
    }

    /**
     * @return the source
     */
    public Source getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("MapPAbort[")
                .append("ProviderReason = ").append(providerReason)
                .append(";Source = ").append(source)
                .append("]").toString();

    }

    public enum ProviderReason {

        PROVIDER_MALFUNCTION(700),
        SUPPORTING_DIALOGUE_RELEASED(701),
        RESOURCE_LIMITATION(702),
        VERSION_INCOMPATIBILTY(703),
        ABNORMAL_MAP_DIALOGUE(704);

        private int value;

        private ProviderReason(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }

    public enum Source {

        MAP_PROBLEM,
        TC_PROBLEM,
        NETWROK_SERVICE_PROBLEM;
    }
}
