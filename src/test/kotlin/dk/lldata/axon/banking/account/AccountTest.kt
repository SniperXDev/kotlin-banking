package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.AccountCreatedEvent
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.MoneyWithdrawnEvent
import dk.lldata.axon.banking.coreapi.WithdrawMoneyCommand
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
        .expectEvents(AccountCreatedEvent("1234", 1000))
  }

  @Test
  fun withDrawReasonableAmount() {
    fixture.given(AccountCreatedEvent("1234", 1000))
        .`when`(WithdrawMoneyCommand("1234", 600))
        .expectEvents(MoneyWithdrawnEvent("1234", 600, -600))
  }

  @Test
  fun withDrawLargeAmount() {
    fixture.given(AccountCreatedEvent("1234", 1000))
        .`when`(WithdrawMoneyCommand("1234", 1001))
        .expectNoEvents()
        .expectException(OverdraftLimitExceeded::class.java)
  }

  @Test
  fun withdrawTwice() {
    fixture.given(
        AccountCreatedEvent("1234", 1000),
        MoneyWithdrawnEvent("1234", 999, -999)
    )
        .`when`(WithdrawMoneyCommand("1234", 2))
        .expectNoEvents()
        .expectException(OverdraftLimitExceeded::class.java)
  }
}