package dk.lldata.axon.banking.rest

import dk.lldata.axon.banking.balance.AccountBalanceRepository
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.ID
import dk.lldata.axon.banking.coreapi.RequestMoneyTransferCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/test"])
class TestRestService(
    val commandGateway: CommandGateway,
    val accountBalanceRepository: AccountBalanceRepository
) {

  @RequestMapping(path = ["/create/{accountId}"], method = [RequestMethod.GET])
  fun create(@PathVariable accountId: String) {
    commandGateway.send<CreateAccountCommand>(CreateAccountCommand(accountId, 1000))
  }

  @RequestMapping(path = ["/transfer/{from}/{to}"], method = [RequestMethod.GET])
  fun transfer(@PathVariable from: String, @PathVariable to: String) {
    commandGateway.send<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), from, to, 1 + (Math.random() * 20).toInt()))
  }

  @RequestMapping(path = ["/balance/{accountId}"], method = [RequestMethod.GET])
  fun balance(@PathVariable accountId: String) = accountBalanceRepository.findById(accountId).get()

  @RequestMapping(path = ["/hello"], method = [RequestMethod.GET])
  fun hello() = "OK"
}