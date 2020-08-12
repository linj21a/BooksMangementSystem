package GUIModule.Factory;

import GUIModule.Register.UserRegister;

import javax.swing.*;

public class GUIFactory {
    private static UserRegister userRegister=null;

    public static UserRegister getUserRegister(JFrame jFrame) {
        if(userRegister==null){
            synchronized (GUIFactory.class){
                if(userRegister==null)
                    userRegister = new UserRegister(jFrame);
            }
        }
        return userRegister;

    }
}
