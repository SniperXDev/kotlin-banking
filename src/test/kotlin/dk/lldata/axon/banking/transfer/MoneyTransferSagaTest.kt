package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.*
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
        .expectDispatchedCommands(WithdrawMoneyCommand("a1", "tf1",100))
  }

  @Test
  fun depositAfterWithdraw() {
    fixture
        .givenAPublished(MoneyTransferRequestedEvent("tf1", "a1", "a2", 100))
        .whenPublishingA(MoneyWithdrawnEvent("a1", "tf1",100, 500))
        .expectDispatchedCommands(DepositMoneyCommand("a2", "tf1",100))
  }

  @Test
  fun transferCompleteAfterDeposit() {
    fixture
        .givenAPublished(MoneyTransferRequestedEvent("tf1", "a1", "a2", 100))
        .andThenAPublished(MoneyWithdrawnEvent("a1", "tf1", 100, 500))
        .whenPublishingA(MoneyDepositedEvent("a2", "tf1", 100, 400))
        .expectDispatchedCommands(CompleteMoneyTransferCommand("tf1"))
  }

  @Test
  fun sagaEnds() {
    fixture
        .givenAPublished(MoneyTransferRequestedEvent("tf1", "a1", "a2", 100))
        .andThenAPublished(MoneyWithdrawnEvent("a1", "tf1", 100, 500))
        .andThenAPublished(MoneyDepositedEvent("a2", "tf1", 100, 400))
        .whenPublishingA(MoneyTransferCompletedEvent("tf1"))
        .expectActiveSagas(0)
        .expectNoDispatchedCommands()
  }
}