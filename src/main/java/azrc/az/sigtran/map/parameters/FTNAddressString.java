/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

/**
 * FTN-AddressString ::=
 * AddressString (SIZE (1..maxFTN-AddressLength))
 * -- This type is used to represent forwarded-to numbers.
 * -- If NAI = international the first digits represent the country code (CC)
 * -- and the network destination code (NDC) as for E.164.
 *
 * @author eatakishiyev
 */
public class FTNAddressString extends AddressStringImpl {

}
