package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.MoneyTransferRequestedEvent
import dk.lldata.axon.banking.coreapi.WithdrawMoneyCommand
import org.axonframework.test.saga.SagaTestFixture
import org.junit.Test

class MoneyTransferSagaTest {
  val fixture : SagaTestFixture<MoneyTransferSaga>

  constructor() {
    fixture = SagaTestFixture(MoneyTransferSaga::class.java)
  }

  @Test
  fun moneyTransferRequestTest() {
    fixture.givenNoPriorActivity()
        .whenPublishingA(MoneyTransferRequestedEvent("tf1", "a1", "a2", 100))
        .expectActiveSagas(1)
        .expectDispatchedCommands(WithdrawMoneyCommand("a1", 100))
  }
}