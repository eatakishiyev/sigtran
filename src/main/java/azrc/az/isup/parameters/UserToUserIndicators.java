/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

/**
 *
 * @author eatakishiyev
 */
public class UserToUserIndicators implements IsupParameter {

    private boolean request;

    private int serviceOne;
    private int serviceTwo;
    private int serviceThree;

    private int networkDiscardIndicator;

    public UserToUserIndicators() {
    }

    /**
     * Create new Request type User-To-User indicators
     *
     * @param serviceOne 0-No information, 1-Spare, 2-Request, not essential, 3-Request, essential
     * @param serviceTwo 0-No information, 1-Spare, 2-Request, not essential, 3-Request, essential
     * @param serviceThree 0-No information, 1-Spare, 2-Request,not essential, 3-Request, essential
     */
    public UserToUserIndicators(int serviceOne, int serviceTwo, int serviceThree) {
        this.request = true;
        this.serviceOne = serviceOne;
        this.serviceTwo = serviceTwo;
        this.serviceThree = serviceThree;
    }

    /**
     * Create new Response type User-To-User indicators
     *
     * @param serviceOne 0-No information, 1-Not provided, 2-Provided, 3-Spare
     * @param serviceTwo 0-No information, 1-Not provided, 2-Provided, 3-Spare
     * @param serviceThree 0-No information, 1-Not provided, 2-Provided, 3-Spare
     * @param networkDiscardIndicator 0-No information, 1-User-to-user information discarded by the network
     */
    public UserToUserIndicators(int serviceOne, int serviceTwo, int serviceThree, int networkDiscardIndicator) {
        this.request = false;
        this.serviceOne = serviceOne;
        this.serviceTwo = serviceTwo;
        this.serviceThree = serviceThree;
        this.networkDiscardIndicator = networkDiscardIndicator;
    }

    @Override
    public byte[] encode() {

        int result = networkDiscardIndicator;
        result = (result << 2) | serviceThree;
        result = (result << 2) | serviceTwo;
        result = (result << 2) | serviceOne;
        result = (result << 1) | (request ? 0 : 1);
        return new byte[]{(byte) (result)};
    }

    @Override
    public void decode(byte[] data) {
        int value = data[0] & 0xFF;
        this.networkDiscardIndicator = (value >> 7) & 0b00000001;
        this.serviceThree = (value >> 5) & 0b00000011;
        this.serviceTwo = (value >> 3) & 0b00000011;
        this.serviceOne = (value >> 1) & 0b00000011;
        this.request = (value & 0b00000001) == 0;
    }

    /**
     * In case of request :0-No information,1-Spare,2-Request, not essential,3-Request, essential.
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     *
     * @return Service1 value
     */
    public int getServiceOne() {
        return serviceOne;
    }

    /**
     *
     * @param serviceOne In case of request :0-No information,1-Spare,2-Request, not essential,3-Request, essential.
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     */
    public void setServiceOne(int serviceOne) {
        this.serviceOne = serviceOne;
    }

    /**
     * In case of request :No information,1-Spare,2-Request, not essential,3-Request,essential
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     *
     * @return Service2 value
     */
    public int getServiceTwo() {
        return serviceTwo;
    }

    /**
     * @param serviceTwo In case of request :No information,1-Spare,2-Request, not essential,3-Request,essential
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     *
     */
    public void setServiceTwo(int serviceTwo) {
        this.serviceTwo = serviceTwo;
    }

    /**
     * In case of request :No information,1-Spare,2-Request, not essential,3-Request,essential
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     *
     * @return Service3 value
     */
    public int getServiceThree() {
        return serviceThree;
    }

    /**
     * @param serviceThree In case of request :No information,1-Spare,2-Request, not essential,3-Request,essential
     * In case of response:0-No information,1-Not-provided,2-Provided,3-Spare.
     *
     */
    public void setServiceThree(int serviceThree) {
        this.serviceThree = serviceThree;
    }

    /**
     *
     * @return In case response 0- No information, 1-User-to-user information discarded by the network
     */
    public int getNetworkDiscardIndicator() {
        return networkDiscardIndicator;
    }

    /**
     *
     * @param networkDiscardIndicator In case response 0- No information, 1-User-to-user information discarded by the network
     */
    public void setNetworkDiscardIndicator(int networkDiscardIndicator) {
        this.networkDiscardIndicator = networkDiscardIndicator;
    }

    public boolean isRequest() {
        return request;
    }

    @Override
    public int getParameterCode() {
        return USER_TO_USER_INDICATORS;
    }

}
