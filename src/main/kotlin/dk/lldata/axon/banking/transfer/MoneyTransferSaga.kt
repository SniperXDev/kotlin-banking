package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.MoneyTransferRequestedEvent
import dk.lldata.axon.banking.coreapi.WithdrawMoneyCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.StartSaga
import org.springframework.beans.factory.annotation.Autowired

class MoneyTransferSaga {

  @Autowired @Transient
  var commandGateway : CommandGateway? = null

  @StartSaga
  @SagaEventHandler(associationProperty = "transferId")
  fun on(event : MoneyTransferRequestedEvent) {
    commandGateway!!.send<WithdrawMoneyCommand>(WithdrawMoneyCommand(event.sourceAccountId, event.amount))
  }
}