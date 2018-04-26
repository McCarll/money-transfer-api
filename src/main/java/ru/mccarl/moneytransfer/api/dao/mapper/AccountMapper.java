package ru.mccarl.moneytransfer.api.dao.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import ru.mccarl.moneytransfer.api.dao.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements ResultSetMapper<Account> {
    public Account map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Account(resultSet.getInt("ID"), resultSet.getLong("amount"));
    }
}