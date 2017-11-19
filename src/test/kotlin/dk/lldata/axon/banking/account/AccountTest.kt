package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.Test


//import org.axonframework.test.Fixtures;

class AccountTest {
  var fixture: AggregateTestFixture<Account>

  constructor() {
    fixture = AggregateTestFixture(Account::class.java)
  }

  @Test
  fun createAccount() {
    fixture.givenNoPriorActivity()
        .`when`(CreateAccountCommand("1234", 1000))
        .expectEvents(AccountCreatedEvent("1234", "1234", 1000, 0))
  }

  @Test
  fun withDrawReasonableAmount() {
    fixture.given(AccountCreatedEvent("1234", ID.uuid(), 1000, 0))
        .`when`(WithdrawMoneyCommand("1234", "tx1", 600))
        .expectEvents(MoneyWithdrawnEvent("1234", "tx1", 600, -600))
  }

  @Test
  fun withDrawLargeAmount() {
    fixture.given(AccountCreatedEvent("1234", ID.uuid(), 1000, 0))
        .`when`(WithdrawMoneyCommand("1234", "tx1",1001))
        .expectNoEvents()
        .expectException(OverdraftLimitExceeded::class.java)
  }

  @Test
  fun withdrawTwice() {
    fixture.given(
        AccountCreatedEvent("1234", ID.uuid(), 1000, 0),
        MoneyWithdrawnEvent("1234", "tx1", 999, -999)
    )
        .`when`(WithdrawMoneyCommand("1234", "tx1", 2))
        .expectNoEvents()
        .expectException(OverdraftLimitExceeded::class.java)
  }

  @Test
  fun deposit() {
    fixture.given(AccountCreatedEvent("1234", ID.uuid(), 1000, 0))
        .`when`(DepositMoneyCommand("1234", "tx1", 500))
        .expectEvents(MoneyDepositedEvent("1234","tx1",500, 500))
  }
}