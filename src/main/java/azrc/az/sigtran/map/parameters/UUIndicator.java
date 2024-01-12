/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class UUIndicator {

    private Type type;
    private Service requestService1;
    private Service requestService2;
    private Service requestService3;
    private Service responseService1;
    private Service responseService2;
    private Service responseService3;
    private NetworkDiscardIndicator networkDiscardIndicator;

    public UUIndicator() {
    }

    public UUIndicator(Service requestService1, Service requestService2, Service requestService3) {
        this.type = Type.REQUEST;
        this.requestService1 = requestService1;
        this.requestService2 = requestService2;
        this.requestService3 = requestService3;
    }

    public UUIndicator(Service responseService1, Service responseService2, Service responseService3, NetworkDiscardIndicator networkDiscardIndicator) {
        this.type = Type.RESPONSE;
        this.responseService1 = responseService1;
        this.responseService2 = responseService2;
        this.responseService3 = responseService3;
        this.networkDiscardIndicator = networkDiscardIndicator;
    }

    public void encode(ByteArrayOutputStream baos) {
        int b;
        if (type == Type.REQUEST) {
            b = 0;//spare
            b = (b << 2) | requestService3.ordinal();
            b = (b << 2) | requestService2.ordinal();
            b = (b << 2) | requestService1.ordinal();
            b = (b << 1) | type.ordinal();

        } else {
            b = networkDiscardIndicator.ordinal();
            b = (b << 2) | requestService3.ordinal();
            b = (b << 2) | requestService2.ordinal();
            b = (b << 2) | requestService1.ordinal();
            b = (b << 1) | type.ordinal();
        }
        baos.write(b);
    }

    public void decode(ByteArrayInputStream bais) {
        int b = bais.read() & 0xFF;
        this.type = Type.valueOf(b & 0b00000001);
        if (type == Type.REQUEST) {
            this.requestService1 = Service.valueOf((b >> 1) & 0b00000011);
            this.requestService2 = Service.valueOf((b >> 3) & 0b00000011);
            this.requestService3 = Service.valueOf((b >> 5) & 0b00000011);
        } else {
            this.responseService1 = Service.valueOf((b >> 1) & 0b00000011);
            this.responseService1 = Service.valueOf((b >> 3) & 0b00000011);
            this.responseService1 = Service.valueOf((b >> 5) & 0b00000011);
            this.networkDiscardIndicator = NetworkDiscardIndicator.valueOf(b >> 7);
        }
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the requestService1
     */
    public Service getRequestService1() {
        return requestService1;
    }

    /**
     * @return the requestService2
     */
    public Service getRequestService2() {
        return requestService2;
    }

    /**
     * @return the requestService3
     */
    public Service getRequestService3() {
        return requestService3;
    }

    /**
     * @return the responseService1
     */
    public Service getResponseService1() {
        return responseService1;
    }

    /**
     * @return the responseService2
     */
    public Service getResponseService2() {
        return responseService2;
    }

    /**
     * @return the responseService3
     */
    public Service getResponseService3() {
        return responseService3;
    }

    /**
     * @return the networkDiscardIndicator
     */
    public NetworkDiscardIndicator getNetworkDiscardIndicator() {
        return networkDiscardIndicator;
    }

    public enum Type {

        REQUEST,
        RESPONSE;

        public static Type valueOf(int value) {
            return value == 0 ? REQUEST : RESPONSE;
        }
    }

    public enum Service {

        NO_INFORMATION,
        NOT_PROVIDED,
        PROVIDED,
        SPARE;

        public static Service valueOf(int value) {
            switch (value) {
                case 0:
                    return NO_INFORMATION;
                case 1:
                    return NOT_PROVIDED;
                case 2:
                    return PROVIDED;
                default:
                    return SPARE;
            }
        }
    }

    public enum NetworkDiscardIndicator {

        NO_INFORMATION,
        USERT_TO_USER_INFORMATION_DISCARDED_BY_NETWORK;

        public static NetworkDiscardIndicator valueOf(int value) {
            return value == 0 ? NO_INFORMATION : USERT_TO_USER_INFORMATION_DISCARDED_BY_NETWORK;
        }
    }
}
