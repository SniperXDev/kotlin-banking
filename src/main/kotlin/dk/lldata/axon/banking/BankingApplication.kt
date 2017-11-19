package dk.lldata.axon.banking

import org.axonframework.commandhandling.CommandBus
import org.axonframework.spring.config.EnableAxon
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BankingApplication {

}

fun main(args: Array<String>) {
  val cfg = runApplication<BankingApplication>(*args)
  val commandBus = cfg.getBean(CommandBus::class.java)
  //commandBus.dispatch(asCommandMessage<CreateAccountCommand>(CreateAccountCommand("a1", 1000)), LoggingCallback.INSTANCE)
  //commandBus.dispatch(asCommandMessage<CreateAccountCommand>(CreateAccountCommand("a2", 1000)), LoggingCallback.INSTANCE)
  //commandBus.dispatch(asCommandMessage<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), "a1", "a2", 4000)), LoggingCallback.INSTANCE)
}

