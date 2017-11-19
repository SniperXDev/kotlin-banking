package dk.lldata.axon.banking.coreapi

import java.util.*

class ID(val id: String) {

  companion object {
    private var count: Int = 1
    fun random() = random("")
    fun random(prefix: String) = (prefix + count++)

    fun uuid(): String = UUID.randomUUID().toString()
  }
}
