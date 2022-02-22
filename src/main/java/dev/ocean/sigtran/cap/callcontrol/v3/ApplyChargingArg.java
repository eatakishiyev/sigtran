/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v3;


/**
 * ApplyChargingArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
 * aChBillingChargingCharacteristics [0]
 * AChBillingChargingCharacteristics{bound}, partyToCharge [2] SendingSideID
 * DEFAULT sendingSideID : leg1, extensions [3] Extensions {bound} OPTIONAL,
 * aChChargingAddress [50] AChChargingAddress {bound} DEFAULT
 * legID:sendingSideID:leg1, ... }
 *
 * @author eatakishiyev
 */
public class ApplyChargingArg extends dev.ocean.sigtran.cap.callcontrol.v2.ApplyChargingArg {

}
