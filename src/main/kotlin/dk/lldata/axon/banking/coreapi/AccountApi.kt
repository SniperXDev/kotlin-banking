package dk.lldata.axon.banking.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

class CreateAccountCommand(val accountId : String, val overdraftLimit : Int)
class WithdrawMoneyCommand(@TargetAggregateIdentifier val accountId : String, val amount : Int)

class AccountCreatedEvent(val accountId: String, val overdraftLimit: Int)
class MoneyWithdrawnEvent(val accountId: String, val amount: Int, val balance : Int)

