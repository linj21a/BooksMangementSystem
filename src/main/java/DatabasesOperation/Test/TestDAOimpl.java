package DatabasesOperation.Test;

import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;


/**
 * 测试类，用于测试每个DAO实现类的CRUD是否可以实现。
 * 已经通过测试：books表, user表、Reader表、borrow表
 */

public class TestDAOimpl {
    public static void main(String[] args) {
        int id = DAOUser.register("Amy", "");
        System.out.println(id);

    }
}
