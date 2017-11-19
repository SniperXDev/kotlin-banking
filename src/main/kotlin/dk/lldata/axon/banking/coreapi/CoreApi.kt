package dk.lldata.axon.banking.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

// Account
class CreateAccountCommand(val accountId : String, val overdraftLimit : Int)
class WithdrawMoneyCommand(@TargetAggregateIdentifier val accountId : String, val txId : String, val amount : Int)
class DepositMoneyCommand(@TargetAggregateIdentifier val accountId : String, val txId : String, val amount : Int)

abstract class BalanceUpdatedEvent(val accountId : String, val balance : Int)
class AccountCreatedEvent(accountId: String, val overdraftLimit: Int, balance : Int) : BalanceUpdatedEvent(accountId, balance)
class MoneyWithdrawnEvent(accountId: String, val txId : String, val amount: Int, balance: Int) : BalanceUpdatedEvent(accountId, balance)
class MoneyDepositedEvent(accountId: String, val txId : String, val amount: Int, balance: Int) : BalanceUpdatedEvent(accountId, balance)

// MoneyTransfer
class RequestMoneyTransferCommand(val transferId : String, val sourceAccountId : String, val targetAccountId : String, val amount: Int)
class CompleteMoneyTransferCommand(@TargetAggregateIdentifier val transferId : String)
class CancelMoneyTransferCommand(@TargetAggregateIdentifier val transferId : String)

class MoneyTransferRequestedEvent(val transferId : String, val sourceAccountId : String, val targetAccountId : String, val amount: Int)
class MoneyTransferCompletedEvent(val transferId: String)
class MoneyTransferCancelledEvent(val transferId: String)
