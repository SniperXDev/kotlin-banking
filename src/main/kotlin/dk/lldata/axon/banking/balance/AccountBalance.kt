package dk.lldata.axon.banking.balance

import dk.lldata.axon.banking.coreapi.BalanceUpdatedEvent
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class AccountBalance(
    @Id var accountId: String = "",
    @Basic var balance: Int = 0
) {
}

@Component
class AccountBalanceEventHandler(val repo: AccountBalanceRepository) {

  @EventHandler
  fun on(event: BalanceUpdatedEvent) {
    repo.save(AccountBalance(event.accountId, event.balance))
  }
}

interface AccountBalanceRepository : JpaRepository<AccountBalance, String>