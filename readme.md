
# TASK

Design and implement a RESTful API (including data model and the backing implementation) for money
transfers between accounts.
Explicit requirements:
1 - keep it simple and to the point (e.g. no need to implement any authentication, assume the APi is
invoked by another internal system/service)
2 - use whatever frameworks/libraries you like (except Spring, sorry!) but don't forget about the
requirement #1
3 - the datastore should run in-memory for the sake of this test
4 - the final result should be executable as a standalone program (should not require a pre-installed
container/server)
5 - demonstrate with test that the API works as expected
Implicit requirements:
1 - the code produced by you is expected to be of high quality.
2 - there are no detailed requirements, use common sense.

# API
## Get info for one account by id. 
### Request
```
/transfer?accountId=1
```
### Response
```
[{"id":"<id>", "amount":"<amount>"}]
```
## Do money transfer between two accounts by ids
### Request
```
/transfer?outgoingAccount=<accountId1>&ingoingAccount=<accountId2>&amount=<amount for transfer>
```
### Response
```
[{"id":"<id>", "amount":"<amount>"},{"id":"<id>", "amount":"<amount>"}]
```