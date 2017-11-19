package dk.lldata.axon.banking.rest

import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.ID
import dk.lldata.axon.banking.coreapi.RequestMoneyTransferCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path= arrayOf("/test"))
class TestRestService(val commandGateway: CommandGateway) {
  @Transactional
  @RequestMapping(path= arrayOf("/create/{accountId}"), method = arrayOf(RequestMethod.GET))
  fun create(@PathVariable accountId : String) {
    commandGateway.send<CreateAccountCommand>(CreateAccountCommand(accountId, 1000))
  }

  @RequestMapping(path= arrayOf("/transfer/{from}/{to}"), method = arrayOf(RequestMethod.GET))
  fun transfer(@PathVariable from : String, @PathVariable to : String) {
    commandGateway.send<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), from, to, 600))
  }

  @RequestMapping(path= arrayOf("/hello"), method = arrayOf(RequestMethod.GET))
  fun hello() : String {
    return "OK"
  }
}