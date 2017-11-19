package dk.lldata.axon.banking.rest

import dk.lldata.axon.banking.balance.AccountBalance
import dk.lldata.axon.banking.balance.AccountBalanceRepository
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.ID
import dk.lldata.axon.banking.coreapi.RequestMoneyTransferCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.annotation.PostConstruct

@RestController
@RequestMapping(path= arrayOf("/test"))
class TestRestService(
    val commandGateway: CommandGateway,
    val accountBalanceRepository: AccountBalanceRepository
) {

  @RequestMapping(path= arrayOf("/init"), method = arrayOf(RequestMethod.GET))
  fun init(@PathVariable accountId : String) {
    commandGateway.send<CreateAccountCommand>(CreateAccountCommand("a1", 1000))
    commandGateway.send<CreateAccountCommand>(CreateAccountCommand("a2", 1000))
    commandGateway.send<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), "a1", "a2", 600))
  }

  @RequestMapping(path= arrayOf("/create/{accountId}"), method = arrayOf(RequestMethod.GET))
  fun create(@PathVariable accountId : String) {
    commandGateway.send<CreateAccountCommand>(CreateAccountCommand(accountId, 1000))
  }

  @RequestMapping(path= arrayOf("/transfer/{from}/{to}"), method = arrayOf(RequestMethod.GET))
  fun transfer(@PathVariable from : String, @PathVariable to : String) {
    commandGateway.send<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), from, to, 1+(Math.random() * 20).toInt()))
  }

  @RequestMapping(path= arrayOf("/balance/{accountId}"), method = arrayOf(RequestMethod.GET))
  fun balance(@PathVariable accountId : String) : AccountBalance {
    return accountBalanceRepository.findById(accountId).get()
  }

  @RequestMapping(path= arrayOf("/hello"), method = arrayOf(RequestMethod.GET))
  fun hello() : String {
    return "OK"
  }
}