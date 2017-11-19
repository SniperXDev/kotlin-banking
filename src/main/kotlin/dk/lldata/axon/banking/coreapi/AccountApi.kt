package dk.lldata.axon.banking.coreapi

class CreateAccountCommand(val accountId : String, val overdraftLimit : Int)
class WithdrawMoneyCommand(val accountId : String, val amount : Int)

class AccountCreatedEvent(val accountId: String, val overdraftLimit: Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance : Int)

