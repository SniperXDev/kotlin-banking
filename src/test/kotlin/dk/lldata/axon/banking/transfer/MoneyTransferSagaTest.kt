package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.*
import org.axonframework.spring.stereotype.Saga
import org.axonframework.test.saga.SagaTestFixture
import org.junit.Test

@Saga
class MoneyTransferSagaTest {
  val fixture = SagaTestFixture(MoneyTransferSaga::class.java)

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

//  Hmmm. Not as easy to test as I imagined ..
//  @Test
//  fun moneyTransferRequestExceedLimit() {
//    fixture.givenAPublished(AccountCreatedEvent("a1", 1000))
//        .andThenAPublished(AccountCreatedEvent("a2", 1000))
//        .whenPublishingA(MoneyTransferRequestedEvent("tf1", "a1", "a2", 10000))
//        .expectActiveSagas(1)
//        .expectDispatchedCommands(WithdrawMoneyCommand("a1", "tf1",10000))
//        .expectNoScheduledEvents()
//        //.expectActiveSagas(0)
//        .expectDispatchedCommands(CancelMoneyTransferCommand("tf1"))
//  }

}