package dk.lldata.axon.banking.balance

import dk.lldata.axon.banking.coreapi.BalanceUpdatedEvent
import dk.lldata.axon.banking.coreapi.MoneyDepositedEvent
import dk.lldata.axon.banking.coreapi.MoneyWithdrawnEvent
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@ProcessingGroup("TrackingProcessingGroup")
@RestController
class TransactionHistoryEventHandler(
    val repo : TransactionHistoryRepository
) {
  @EventHandler
  fun on(event : BalanceUpdatedEvent) {
    repo.save(TransactionHistory(event.accountId, event.balance))
  }

  @GetMapping("/history/{accountId}")
  fun history(@PathVariable accountId : String) : List<TransactionHistory> {
    return repo.findByAccountId(accountId)
  }
}

@Entity
class TransactionHistory(
    @Basic var accountId : String = "",
    @Basic var balance : Int = 0,
    @GeneratedValue @Id var id : Long = 0
    ) {


}

interface TransactionHistoryRepository : JpaRepository<TransactionHistory, Long> {
  fun findByAccountId(accountId: String) : List<TransactionHistory>
}