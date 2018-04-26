package ru.mccarl.moneytransfer.api.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import ru.mccarl.moneytransfer.api.dao.mapper.AccountMapper;

import java.util.List;

@RegisterMapper(AccountMapper.class)
public interface AccountDao {
    @SqlQuery("select * from ACCOUNTS where ID = :id")
    Account findById(@Bind("id") int id);

    @SqlQuery("select * from ACCOUNTS where ID in (:id_1,:id_2)")
    List<Account> findByIds(@Bind("id_1") int id1, @Bind("id_2") int id2);

    @SqlUpdate("UPDATE ACCOUNTS\n" +
            "SET AMOUNT = CASE\n" +
            "             WHEN ID = :id_1 THEN AMOUNT - :amount\n" +
            "             WHEN ID = :id_2 THEN AMOUNT + :amount\n" +
            "            ELSE AMOUNT\n" +
            "              END;")
    void doTransfer(@Bind("id_1") int outgoingAccount,
                       @Bind("id_2") int ingoingAccount,
                       @Bind("amount") int amount);
}
