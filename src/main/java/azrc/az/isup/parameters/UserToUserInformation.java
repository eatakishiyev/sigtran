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
public class UserToUserInformation implements IsupParameter {

    private byte[] userToUserInformation;

    public UserToUserInformation() {
    }

    public UserToUserInformation(byte[] userToUserInformation) {
        this.userToUserInformation = userToUserInformation;
    }

    @Override
    public byte[] encode() {
        return this.userToUserInformation;
    }

    @Override
    public void decode(byte[] data) {
        this.userToUserInformation = data;
    }

    public byte[] getUserToUserInformation() {
        return userToUserInformation;
    }

    public void setUserToUserInformation(byte[] userToUserInformation) {
        this.userToUserInformation = userToUserInformation;
    }

    @Override
    public int getParameterCode() {
        return USER_TO_USER_INFORMATION;
    }

}
