package ru.mccarl.moneytransfer.api.controller;

import ru.mccarl.moneytransfer.api.dao.Account;
import ru.mccarl.moneytransfer.api.dao.AccountDao;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/transfer")
public class RESTClientController {

    AccountDao accountDao;

    private Client client;

    public RESTClientController(AccountDao personDAO) {
        this.accountDao = personDAO;
    }

    public RESTClientController(Client client) {
        this.client = client;
    }

    @GET
    public Account getAccount(@QueryParam("accountId") Integer accountId) {
        return accountDao.findById(accountId);
    }

//    @POST
//    public List<Account> doTransfer(@QueryParam("outgoingAccount") Integer outgoingAccount,
//                                    @QueryParam("ingoingAccount") Integer ingoingAccount,
//                                    @QueryParam("amount") Integer amount) {
//        accountDao.doTransfer(outgoingAccount, ingoingAccount, amount);
//        return accountDao.findByIds(outgoingAccount, ingoingAccount);
//    }

    @POST
//    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Account> doTransfer(@QueryParam("outgoingAccount") Integer outgoingAccount,
                               @QueryParam("ingoingAccount") Integer ingoingAccount,
                               @QueryParam("amount") Integer amount) {
        accountDao.doTransfer(outgoingAccount, ingoingAccount, amount);
//        return Response.status(Response.Status.OK).entity(accountDao.findByIds(outgoingAccount, ingoingAccount)).build();
        return accountDao.findByIds(outgoingAccount, ingoingAccount);
    }

}