package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.AccountCreatedEvent
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
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
}